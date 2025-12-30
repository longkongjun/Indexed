package com.pusu.indexed.comics.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.pusu.indexed.comics.di.DependencyContainer
import com.pusu.indexed.shared.feature.discover.DiscoverScreen
import com.pusu.indexed.shared.feature.animedetail.AnimeDetailScreen
import com.pusu.indexed.shared.feature.search.SearchScreen
import kotlinx.coroutines.CoroutineScope

/**
 * 屏幕定义
 */
sealed class Screen {
    data object Discover : Screen()
    data object Search : Screen()
    data class AnimeDetail(val animeId: Int) : Screen()
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
    // 当前页面状态
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Discover) }
    
    // 缓存 DiscoverViewModel，避免每次返回时重新创建
    val discoverViewModel = remember {
        dependencyContainer.createDiscoverViewModel(scope)
    }
    
    // 缓存 SearchViewModel
    val searchViewModel = remember {
        dependencyContainer.createSearchViewModel(scope)
    }
    
    when (val screen = currentScreen) {
        is Screen.Discover -> {
            DiscoverScreen(
                viewModel = discoverViewModel,
                onNavigateToDetail = { animeId ->
                    currentScreen = Screen.AnimeDetail(animeId)
                },
                onNavigateToSearch = {
                    currentScreen = Screen.Search
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
    }
}

