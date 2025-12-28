package com.pusu.indexed.androidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.pusu.indexed.shared.feature.discover.DiscoverScreen
import com.pusu.indexed.shared.feature.animedetail.AnimeDetailScreen

/**
 * 主 Activity
 * 
 * 简单的导航实现：使用 State 来管理当前显示的页面
 */
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}

/**
 * 应用导航组件
 */
@Composable
private fun AppNavigation() {
    // 当前页面状态
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Discover) }
    val scope = rememberCoroutineScope()
    
    // 处理返回按钮
    BackHandler(enabled = currentScreen is Screen.AnimeDetail) {
        currentScreen = Screen.Discover
    }
    
    when (val screen = currentScreen) {
        is Screen.Discover -> {
            val viewModel = remember {
                App.instance.dependencyContainer.createDiscoverViewModel(scope)
            }
            
            DiscoverScreen(
                viewModel = viewModel,
                onNavigateToDetail = { animeId ->
                    currentScreen = Screen.AnimeDetail(animeId)
                }
            )
        }
        is Screen.AnimeDetail -> {
            val viewModel = remember(screen.animeId) {
                App.instance.dependencyContainer.createAnimeDetailViewModel(scope)
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

/**
 * 屏幕定义
 */
private sealed class Screen {
    data object Discover : Screen()
    data class AnimeDetail(val animeId: Int) : Screen()
}
