# Discover Feature Module

## 📦 模块说明

这是发现（Discover）功能的 Feature 层实现，包含 UI 和 Presentation 逻辑。

---

## 🏗️ 架构

### MVI (Model-View-Intent) 架构

```
用户操作 → Intent → ViewModel → UseCase → Repository
             ↓
          UiState
             ↓
          UI 重组
```

### 文件结构

```
shared/feature/discover/
├── presentation/
│   ├── DiscoverViewModel.kt      # ViewModel（处理业务逻辑）
│   ├── DiscoverUiState.kt        # UI 状态（不可变数据类）
│   ├── DiscoverIntent.kt         # 用户意图（所有可能的操作）
│   └── DiscoverUiEvent.kt        # UI 事件（一次性事件）
└── DiscoverScreen.kt             # UI 组件（Composable）
```

---

## 🔗 依赖关系

### Gradle 依赖

```kotlin
dependencies {
    // 只依赖 Domain 层
    implementation(project(":shared:domain:discover"))
    
    // ❌ 不依赖 Data 层
    // implementation(project(":shared:data:jikan"))  // 错误！
}
```

### 代码依赖

```
DiscoverScreen
    ↓ 使用
DiscoverViewModel
    ↓ 使用
GetTrendingAnimeUseCase (来自 Domain 层)
    ↓ 使用
DiscoverRepository 接口 (来自 Domain 层)
```

**关键点**：
- ✅ Feature 层只知道 **Domain 层的接口**
- ❌ Feature 层不知道 **Data 层的实现**
- ✅ 完全隔离，易于测试和替换

---

## 🎯 如何使用

### 1. 在 App 层配置依赖注入

**不要在 Feature 层配置 DI！** DI 配置应该在 App 层（Android、Desktop、iOS）。

#### Android 示例

```kotlin
// apps/android/src/main/kotlin/di/AppModule.kt
val appModule = module {
    // Data 层
    single { JikanApi.create(...) }
    single<DiscoverRepository> { JikanDiscoverRepository(...) }
    
    // Domain 层
    factory { GetTrendingAnimeUseCase(get()) }
    
    // Feature 层
    factory { (scope: CoroutineScope) ->
        DiscoverViewModel(
            getTrendingAnimeUseCase = get(),
            coroutineScope = scope
        )
    }
}
```

#### Desktop 示例

```kotlin
// apps/desktop/src/jvmMain/kotlin/Main.kt
fun main() = application {
    // 手动创建依赖链
    val repository: DiscoverRepository = JikanDiscoverRepository(...)
    val useCase = GetTrendingAnimeUseCase(repository)
    val viewModel = DiscoverViewModel(useCase, scope)
    
    Window(...) {
        DiscoverScreen(viewModel)
    }
}
```

### 2. 在 UI 中使用

```kotlin
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController, startDestination = "discover") {
        composable("discover") {
            val scope = rememberCoroutineScope()
            val viewModel: DiscoverViewModel = koinInject { 
                parametersOf(scope) 
            }
            
            DiscoverScreen(
                viewModel = viewModel,
                onNavigateToDetail = { animeId ->
                    navController.navigate("detail/$animeId")
                }
            )
        }
    }
}
```

---

## 📊 数据流示例

### 加载热门动漫

```
1. UI 触发
   DiscoverScreen 渲染 → ViewModel.init()

2. ViewModel 处理
   handleIntent(DiscoverIntent.LoadContent)
   ↓
   _uiState.update { it.copy(isLoading = true) }

3. 调用 UseCase
   getTrendingAnimeUseCase(page = 1, limit = 10)
   ↓
   (UseCase 内部调用 Repository 接口)

4. 接收数据
   .onSuccess { animeList ->
       _uiState.update { 
           it.copy(
               isLoading = false,
               trendingAnime = animeList
           )
       }
   }

5. UI 重组
   val uiState by viewModel.uiState.collectAsState()
   LazyRow { items(uiState.trendingAnime) { ... } }
```

---

## 🧪 测试

### ViewModel 测试

```kotlin
class DiscoverViewModelTest {
    @Test
    fun `test load content success`() = runTest {
        // Mock UseCase
        val mockUseCase = mockk<GetTrendingAnimeUseCase>()
        coEvery { mockUseCase(any(), any()) } returns Result.success(
            listOf(createTestAnimeItem())
        )
        
        // 创建 ViewModel
        val viewModel = DiscoverViewModel(mockUseCase, this)
        
        // 验证状态
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.trendingAnime.isNotEmpty())
        }
    }
}
```

### UI 测试

```kotlin
class DiscoverScreenTest {
    @Test
    fun `test loading state displays correctly`() {
        composeTestRule.setContent {
            val viewModel = createTestViewModel(
                initialState = DiscoverUiState(isLoading = true)
            )
            DiscoverScreen(viewModel)
        }
        
        composeTestRule
            .onNodeWithText("加载中...")
            .assertIsDisplayed()
    }
}
```

---

## 🔄 添加新功能

### 添加新的 UseCase

1. 在 Domain 层定义 UseCase：

```kotlin
// shared/domain/discover/usecase/SearchAnimeUseCase.kt
class SearchAnimeUseCase(
    private val repository: DiscoverRepository
) {
    suspend operator fun invoke(query: String): Result<List<AnimeItem>> {
        return repository.searchAnime(query)
    }
}
```

2. 在 ViewModel 中使用：

```kotlin
class DiscoverViewModel(
    private val getTrendingAnimeUseCase: GetTrendingAnimeUseCase,
    private val searchAnimeUseCase: SearchAnimeUseCase,  // 新增
    private val coroutineScope: CoroutineScope
) {
    // ...
}
```

3. 在 App 层配置 DI：

```kotlin
factory { SearchAnimeUseCase(get()) }
factory { (scope: CoroutineScope) ->
    DiscoverViewModel(get(), get(), scope)  // 注入两个 UseCase
}
```

---

## 📚 相关文档

- [完整依赖链](../../../docs/complete_dependency_chain.md)
- [依赖注入指南](../../../docs/dependency_injection_guide.md)
- [Android 集成示例](../../../docs/android_integration_example.md)
- [架构文档](./ARCHITECTURE.md)

---

## ✅ 编译验证

```bash
# 编译所有平台
./gradlew :shared:feature:discover:build

# 只编译 Desktop
./gradlew :shared:feature:discover:desktopJar

# 只编译 Android
./gradlew :shared:feature:discover:assembleDebug
```

---

## 🎯 关键原则

1. **只依赖 Domain 层** ✅
2. **不依赖 Data 层** ✅
3. **DI 配置在 App 层** ✅
4. **使用 MVI 架构** ✅
5. **状态不可变** ✅
6. **单向数据流** ✅

---

**状态**: ✅ 完成  
**编译**: ✅ 通过  
**平台**: Android, iOS, Desktop, Web

