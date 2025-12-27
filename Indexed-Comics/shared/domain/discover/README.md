# Domain Discover 模块

## 📍 模块定位

**领域层（Domain Layer）** - 包含 Discover 功能的所有业务逻辑。

- ✅ 纯 Kotlin 代码，不依赖任何 UI 框架
- ✅ 跨平台共享业务逻辑
- ✅ 可独立测试

---

## 📁 目录结构

```
domain/discover/
├── build.gradle.kts
├── README.md
└── src/
    ├── commonMain/kotlin/com/pusu/indexed/domain/discover/
    │   ├── model/              # 领域模型
    │   ├── usecase/            # 用例（业务逻辑）
    │   └── mapper/             # 数据映射器
    ├── commonTest/kotlin/      # 单元测试
    └── androidMain/
        └── AndroidManifest.xml
```

---

## 🎯 职责

### 1. 定义领域模型
- `AnimeItem` - 简化的动漫模型
- `MangaItem` - 简化的漫画模型
- `Genre` - 类型/题材
- `Season` - 季度信息
- 等等...

### 2. 实现业务逻辑（UseCase）
- `GetTrendingAnimeUseCase` - 获取热门动漫
- `GetCurrentSeasonAnimeUseCase` - 获取本季新番
- `GetTopRankedAnimeUseCase` - 获取排行榜
- `GetRandomAnimeUseCase` - 获取随机推荐
- 等等...

### 3. 数据转换
- 将 Jikan API 的复杂模型转换为简化的领域模型
- 处理空值和异常情况
- 数据格式标准化

---

## 🔗 依赖关系

```
domain:discover
  ↓ 依赖
data:jikan      # Jikan API 数据源
core:model      # 共享基础模型
```

**被依赖于**:
```
feature:discover  # UI 层
```

---

## 📝 使用示例

```kotlin
// 在 ViewModel 中使用 UseCase
class DiscoverViewModel(
    private val getTrendingAnimeUseCase: GetTrendingAnimeUseCase
) : ViewModel() {
    
    fun loadTrendingAnime() {
        viewModelScope.launch {
            getTrendingAnimeUseCase(page = 1, limit = 10)
                .onSuccess { animeList ->
                    // 更新 UI 状态
                    _uiState.update { it.copy(trendingAnime = animeList) }
                }
                .onFailure { error ->
                    // 处理错误
                    _uiState.update { it.copy(error = error.message) }
                }
        }
    }
}
```

---

## ✅ 待实现清单

### Phase 1: 基础模型 ⏳
- [ ] `model/AnimeItem.kt`
- [ ] `model/MangaItem.kt`
- [ ] `model/Genre.kt`
- [ ] `model/ContentType.kt`
- [ ] `model/RankingType.kt`

### Phase 2: 核心 UseCase ⏳
- [ ] `usecase/GetTrendingAnimeUseCase.kt`
- [ ] `usecase/GetCurrentSeasonAnimeUseCase.kt`
- [ ] `usecase/GetTopRankedAnimeUseCase.kt`
- [ ] `usecase/GetRandomAnimeUseCase.kt`
- [ ] `usecase/GetAnimeGenresUseCase.kt`

### Phase 3: 映射器 ⏳
- [ ] `mapper/JikanToDiscoverMapper.kt`

### Phase 4: 测试 ⏳
- [ ] 为每个 UseCase 编写单元测试

---

## 📚 相关文档

- [完整规划文档](../../../../docs/domain_discover_plan.md)
- [Feature Discover 架构](../../feature/discover/ARCHITECTURE.md)

---

**创建日期**: 2025-12-25  
**状态**: 🚧 待实现

