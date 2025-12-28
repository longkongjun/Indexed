import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.window.ComposeUIViewController
import com.pusu.indexed.shared.feature.discover.DiscoverScreen
import com.pusu.indexed.shared.feature.animedetail.AnimeDetailScreen
import com.pusu.indexed.shared.feature.search.SearchScreen
import com.pusu.indexed.iosapp.di.DependencyContainer
import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json

/**
 * iOS 应用入口
 */
@Composable
fun IOSApp() {
    // 创建 HttpClient
    val httpClient = remember {
        HttpClient(Darwin) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.INFO
            }
        }
    }
    
    // 创建依赖容器
    val dependencyContainer = remember { DependencyContainer(httpClient) }
    val scope = remember { CoroutineScope(SupervisorJob() + Dispatchers.Main) }
    
    MaterialTheme {
        AppNavigation(dependencyContainer, scope)
    }
}

/**
 * 应用导航组件
 */
@Composable
private fun AppNavigation(
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

/**
 * 屏幕定义
 */
private sealed class Screen {
    data object Discover : Screen()
    data object Search : Screen()
    data class AnimeDetail(val animeId: Int) : Screen()
}

@OptIn(ExperimentalForeignApi::class)
fun MainViewController() = ComposeUIViewController(
    configure = {
        // 禁用帧率限制检查，允许使用高刷新率
        enforceStrictPlistSanityCheck = false
    }
) { 
    IOSApp() 
}
