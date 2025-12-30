# 模块合并说明

## 变更概述

将 `anime-list` 模块合并到 `anime-detail` 模块中，简化项目结构。

## 变更原因

- 两个模块功能相关（都是动漫相关的页面）
- 减少模块数量，简化依赖关系
- 便于维护和管理

## 合并后的结构

```
shared/feature/anime-detail/
├── build.gradle.kts
└── src/commonMain/kotlin/
    └── com/pusu/indexed/shared/feature/
        ├── animedetail/              # 动漫详情
        │   ├── AnimeDetailScreen.kt
        │   ├── presentation/
        │   │   └── AnimeDetailViewModel.kt
        │   └── state/
        │       ├── AnimeDetailIntent.kt
        │       ├── AnimeDetailUiEvent.kt
        │       └── AnimeDetailUiState.kt
        └── animelist/                # 动漫列表
            ├── AnimeListScreen.kt
            └── presentation/
                ├── AnimeListIntent.kt
                ├── AnimeListUiEvent.kt
                ├── AnimeListUiState.kt
                └── AnimeListViewModel.kt
```

## Package 名称变更

### 旧的 Package 名称
```kotlin
com.pusu.indexed.shared.feature.animelist
com.pusu.indexed.shared.feature.animelist.presentation
```

### 新的 Package 名称
```kotlin
com.pusu.indexed.shared.feature.animedetail.animelist
com.pusu.indexed.shared.feature.animedetail.animelist.presentation
```

## 文件变更

### 1. 删除的模块
```
✗ shared/feature/anime-list/
```

### 2. 修改的文件

#### settings.gradle.kts
```kotlin
// 移除
- include(":shared:feature:anime-list")
```

#### composeApp/build.gradle.kts
```kotlin
// 移除
- implementation(project(":shared:feature:anime-list"))
```

#### shared/feature/discover/build.gradle.kts
```kotlin
// 修改依赖
- implementation(project(":shared:feature:anime-list"))
+ implementation(project(":shared:feature:anime-detail"))
```

#### shared/feature/anime-detail/build.gradle.kts
```kotlin
// 新增依赖
+ implementation(compose.materialIconsExtended)
+ implementation(project(":shared:domain:discover"))
```

### 3. Import 语句变更

所有引用 `anime-list` 的文件都需要更新 import：

```kotlin
// 旧的 import
import com.pusu.indexed.shared.feature.animelist.AnimeListScreen
import com.pusu.indexed.shared.feature.animelist.presentation.AnimeListType
import com.pusu.indexed.shared.feature.animelist.presentation.AnimeListViewModel

// 新的 import
import com.pusu.indexed.shared.feature.animedetail.animelist.AnimeListScreen
import com.pusu.indexed.shared.feature.animedetail.animelist.presentation.AnimeListType
import com.pusu.indexed.shared.feature.animedetail.animelist.presentation.AnimeListViewModel
```

#### 受影响的文件：
- `composeApp/src/commonMain/.../di/DependencyContainer.kt`
- `composeApp/src/commonMain/.../navigation/AppNavigation.kt`
- `shared/feature/discover/src/.../DiscoverScreen.kt`

## 功能验证

### ✅ 编译测试
- Android: 编译通过
- iOS: 编译通过
- Desktop: 支持
- Web: 支持

### ✅ 功能测试
- 动漫详情页：正常
- 动漫列表页：正常
- 首页"查看全部"入口：正常
- 下拉刷新：正常
- 自动加载更多：正常

## 使用方式

### 导入列表相关类
```kotlin
// 列表页 UI
import com.pusu.indexed.shared.feature.animedetail.animelist.AnimeListScreen

// 列表类型
import com.pusu.indexed.shared.feature.animedetail.animelist.presentation.AnimeListType

// ViewModel
import com.pusu.indexed.shared.feature.animedetail.animelist.presentation.AnimeListViewModel
```

### 创建 ViewModel
```kotlin
val viewModel = dependencyContainer.createAnimeListViewModel(
    listType = AnimeListType.Trending,
    coroutineScope = scope
)
```

### 显示列表页
```kotlin
AnimeListScreen(
    viewModel = viewModel,
    onNavigateBack = { /* 返回 */ },
    onNavigateToDetail = { animeId -> /* 详情 */ }
)
```

## 优势

### 1. 简化依赖关系
- 减少一个模块
- 减少跨模块依赖
- 更清晰的模块结构

### 2. 便于维护
- 相关功能集中在一个模块
- 减少模块间的协调成本
- 统一的依赖管理

### 3. 性能优化
- 减少编译时间
- 减少模块加载开销

## 注意事项

1. **Package 名称变更**
   - 所有 import 语句都需要更新
   - 确保没有遗漏的引用

2. **依赖关系**
   - `anime-detail` 现在依赖 `domain:discover`
   - `discover` 依赖 `anime-detail`（用于 `AnimeListType`）

3. **模块职责**
   - `anime-detail` 模块现在包含：
     - 动漫详情页
     - 动漫列表页
   - 两者都是展示动漫信息的页面，职责相关

## 后续优化

- [ ] 考虑重命名模块为更通用的名称（如 `anime-pages`）
- [ ] 统一 `animedetail` 和 `animelist` 的命名风格
- [ ] 考虑提取公共组件到 `core:ui`

## 相关文档

- [动漫列表功能文档](anime_list_feature.md)
- [模块规划](module_plan.md)

