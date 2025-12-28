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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json

/**
 * iOS 应用入口
 */
@Composable
fun IOSApp() {
    // 创建依赖容器
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
                level = LogLevel.ALL
            }
        }
    }
    
    val dependencyContainer = remember { DependencyContainer(httpClient) }
    val scope = remember { CoroutineScope(SupervisorJob() + Dispatchers.Main) }
    
    // 简单的导航状态管理
    var currentScreen by remember { mutableStateOf<IOSScreen>(IOSScreen.Discover) }
    
    when (val screen = currentScreen) {
        is IOSScreen.Discover -> {
            val viewModel = remember {
                dependencyContainer.createDiscoverViewModel(scope)
            }
            
            DiscoverScreen(
                viewModel = viewModel,
                onNavigateToDetail = { animeId ->
                    currentScreen = IOSScreen.AnimeDetail(animeId)
                },
                onNavigateToSearch = {
                    currentScreen = IOSScreen.Search
                }
            )
        }
        is IOSScreen.Search -> {
            val viewModel = remember {
                dependencyContainer.createSearchViewModel(scope)
            }
            
            SearchScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    currentScreen = IOSScreen.Discover
                },
                onNavigateToDetail = { animeId ->
                    currentScreen = IOSScreen.AnimeDetail(animeId)
                }
            )
        }
        is IOSScreen.AnimeDetail -> {
            val viewModel = remember(screen.animeId) {
                dependencyContainer.createAnimeDetailViewModel(scope)
            }
            
            AnimeDetailScreen(
                animeId = screen.animeId,
                viewModel = viewModel,
                onNavigateBack = {
                    currentScreen = IOSScreen.Discover
                },
                onNavigateToAnimeDetail = { animeId ->
                    currentScreen = IOSScreen.AnimeDetail(animeId)
                }
            )
        }
    }
}

/**
 * iOS 屏幕定义
 */
private sealed class IOSScreen {
    data object Discover : IOSScreen()
    data object Search : IOSScreen()
    data class AnimeDetail(val animeId: Int) : IOSScreen()
}

fun MainViewController() = ComposeUIViewController { IOSApp() }
