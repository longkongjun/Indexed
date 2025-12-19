# Result 合并扩展方法使用示例

本文档展示如何使用 `ResultExtensions.kt` 中提供的 Result 合并方法。

## 方法概览

| 方法 | 用途 | 返回类型 |
|------|------|---------|
| `zip` / `combine` | 合并 2 个 Result | `Result<V>` |
| `zip` (3 参数) | 合并 3 个 Result | `Result<R>` |
| `combineAll` | 合并列表中所有 Result（全成功或第一个失败） | `Result<List<T>>` |
| `collectResults` | 分离成功和失败的 Result | `Pair<List<T>, List<Throwable>>` |
| `collectSuccesses` | 只收集成功的值 | `List<T>` |
| `collectFailures` | 只收集失败的异常 | `List<Throwable>` |
| `allSuccess` | 检查是否全部成功 | `Boolean` |
| `anyFailure` | 检查是否存在失败 | `Boolean` |

## 使用场景

### 1. 合并两个 API 请求 (zip/combine)

**场景**：同时获取动漫和漫画信息，组合成一个对象

```kotlin
suspend fun loadAnimeAndManga(animeId: Int, mangaId: Int) {
    viewModelScope.launch {
        val animeResult = JikanApiClient.animeApi.getAnimeById(animeId)
        val mangaResult = JikanApiClient.mangaApi.getMangaById(mangaId)
        
        // 方式 1: 使用 zip
        animeResult.zip(mangaResult) { animeResponse, mangaResponse ->
            CombinedContent(
                anime = animeResponse.data,
                manga = mangaResponse.data
            )
        }.onSuccess { combined ->
            println("动漫: ${combined.anime?.title}")
            println("漫画: ${combined.manga?.title}")
        }.onFailure { exception ->
            println("加载失败: ${exception.message}")
        }
        
        // 方式 2: 使用 combine (zip 的别名)
        animeResult.combine(mangaResult) { anime, manga ->
            Pair(anime, manga)
        }
    }
}

data class CombinedContent(
    val anime: Anime?,
    val manga: Manga?
)
```

### 2. 合并三个 API 请求

**场景**：同时获取动漫、角色和制作公司信息

```kotlin
suspend fun loadCompleteAnimeInfo(animeId: Int, characterId: Int, producerId: Int) {
    val animeResult = JikanApiClient.animeApi.getAnimeById(animeId)
    val characterResult = JikanApiClient.charactersApi.getCharacterById(characterId)
    val producerResult = JikanApiClient.producersApi.getProducerById(producerId)
    
    animeResult.zip(characterResult, producerResult) { anime, character, producer ->
        CompleteInfo(
            anime = anime.data,
            character = character.data,
            producer = producer.data
        )
    }.onSuccess { info ->
        println("完整信息加载成功")
    }.onFailure { exception ->
        println("加载失败: ${exception.message}")
    }
}

data class CompleteInfo(
    val anime: Anime?,
    val character: Character?,
    val producer: Producer?
)
```

### 3. 批量加载并全部成功 (combineAll)

**场景**：加载多个动漫，只有全部成功才继续

```kotlin
suspend fun loadMultipleAnime(ids: List<Int>) {
    viewModelScope.launch {
        // 并行请求多个动漫
        val results = ids.map { id ->
            async { JikanApiClient.animeApi.getAnimeById(id) }
        }.awaitAll()
        
        // 合并所有结果，只有全部成功才返回成功
        results.combineAll()
            .onSuccess { responses ->
                val animeList = responses.mapNotNull { it.data }
                println("成功加载 ${animeList.size} 个动漫")
                _animeListState.value = animeList
            }
            .onFailure { exception ->
                // 任一请求失败，显示错误
                println("加载失败: ${exception.message}")
                _errorState.value = "部分动漫加载失败"
            }
    }
}
```

### 4. 批量加载并分离成功/失败 (collectResults)

**场景**：加载多个动漫，分别处理成功和失败的情况

```kotlin
suspend fun loadMultipleAnimeWithPartialSuccess(ids: List<Int>) {
    viewModelScope.launch {
        val results = ids.map { id ->
            async { JikanApiClient.animeApi.getAnimeById(id) }
        }.awaitAll()
        
        // 分离成功和失败的结果
        val (successes, failures) = results.collectResults()
        
        // 处理成功的结果
        val animeList = successes.mapNotNull { it.data }
        if (animeList.isNotEmpty()) {
            println("成功加载 ${animeList.size} 个动漫")
            _animeListState.value = animeList
        }
        
        // 处理失败的结果
        if (failures.isNotEmpty()) {
            println("${failures.size} 个请求失败")
            failures.forEach { exception ->
                when (exception) {
                    is HttpException -> println("HTTP ${exception.code}: ${exception.message}")
                    else -> println("错误: ${exception.message}")
                }
            }
        }
    }
}
```

### 5. 只收集成功的结果 (collectSuccesses)

**场景**：尽可能多地加载数据，忽略失败的请求

```kotlin
suspend fun loadAvailableAnime(ids: List<Int>) {
    viewModelScope.launch {
        val results = ids.map { id ->
            async { JikanApiClient.animeApi.getAnimeById(id) }
        }.awaitAll()
        
        // 只收集成功的结果，忽略失败
        val successfulResponses = results.collectSuccesses()
        val animeList = successfulResponses.mapNotNull { it.data }
        
        println("成功加载 ${animeList.size}/${ids.size} 个动漫")
        _animeListState.value = animeList
    }
}
```

### 6. 检查批量操作状态

**场景**：检查批量操作是否全部成功

```kotlin
suspend fun checkBatchOperationStatus(ids: List<Int>) {
    val results = ids.map { id ->
        async { JikanApiClient.animeApi.getAnimeById(id) }
    }.awaitAll()
    
    when {
        results.allSuccess() -> {
            println("✅ 所有请求都成功")
            showSuccessMessage()
        }
        results.anyFailure() -> {
            val failureCount = results.collectFailures().size
            println("⚠️ ${failureCount} 个请求失败")
            showPartialSuccessMessage()
        }
    }
}
```

## 实际应用示例

### 示例 1: 动漫详情页面

加载动漫详情、角色列表和推荐列表

```kotlin
class AnimeDetailViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow<AnimeDetailUiState>(AnimeDetailUiState.Loading)
    val uiState: StateFlow<AnimeDetailUiState> = _uiState
    
    fun loadAnimeDetail(animeId: Int) {
        viewModelScope.launch {
            _uiState.value = AnimeDetailUiState.Loading
            
            // 并行请求三个接口
            val animeResult = async { 
                JikanApiClient.animeApi.getAnimeById(animeId) 
            }
            val charactersResult = async { 
                JikanApiClient.animeApi.getAnimeCharacters(animeId) 
            }
            val recommendationsResult = async { 
                JikanApiClient.animeApi.getAnimeRecommendations(animeId) 
            }
            
            // 合并结果
            animeResult.await()
                .zip(charactersResult.await(), recommendationsResult.await()) { anime, characters, recommendations ->
                    AnimeDetailData(
                        anime = anime.data,
                        characters = characters.data,
                        recommendations = recommendations.data
                    )
                }
                .onSuccess { data ->
                    _uiState.value = AnimeDetailUiState.Success(data)
                }
                .onHttpError { exception ->
                    _uiState.value = AnimeDetailUiState.Error("HTTP ${exception.code}")
                }
                .onNetworkError { exception ->
                    _uiState.value = AnimeDetailUiState.Error("网络错误")
                }
        }
    }
}

sealed class AnimeDetailUiState {
    object Loading : AnimeDetailUiState()
    data class Success(val data: AnimeDetailData) : AnimeDetailUiState()
    data class Error(val message: String) : AnimeDetailUiState()
}

data class AnimeDetailData(
    val anime: Anime?,
    val characters: List<AnimeCharacter>?,
    val recommendations: List<Recommendation>?
)
```

### 示例 2: 搜索结果页面

同时搜索动漫、漫画和角色

```kotlin
class SearchViewModel : ViewModel() {
    
    fun search(query: String) {
        viewModelScope.launch {
            val animeResults = async { 
                JikanApiClient.animeApi.searchAnime(q = query) 
            }
            val mangaResults = async { 
                JikanApiClient.mangaApi.searchManga(q = query) 
            }
            val characterResults = async { 
                JikanApiClient.charactersApi.searchCharacters(q = query) 
            }
            
            // 等待所有结果
            val results = listOf(
                animeResults.await(),
                mangaResults.await(),
                characterResults.await()
            )
            
            // 分离成功和失败
            val (successes, failures) = results.collectResults()
            
            if (successes.isNotEmpty()) {
                // 至少有一些结果成功
                _searchResultsState.value = SearchResults(
                    anime = (successes.getOrNull(0) as? JikanPageResponse<Anime>)?.data,
                    manga = (successes.getOrNull(1) as? JikanPageResponse<Manga>)?.data,
                    characters = (successes.getOrNull(2) as? JikanPageResponse<Character>)?.data
                )
            }
            
            if (failures.isNotEmpty()) {
                // 显示部分失败提示
                _warningState.value = "部分搜索结果加载失败"
            }
        }
    }
}
```

### 示例 3: 推荐系统

基于用户喜好加载多个推荐动漫

```kotlin
class RecommendationViewModel : ViewModel() {
    
    fun loadRecommendations(userFavoriteIds: List<Int>) {
        viewModelScope.launch {
            // 为每个喜欢的动漫获取推荐
            val recommendationResults = userFavoriteIds.map { id ->
                async { JikanApiClient.animeApi.getAnimeRecommendations(id) }
            }.awaitAll()
            
            // 只收集成功的推荐
            val allRecommendations = recommendationResults
                .collectSuccesses()
                .flatMap { it.data ?: emptyList() }
                .distinctBy { it.entry?.malId } // 去重
                .take(20) // 限制数量
            
            _recommendationsState.value = allRecommendations
            
            // 统计信息
            val successCount = recommendationResults.count { it.isSuccess }
            val totalCount = recommendationResults.size
            println("成功加载 $successCount/$totalCount 个推荐源")
        }
    }
}
```

### 示例 4: 批量操作进度追踪

```kotlin
class BatchOperationViewModel : ViewModel() {
    
    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress
    
    suspend fun batchLoadAnime(ids: List<Int>) {
        val results = mutableListOf<Result<JikanResponse<Anime>>>()
        
        ids.forEachIndexed { index, id ->
            val result = JikanApiClient.animeApi.getAnimeById(id)
            results.add(result)
            
            // 更新进度
            _progress.value = (index + 1).toFloat() / ids.size
        }
        
        // 检查结果
        when {
            results.allSuccess() -> {
                showMessage("✅ 全部加载成功")
            }
            results.anyFailure() -> {
                val (successes, failures) = results.collectResults()
                showMessage("⚠️ 成功: ${successes.size}, 失败: ${failures.size}")
            }
        }
    }
}
```

## 性能建议

### 1. 并行 vs 串行

```kotlin
// ❌ 串行执行（慢）
val result1 = api.getAnime(1)
val result2 = api.getAnime(2)
val combined = result1.zip(result2) { a, b -> Pair(a, b) }

// ✅ 并行执行（快）
val result1 = async { api.getAnime(1) }
val result2 = async { api.getAnime(2) }
val combined = result1.await().zip(result2.await()) { a, b -> Pair(a, b) }
```

### 2. 大量请求的批处理

```kotlin
// 分批处理，避免同时发起过多请求
suspend fun loadManyAnime(ids: List<Int>) {
    ids.chunked(10).forEach { batch ->
        val results = batch.map { id ->
            async { JikanApiClient.animeApi.getAnimeById(id) }
        }.awaitAll()
        
        val animeList = results.collectSuccesses().mapNotNull { it.data }
        // 处理这一批结果
        processBatch(animeList)
        
        // 避免触发 API 限流
        delay(1000)
    }
}
```

### 3. 超时控制

```kotlin
suspend fun loadWithTimeout(ids: List<Int>) {
    withTimeout(10000) { // 10 秒超时
        val results = ids.map { id ->
            async { JikanApiClient.animeApi.getAnimeById(id) }
        }.awaitAll()
        
        results.combineAll()
    }
}
```

## 错误处理模式

### 模式 1: 全有或全无

```kotlin
// 要么全部成功，要么失败
results.combineAll()
    .onSuccess { /* 全部成功 */ }
    .onFailure { /* 至少一个失败 */ }
```

### 模式 2: 尽力而为

```kotlin
// 收集所有成功的，忽略失败
val successes = results.collectSuccesses()
if (successes.isNotEmpty()) {
    // 至少有一些成功
}
```

### 模式 3: 详细报告

```kotlin
// 分别处理成功和失败
val (successes, failures) = results.collectResults()
println("成功: ${successes.size}, 失败: ${failures.size}")
failures.forEach { exception ->
    logError(exception)
}
```

## 总结

Result 合并扩展方法提供了灵活的方式来处理多个异步操作：

- **`zip`/`combine`**: 适合固定数量的请求（2-3个）
- **`combineAll`**: 适合需要全部成功的批量操作
- **`collectResults`**: 适合需要详细了解成功/失败情况
- **`collectSuccesses`**: 适合尽可能多地获取数据
- **`allSuccess`/`anyFailure`**: 适合快速检查批量操作状态

选择合适的方法可以让代码更简洁、更易维护！

