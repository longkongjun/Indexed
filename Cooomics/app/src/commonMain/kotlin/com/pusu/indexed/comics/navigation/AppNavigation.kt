package com.pusu.indexed.comics.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.pusu.indexed.comics.splash.SplashScreen
import com.pusu.indexed.shared.feature.animedetail.AnimeDetailScreen
import com.pusu.indexed.shared.feature.animedetail.animelist.AnimeListScreen
import com.pusu.indexed.shared.feature.animedetail.animelist.presentation.AnimeListType
import com.pusu.indexed.shared.feature.animedetail.presentation.AnimeDetailViewModel
import com.pusu.indexed.shared.feature.discover.DiscoverScreen
import com.pusu.indexed.shared.feature.discover.presentation.DiscoverViewModel
import com.pusu.indexed.shared.feature.search.SearchScreen
import com.pusu.indexed.shared.feature.search.presentation.SearchViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import androidx.compose.runtime.LaunchedEffect
import com.pusu.indexed.shared.feature.animedetail.animelist.presentation.AnimeListViewModel
import com.pusu.indexed.shared.feature.discover.filter.FilterScreen
import com.pusu.indexed.shared.feature.discover.filter.presentation.FilterViewModel
import org.koin.compose.koinInject

/**
 * 导航目标定义
 *
 * 使用 @Serializable 注解标记，以便 Navigation3 可以序列化和反序列化
 */
@Serializable
sealed interface Screen {
    @Serializable
    data object Splash : Screen, NavKey

    @Serializable
    data object Discover : Screen, NavKey

    @Serializable
    data object Search : Screen, NavKey

    @Serializable
    data object Filter : Screen, NavKey

    @Serializable
    data class AnimeDetail(val animeId: Int) : Screen, NavKey

    @Serializable
    data class AnimeList(val listType: String) : Screen, NavKey
}

/**
 * SavedState 配置
 *
 * 配置多态序列化以支持 NavKey 的所有子类型
 */
private val savedStateConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Screen.Splash::class)
            subclass(Screen.Discover::class)
            subclass(Screen.Search::class)
            subclass(Screen.Filter::class)
            subclass(Screen.AnimeDetail::class)
            subclass(Screen.AnimeList::class)
        }
    }
}

/**
 * 应用导航组件
 *
 * 使用 Navigation3 (Multiplatform) 实现类型安全的导航
 */
@Composable
fun AppNavigation() {
    // 创建导航返回栈，从 Splash 页面开始，使用配置好的序列化模块
    val backStack = rememberNavBackStack(savedStateConfig, Screen.Splash)

    // 使用 NavDisplay 显示当前导航状态
    // 添加 ViewModel 装饰器，使每个 NavEntry 都有自己的 ViewModelStore
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            // 注意：装饰器顺序很重要！
            // SaveableStateHolder 必须在 ViewModelStore 之前，以支持 SavedStateHandle
            rememberSaveableStateHolderNavEntryDecorator(),
            // ViewModelStore 装饰器：为每个导航目的地提供独立的 ViewModel 存储
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            // 启动页
            entry<Screen.Splash> {
                SplashScreen(
                    onSplashFinished = {
                        // 启动完成后，导航到发现页并清除启动页
                        backStack.clear()
                        backStack.add(Screen.Discover)
                    }
                )
            }

                    // 发现页（主页）
                    entry<Screen.Discover> {
                        // 使用 Koin Compose 注入 ViewModel
                        val viewModel: DiscoverViewModel = koinInject<DiscoverViewModel>()

                DiscoverScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { animeId ->
                        backStack.add(Screen.AnimeDetail(animeId))
                    },
                    onNavigateToSearch = {
                        backStack.add(Screen.Search)
                    },
                    onNavigateToFilter = {
                        backStack.add(Screen.Filter)
                    },
                    onNavigateToList = { listType ->
                        backStack.add(Screen.AnimeList(listType.name))
                    }
                )
            }

                    // 搜索页
                    entry<Screen.Search> {
                        // 使用 Koin Compose 注入 ViewModel
                        val viewModel: SearchViewModel = koinInject<SearchViewModel>()

                SearchScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        backStack.removeLastOrNull()
                    },
                    onNavigateToDetail = { animeId ->
                        backStack.add(Screen.AnimeDetail(animeId))
                    }
                )
            }

            // 筛选页
            entry<Screen.Filter> {
                // 使用 Koin Compose 注入 ViewModel
                val viewModel: FilterViewModel = koinInject<FilterViewModel>()

                FilterScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        backStack.removeLastOrNull()
                    },
                    onNavigateToDetail = { animeId ->
                        backStack.add(Screen.AnimeDetail(animeId))
                    }
                )
            }

            // 动漫详情页
            entry<Screen.AnimeDetail> { screen ->
                // 使用 Koin Compose 注入 ViewModel（每次导航创建新实例）
                val viewModel: AnimeDetailViewModel = koinInject<AnimeDetailViewModel>()

                AnimeDetailScreen(
                    animeId = screen.animeId,
                    viewModel = viewModel,
                    onNavigateBack = {
                        backStack.removeLastOrNull()
                    },
                    onNavigateToAnimeDetail = { animeId ->
                        backStack.add(Screen.AnimeDetail(animeId))
                    }
                )
            }

            // 动漫列表页
            entry<Screen.AnimeList> { screen ->
                val listType = AnimeListType.valueOf(screen.listType)

                // 使用 Koin Compose 注入 ViewModel
                val viewModel: AnimeListViewModel = koinInject<AnimeListViewModel>()
                
                // 初始化列表类型（使用 LaunchedEffect 确保每个 ViewModel 实例只初始化一次）
                LaunchedEffect(viewModel) {
                    viewModel.initListType(listType)
                }

                AnimeListScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        backStack.removeLastOrNull()
                    },
                    onNavigateToDetail = { animeId ->
                        backStack.add(Screen.AnimeDetail(animeId))
                    }
                )
            }
        }
    )
}
