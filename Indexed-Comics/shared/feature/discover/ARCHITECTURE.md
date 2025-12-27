# Discover Feature 模块架构规划

## 📋 模块概述

**discover（发现）** 是应用的核心功能模块，负责帮助用户发现和浏览动漫/漫画内容。这是用户进入应用后的主要入口页面。

**包名**: `com.pusu.indexed.shared.feature.discover`

---

## 🎯 功能定位

Discover 模块承担以下核心功能：

1. **内容发现**: 展示热门、推荐、新番等内容
2. **分类浏览**: 按类型、季度、排行榜等维度浏览
3. **快速访问**: 随机推荐、今日更新等快捷入口
4. **搜索入口**: 提供内容搜索功能

---

## 📁 推荐目录结构

### 整体分层架构

```
shared/
├── domain/discover/              # 🔵 领域层（业务逻辑）
│   ├── model/                   # 领域模型
│   ├── usecase/                 # 用例
│   └── repository/              # 仓库接口（可选）
│
├── data/jikan/                  # 🟢 数据层（已存在）
│   └── 实现 domain 层定义的数据获取
│
└── feature/discover/            # 🟡 表现层（UI）
    ├── presentation/            # ViewModel, State
    └── ui/                      # Compose UI 组件
```

### Domain 层：`shared/domain/discover/`

**路径**: `shared/domain/discover/src/commonMain/kotlin/com/pusu/indexed/domain/discover/`

```
discover/
├── build.gradle.kts
└── src/
    └── commonMain/kotlin/com/pusu/indexed/domain/discover/
        ├── model/                              # 领域模型
        │   ├── DiscoverContent.kt             # 发现内容聚合
        │   ├── AnimeItem.kt                   # 动漫项（简化模型）
        │   ├── MangaItem.kt                   # 漫画项
        │   ├── Genre.kt                       # 类型/题材
        │   ├── Season.kt                      # 季度信息
        │   ├── ContentFilter.kt               # 内容过滤器
        │   └── RankingType.kt                 # 排名类型枚举
        │
        ├── usecase/                            # 用例
        │   ├── GetTrendingAnimeUseCase.kt     # 获取热门动漫
        │   ├── GetTrendingMangaUseCase.kt     # 获取热门漫画
        │   ├── GetCurrentSeasonAnimeUseCase.kt # 获取本季新番
        │   ├── GetTopRankedUseCase.kt         # 获取排行榜
        │   ├── GetRandomPickUseCase.kt        # 获取随机推荐
        │   ├── GetRecommendationsUseCase.kt   # 获取推荐内容
        │   ├── GetGenresUseCase.kt            # 获取类型列表
        │   └── GetSchedulesUseCase.kt         # 获取播放时间表
        │
        └── repository/                         # 仓库接口（可选）
            └── DiscoverRepository.kt          # 定义数据获取接口
```

### Feature 层：`shared/feature/discover/`

**路径**: `shared/feature/discover/src/commonMain/kotlin/com/pusu/indexed/shared/feature/discover/`

```
discover/
├── build.gradle.kts
└── src/
    └── commonMain/kotlin/com/pusu/indexed/shared/feature/discover/
        ├── DiscoverScreen.kt                   # 主屏幕组件
        │
        ├── presentation/                       # 表现层
        │   ├── DiscoverViewModel.kt           # ViewModel
        │   ├── DiscoverUiState.kt             # UI 状态
        │   ├── DiscoverUiEvent.kt             # UI 事件
        │   └── DiscoverIntent.kt              # 用户意图
        │
        ├── ui/                                 # UI 组件
        │   ├── components/                    # 可复用组件
        │   │   ├── AnimeCard.kt              # 动漫卡片
        │   │   ├── MangaCard.kt              # 漫画卡片
        │   │   ├── CategoryChip.kt           # 分类标签
        │   │   ├── HorizontalCarousel.kt     # 横向轮播
        │   │   ├── RankingBadge.kt           # 排名徽章
        │   │   └── ScoreLabel.kt             # 评分标签
        │   │
        │   └── sections/                      # 页面区块
        │       ├── TrendingSection.kt        # 热门趋势
        │       ├── CurrentSeasonSection.kt   # 本季新番
        │       ├── RandomPickSection.kt      # 随机推荐
        │       ├── GenresSection.kt          # 分类浏览
        │       ├── TopChartsSection.kt       # 排行榜
        │       └── ScheduleSection.kt        # 播放时间表
        │
        └── navigation/                         # 导航
            └── DiscoverNavigation.kt          # 导航定义
```

---

## 🎨 核心功能模块

### 1. **热门趋势 (Trending Section)**

展示当前最热门的动漫/漫画内容。

**数据来源**: 
- `jikanApi.top.getTopAnime()` - 排行榜动漫
- `jikanApi.top.getTopManga()` - 排行榜漫画

**UI 组件**:
- 横向滚动的卡片列表
- 显示封面、标题、评分、排名

---

### 2. **本季新番 (Current Season Section)**

展示当前季度的新番动漫。

**数据来源**:
- `jikanApi.seasons.getCurrentSeasonAnime()` - 当前季度
- `jikanApi.seasons.getUpcomingSeasonAnime()` - 即将播出

**UI 组件**:
- 大横幅轮播图（顶部推荐）
- 网格布局展示列表

---

### 3. **分类浏览 (Category Browsing)**

按类型、题材浏览内容。

**数据来源**:
- `jikanApi.genres.getAnimeGenres()` - 动漫类型
- `jikanApi.genres.getMangaGenres()` - 漫画类型

**UI 组件**:
- 横向滚动的分类标签
- 点击进入分类详情页

---

### 4. **排行榜快速入口 (Top Charts)**

提供各种排行榜的快速访问。

**数据来源**:
- `jikanApi.top.getTopAnime(filter = "bypopularity")` - 人气排行
- `jikanApi.top.getTopAnime(filter = "favorite")` - 收藏排行
- `jikanApi.top.getTopAnime(filter = "airing")` - 正在播出排行

**UI 组件**:
- Tab 切换不同排行榜
- 紧凑的列表展示（排名 + 封面 + 标题 + 评分）

---

### 5. **随机推荐 (Random Pick)**

每日一推、随机发现。

**数据来源**:
- `jikanApi.random.getRandomAnime()` - 随机动漫
- `jikanApi.random.getRandomManga()` - 随机漫画

**UI 组件**:
- 大卡片展示
- "换一个" 按钮

---

### 6. **推荐内容 (Recommendations)**

基于热门推荐的内容。

**数据来源**:
- `jikanApi.recommendations.getRecentAnimeRecommendations()` - 最新推荐
- `jikanApi.watch.getPopularEpisodes()` - 热门剧集

**UI 组件**:
- 双列卡片（A推荐B形式）
- 推荐理由文字

---

### 7. **播放时间表 (Schedule)**

本周播放时间表。

**数据来源**:
- `jikanApi.schedules.getSchedules()` - 播放时间表

**UI 组件**:
- 按星期分组
- 时间线展示

---

## 📊 数据流设计

### MVI 架构模式

```kotlin
// 1. UI 状态
data class DiscoverUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val trendingAnime: List<AnimeItem> = emptyList(),
    val currentSeasonAnime: List<AnimeItem> = emptyList(),
    val topAnime: List<AnimeItem> = emptyList(),
    val randomPick: AnimeItem? = null,
    val genres: List<Genre> = emptyList(),
    val recommendations: List<RecommendationPair> = emptyList(),
    val schedule: Map<DayOfWeek, List<ScheduleItem>> = emptyMap()
)

// 2. 用户意图
sealed interface DiscoverIntent {
    object LoadContent : DiscoverIntent
    object RefreshContent : DiscoverIntent
    data class SelectGenre(val genreId: Int) : DiscoverIntent
    object GetRandomPick : DiscoverIntent
    data class NavigateToDetail(val id: Int, val type: ContentType) : DiscoverIntent
}

// 3. UI 事件
sealed interface DiscoverUiEvent {
    data class ShowError(val message: String) : DiscoverUiEvent
    data class NavigateToDetail(val route: String) : DiscoverUiEvent
    data class NavigateToList(val category: String) : DiscoverUiEvent
}
```

---

## 🔄 UseCase 示例

```kotlin
class GetDiscoverContentUseCase(
    private val jikanApi: JikanApi
) {
    suspend operator fun invoke(): Result<DiscoverContent> = runCatching {
        // 并发获取多个数据源
        coroutineScope {
            val trendingDeferred = async { jikanApi.top.getTopAnime(limit = 10) }
            val currentSeasonDeferred = async { jikanApi.seasons.getCurrentSeasonAnime() }
            val randomDeferred = async { jikanApi.random.getRandomAnime() }
            val genresDeferred = async { jikanApi.genres.getAnimeGenres() }
            
            DiscoverContent(
                trending = trendingDeferred.await().getOrThrow().data,
                currentSeason = currentSeasonDeferred.await().getOrThrow().data,
                randomPick = randomDeferred.await().getOrThrow().data,
                genres = genresDeferred.await().getOrThrow().data
            )
        }
    }
}
```

---

## 🎨 UI 布局建议

### 主屏幕布局（垂直滚动）

```
┌─────────────────────────────────┐
│  Header / Search Bar            │ ← 固定顶栏
├─────────────────────────────────┤
│  [本季新番大横幅轮播]              │ ← 全屏轮播
│  ● ● ○ ○ ○                      │
├─────────────────────────────────┤
│  🔥 热门排行                     │ ← 横向滚动
│  [卡片1] [卡片2] [卡片3] →       │
├─────────────────────────────────┤
│  📺 本季新番                     │ ← 横向滚动
│  [卡片1] [卡片2] [卡片3] →       │
├─────────────────────────────────┤
│  🎲 随机推荐                     │ ← 单个大卡片
│  [     大卡片     ] [换一个]     │
├─────────────────────────────────┤
│  🏷️ 分类浏览                    │ ← 横向标签
│  [动作][冒险][喜剧][科幻] →      │
├─────────────────────────────────┤
│  ⭐ 推荐给你                     │ ← 双列网格
│  [卡片1] [卡片2]                │
│  [卡片3] [卡片4]                │
├─────────────────────────────────┤
│  📅 本周时间表                   │ ← 时间线
│  周一: [番1][番2]               │
│  周二: [番3][番4]               │
└─────────────────────────────────┘
```

---

## 🧩 模块依赖关系

### 分层依赖图

```
┌─────────────────────────────────────────────┐
│  feature:discover (表现层 - UI)              │
│  - ViewModel, UiState, Compose UI           │
└─────────────────┬───────────────────────────┘
                  │ 依赖
                  ↓
┌─────────────────────────────────────────────┐
│  domain:discover (领域层 - 业务逻辑)         │
│  - UseCase, Model, Repository Interface    │
└─────────────────┬───────────────────────────┘
                  │ 依赖
                  ↓
┌─────────────────────────────────────────────┐
│  data:jikan (数据层 - API 实现)              │
│  - Repository Implementation, API Client   │
└─────────────────────────────────────────────┘
```

### 详细依赖关系

```kotlin
// domain:discover 依赖
dependencies {
    implementation(project(":shared:core:model"))      // 共享基础模型
    implementation(project(":shared:data:jikan"))      // Jikan API
}

// feature:discover 依赖
dependencies {
    implementation(project(":shared:domain:discover")) // ✅ 领域层
    implementation(project(":shared:core:ui"))         // 共享 UI 组件
    implementation(project(":shared:domain:feed"))     // 订阅/收藏逻辑
    implementation(project(":shared:navigation"))      // 导航
    
    // Compose 相关
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
}
```

### 跨模块调用流程

```
UI 点击事件
    ↓
ViewModel.handleIntent()
    ↓
UseCase.invoke() (domain 层)
    ↓
JikanApi 调用 (data 层)
    ↓
返回领域模型
    ↓
更新 UiState
    ↓
UI 重组
```

### 导航流转

```
Discover Screen
  ├─→ Anime Detail (anime-detail feature)
  ├─→ Manga Detail (manga-detail feature)
  ├─→ Genre List (可以是 discover 内部的子页面)
  ├─→ Top Charts (可以是 discover 内部的子页面)
  ├─→ Season List (可以是 discover 内部的子页面)
  └─→ Search Results (search feature, 未来功能)
```

---

## 📝 实现优先级

### Phase 1: MVP (最小可用产品)
- ✅ 基础框架搭建（ViewModel, UiState）
- ✅ 热门排行 Section
- ✅ 本季新番 Section
- ✅ 随机推荐 Section
- ✅ 基础导航到详情页

### Phase 2: 增强功能
- ⭐ 分类浏览
- ⭐ 排行榜多维度切换
- ⭐ 推荐内容展示
- ⭐ 下拉刷新

### Phase 3: 高级功能
- 🚀 播放时间表
- 🚀 个性化推荐（基于用户历史）
- 🚀 内容预加载优化
- 🚀 离线缓存

---

## 🎯 关键技术点

### 1. 数据加载策略
- 使用 `Flow` 实现响应式数据流
- 实现分页加载（`PagingSource`）
- 错误处理和重试机制

### 2. 性能优化
- 图片懒加载（Coil/Kamel）
- 列表虚拟化
- 数据预取

### 3. 状态管理
- MVI 架构模式
- 单向数据流
- 使用 `StateFlow` 管理状态

### 4. 测试
- ViewModel 单元测试
- UseCase 单元测试
- UI 快照测试

---

## 📚 参考资源

### 类似应用参考
- **Anilist** - 动漫追踪应用
- **MyAnimeList** - 官方应用
- **Kitsu** - 动漫社区应用

### 设计规范
- Material Design 3
- Compose Multiplatform 最佳实践
- iOS Human Interface Guidelines (for iOS target)

---

## 🔧 下一步行动

1. **创建基础结构**
   - 创建包结构
   - 定义 UiState、Intent、Event

2. **实现第一个 UseCase**
   - `GetTopAnimeUseCase`
   - 连接 Jikan API

3. **实现第一个 Section**
   - `TrendingSection` 组件
   - 展示热门动漫卡片

4. **连接 ViewModel**
   - 实现数据加载逻辑
   - 状态管理

5. **完善 UI**
   - 添加加载状态
   - 错误处理 UI
   - 空状态 UI

---

**规划制定**: 2025-12-25  
**目标**: 打造一个现代化、流畅的动漫/漫画发现体验

