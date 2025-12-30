# 动漫列表页功能文档

## 概述

动漫列表页是一个全功能的列表展示页面，支持：
- ✅ 下拉刷新
- ✅ 自动加载更多（滚动到底部）
- ✅ 网格布局（2列）
- ✅ 三种列表类型（热门、本季新番、排行榜）
- ✅ 错误处理和重试
- ✅ 跨平台支持（Android、iOS、Desktop、Web）

## 功能特性

### 1. 列表入口

在首页（DiscoverScreen）的每个区域标题右侧，点击"查看全部"按钮：

```
🔥 热门动漫        [查看全部] → 进入热门动漫列表
📺 本季新番        [查看全部] → 进入本季新番列表
🏆 排行榜          [查看全部] → 进入排行榜列表
```

### 2. 下拉刷新

- 向下拉动列表顶部，触发刷新
- 顶部显示加载进度条
- 刷新完成后显示"刷新成功"提示
- 刷新失败显示错误提示

**实现原理**:
```kotlin
// 监听刷新意图
when (intent) {
    is AnimeListIntent.Refresh -> refresh()
}

// 刷新逻辑
private fun refresh() {
    _uiState.update { it.copy(isRefreshing = true) }
    val result = loadData(page = 1)
    // 重置为第一页数据
}
```

### 3. 自动加载更多

- 滚动到列表底部自动触发
- 底部显示加载指示器
- 没有更多数据时显示"没有更多了"
- 加载失败显示错误提示

**触发时机**:
```kotlin
// 当滚动到倒数第 3 个 item 时触发
LaunchedEffect(listState) {
    snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
        .collect { lastVisibleIndex ->
            if (lastVisibleIndex >= totalItems - 3 && hasMore) {
                onIntent(AnimeListIntent.LoadMore)
            }
        }
}
```

### 4. 网格布局

- 2列网格展示
- 每个卡片包含：封面图、标题、评分
- 卡片高度固定（280dp）
- 12dp 间距

### 5. 列表类型

```kotlin
enum class AnimeListType {
    Trending,       // 热门动漫
    CurrentSeason,  // 本季新番
    TopRanked       // 排行榜
}
```

每种类型使用不同的 UseCase：
- `Trending` → `GetTrendingAnimeUseCase`
- `CurrentSeason` → `GetCurrentSeasonAnimeUseCase`
- `TopRanked` → `GetTopRankedAnimeUseCase`

## 架构设计

### 模块结构

```
shared/feature/anime-list/
├── build.gradle.kts
└── src/commonMain/kotlin/
    └── com/pusu/indexed/shared/feature/animelist/
        ├── AnimeListScreen.kt              # UI 层
        └── presentation/
            ├── AnimeListViewModel.kt       # ViewModel
            ├── AnimeListUiState.kt         # UI 状态
            ├── AnimeListIntent.kt          # 用户意图
            └── AnimeListUiEvent.kt         # UI 事件
```

### 数据流

```
用户操作 → Intent → ViewModel → UseCase → Repository → API
                      ↓
                   UiState
                      ↓
                   UI 更新
```

### 状态管理

```kotlin
data class AnimeListUiState(
    val animeList: List<AnimeItem> = emptyList(),
    val currentPage: Int = 1,
    val hasMore: Boolean = true,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val listType: AnimeListType = AnimeListType.Trending,
    val title: String = ""
)
```

### 分页逻辑

```kotlin
// 初始加载
loadData(page = 1) → 显示第 1 页数据

// 加载更多
loadData(page = currentPage + 1) → 追加到现有列表

// 刷新
loadData(page = 1) → 替换为第 1 页数据
```

## 使用方法

### 1. 在导航中使用

```kotlin
// 定义 Screen
sealed class Screen {
    data class AnimeList(val listType: AnimeListType) : Screen()
}

// 导航到列表页
currentScreen = Screen.AnimeList(AnimeListType.Trending)
```

### 2. 创建 ViewModel

```kotlin
val viewModel = dependencyContainer.createAnimeListViewModel(
    listType = AnimeListType.Trending,
    coroutineScope = scope
)
```

### 3. 显示列表页

```kotlin
AnimeListScreen(
    viewModel = viewModel,
    onNavigateBack = { /* 返回首页 */ },
    onNavigateToDetail = { animeId -> /* 跳转详情 */ }
)
```

## 性能优化

### 1. LazyGrid 优化

```kotlin
LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    state = listState,  // 保存滚动位置
    // ...
) {
    items(
        items = animeList,
        key = { anime -> anime.id }  // 稳定的 key
    ) { anime ->
        AnimeGridItem(anime)
    }
}
```

### 2. 避免重复加载

```kotlin
// 检查加载状态
if (isLoadingMore || !hasMore || hasError) {
    return  // 不触发加载
}
```

### 3. ViewModel 缓存

```kotlin
// 根据 listType 缓存 ViewModel
val viewModel = remember(screen.listType) {
    dependencyContainer.createAnimeListViewModel(screen.listType, scope)
}
```

## 错误处理

### 1. 初始加载失败

显示错误页面，提供"重试"按钮

### 2. 刷新失败

显示错误提示，保留原有数据

### 3. 加载更多失败

显示错误提示，保留原有数据，可重试

## 用户体验

### 1. 加载状态

- 初始加载：全屏 Loading
- 刷新：顶部进度条
- 加载更多：底部小 Loading

### 2. 空状态

- 无数据时显示"暂无内容"

### 3. 结束状态

- 没有更多数据时显示"没有更多了"

### 4. 平滑过渡

- 使用 `remember` 缓存 ViewModel
- 使用 `key` 优化列表性能
- 使用 `LaunchedEffect` 监听滚动

## 依赖关系

```
anime-list 模块依赖：
├── shared:domain:discover      # UseCase
├── shared:core:model           # 数据模型
├── shared:core:ui              # UI 组件
├── compose.material3           # Material Design 3
└── coil-compose                # 图片加载
```

```
discover 模块依赖 anime-list：
└── shared:feature:anime-list   # 用于 AnimeListType
```

## 配置文件

### settings.gradle.kts

```kotlin
include(":shared:feature:anime-list")
```

### composeApp/build.gradle.kts

```kotlin
implementation(project(":shared:feature:anime-list"))
```

### shared/feature/discover/build.gradle.kts

```kotlin
implementation(project(":shared:feature:anime-list"))
```

## 测试建议

1. **功能测试**
   - 测试下拉刷新
   - 测试滚动加载更多
   - 测试三种列表类型
   - 测试错误处理

2. **性能测试**
   - 测试大列表滚动性能
   - 测试图片加载性能
   - 测试内存占用

3. **跨平台测试**
   - Android
   - iOS
   - Desktop
   - Web

## 后续优化

- [ ] 添加搜索功能
- [ ] 添加筛选功能（按类型、年份等）
- [ ] 添加排序功能
- [ ] 支持列表/网格切换
- [ ] 添加收藏功能
- [ ] 缓存列表数据
- [ ] 支持离线浏览

## 相关文件

- `shared/feature/anime-list/` - 列表模块
- `composeApp/src/commonMain/.../navigation/AppNavigation.kt` - 导航配置
- `composeApp/src/commonMain/.../di/DependencyContainer.kt` - 依赖注入
- `shared/feature/discover/src/.../DiscoverScreen.kt` - 首页入口

