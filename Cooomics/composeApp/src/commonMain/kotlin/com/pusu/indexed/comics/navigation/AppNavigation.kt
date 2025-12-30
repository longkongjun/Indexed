package com.pusu.indexed.comics.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.pusu.indexed.comics.di.DependencyContainer
import com.pusu.indexed.comics.splash.SplashScreen
import com.pusu.indexed.shared.feature.animedetail.AnimeDetailScreen
import com.pusu.indexed.shared.feature.animedetail.animelist.AnimeListScreen
import com.pusu.indexed.shared.feature.animedetail.animelist.presentation.AnimeListType
import com.pusu.indexed.shared.feature.discover.DiscoverScreen
import com.pusu.indexed.shared.feature.search.SearchScreen
import kotlinx.coroutines.CoroutineScope

/**
 * 屏幕定义
 */
sealed class Screen {
    data object Splash : Screen()           // 启动页
    data object Discover : Screen()
    data object Search : Screen()
    data class AnimeDetail(val animeId: Int) : Screen()
    data class AnimeList(val listType: AnimeListType) : Screen()  // 列表页
}

/**
 * 应用导航组件
 * 
 * 简单的导航实现：使用 State 来管理当前显示的页面
 */
@Composable
fun AppNavigation(
    dependencyContainer: DependencyContainer,
    scope: CoroutineScope
) {
    // 当前页面状态（从启动页开始）
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }
    
    // 缓存 DiscoverViewModel，避免每次返回时重新创建
    val discoverViewModel = remember {
        dependencyContainer.createDiscoverViewModel(scope)
    }
    
    // 缓存 SearchViewModel
    val searchViewModel = remember {
        dependencyContainer.createSearchViewModel(scope)
    }
    
    when (val screen = currentScreen) {
        is Screen.Splash -> {
            SplashScreen(
                onSplashFinished = {
                    currentScreen = Screen.Discover
                }
            )
        }
        is Screen.Discover -> {
            DiscoverScreen(
                viewModel = discoverViewModel,
                onNavigateToDetail = { animeId ->
                    currentScreen = Screen.AnimeDetail(animeId)
                },
                onNavigateToSearch = {
                    currentScreen = Screen.Search
                },
                onNavigateToList = { listType ->
                    currentScreen = Screen.AnimeList(listType)
                }
            )
        }
        is Screen.Search -> {
            SearchScreen(
                viewModel = searchViewModel,
                onNavigateBack = {
                    currentScreen = Screen.Discover
                },
                onNavigateToDetail = { animeId ->
                    currentScreen = Screen.AnimeDetail(animeId)
                }
            )
        }
        is Screen.AnimeDetail -> {
            // 每个详情页使用独立的 ViewModel，根据 animeId 区分
            val viewModel = remember(screen.animeId) {
                dependencyContainer.createAnimeDetailViewModel(scope)
            }
            
            AnimeDetailScreen(
                animeId = screen.animeId,
                viewModel = viewModel,
                onNavigateBack = {
                    currentScreen = Screen.Discover
                },
                onNavigateToAnimeDetail = { animeId ->
                    currentScreen = Screen.AnimeDetail(animeId)
                }
            )
        }
        is Screen.AnimeList -> {
            // 列表页使用独立的 ViewModel，根据 listType 区分
            val viewModel = remember(screen.listType) {
                dependencyContainer.createAnimeListViewModel(screen.listType, scope)
            }
            
            AnimeListScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    currentScreen = Screen.Discover
                },
                onNavigateToDetail = { animeId ->
                    currentScreen = Screen.AnimeDetail(animeId)
                }
            )
        }
    }
}

