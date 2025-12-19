# Kotlin Result 使用指南

本文档说明如何使用 Jikan API 模块中集成的 Kotlin Result 支持。

## 概述

所有 Jikan API 接口现在都返回 `Result<T>` 类型，提供统一的错误处理机制。

## Kotlin 标准库方法

Kotlin `Result` 类型已经提供了丰富的方法，**请优先使用这些标准方法**：

### 基础方法
- `isSuccess: Boolean` - 判断是否成功
- `isFailure: Boolean` - 判断是否失败
- `getOrNull(): T?` - 获取值或 null
- `exceptionOrNull(): Throwable?` - 获取异常或 null
- `getOrThrow(): T` - 获取值或抛出异常

### 链式操作（标准库提供）
- `onSuccess(action: (T) -> Unit): Result<T>` - 成功时执行操作
- `onFailure(action: (Throwable) -> Unit): Result<T>` - 失败时执行操作
- `map(transform: (T) -> R): Result<R>` - 转换成功值
- `mapCatching(transform: (T) -> R): Result<R>` - 转换并捕获异常
- `recover(transform: (Throwable) -> T): Result<T>` - 从失败中恢复
- `recoverCatching(transform: (Throwable) -> T): Result<T>` - 恢复并捕获异常

### 获取值（标准库提供）
- `getOrDefault(defaultValue: T): T` - 获取值或默认值
- `getOrElse(onFailure: (Throwable) -> T): T` - 获取值或执行函数
- `fold(onSuccess: (T) -> R, onFailure: (Throwable) -> R): R` - 折叠操作

### 自定义扩展方法（网络场景专用）

#### 错误处理
- `onHttpError(action: (HttpException) -> Unit): Result<T>` - HTTP 错误时执行操作
- `onNetworkError(action: (IOException) -> Unit): Result<T>` - 网络错误时执行操作

#### 链式操作
- `flatMap(transform: (T) -> Result<R>): Result<R>` - 扁平化嵌套的 Result

#### 合并多个 Result
- `zip(other, transform): Result<V>` - 合并 2 个 Result
- `combine(other, transform): Result<V>` - zip 的别名
- `zip(result2, result3, transform): Result<R>` - 合并 3 个 Result
- `List<Result<T>>.combineAll(): Result<List<T>>` - 合并列表中所有 Result（全成功或失败）
- `List<Result<T>>.collectResults(): Pair<List<T>, List<Throwable>>` - 分离成功和失败
- `List<Result<T>>.collectSuccesses(): List<T>` - 只收集成功的值
- `List<Result<T>>.collectFailures(): List<Throwable>` - 只收集失败的异常
- `List<Result<T>>.allSuccess(): Boolean` - 检查是否全部成功
- `List<Result<T>>.anyFailure(): Boolean` - 检查是否存在失败

## 基本用法

### 1. 简单调用（使用标准库方法）

```kotlin
// 获取动漫信息
val result = JikanApiClient.animeApi.getAnimeById(1)

result
    .onSuccess { response ->
        // 处理成功响应
        val anime = response.data
        println("动漫名称: ${anime?.title}")
    }
    .onFailure { exception ->
        // 处理失败
        println("错误: ${exception.message}")
    }
```

### 2. 使用 when 表达式

```kotlin
when {
    result.isSuccess -> {
        val response = result.getOrNull()
        // 处理响应
    }
    result.isFailure -> {
        val exception = result.exceptionOrNull()
        // 处理异常
    }
}
```

### 3. 区分 HTTP 错误和网络错误（自定义扩展）

```kotlin
result
    .onHttpError { httpException ->
        // 处理 HTTP 错误（4xx, 5xx）
        when (httpException.code()) {
            404 -> println("资源未找到")
            401 -> println("未授权")
            500 -> println("服务器错误")
            else -> println("HTTP 错误: ${httpException.code()}")
        }
    }
    .onNetworkError { ioException ->
        // 处理网络错误（连接超时、网络不可用等）
        println("网络错误: ${ioException.message}")
    }
```

### 4. 数据转换（使用标准库方法）

```kotlin
// 使用标准库的 map 方法
val titleResult = result.map { response ->
    response.data?.title ?: "未知"
}

// 使用 mapCatching 捕获转换过程中的异常
val scoreResult = result.mapCatching { response ->
    response.data?.score?.toInt() ?: throw IllegalStateException("无评分")
}

// 扁平化嵌套的 Result（自定义扩展）
val detailResult = result.flatMapResult { response ->
    val id = response.data?.malId
    if (id != null) {
        JikanApiClient.animeApi.getAnimeFullById(id)
    } else {
        Result.failure(IllegalStateException("无效的 ID"))
    }
}
```

### 5. 获取数据或默认值（使用标准库方法）

```kotlin
// 获取数据，失败时返回 null
val anime = result.getOrNull()?.data

// 获取数据，失败时返回默认值
val title = result
    .map { it.data?.title }
    .getOrDefault("未知动漫")

// 获取数据，失败时执行函数
val anime = result.getOrElse { exception ->
    println("获取失败: ${exception.message}")
    null // 返回默认值
}?.data

// 折叠操作：统一处理成功和失败
val message = result.fold(
    onSuccess = { response -> "成功: ${response.data?.title}" },
    onFailure = { exception -> "失败: ${exception.message}" }
)
```

### 6. 错误恢复（使用标准库方法）

```kotlin
// 从失败中恢复
val recoveredResult = result.recover { exception ->
    println("使用缓存数据: ${exception.message}")
    JikanResponse(data = getCachedAnime()) // 返回缓存数据
}

// 恢复并捕获异常
val safeResult = result.recoverCatching { exception ->
    loadFromCache() // 可能抛出异常的恢复操作
}
```

## 协程中的使用

### 1. 在 ViewModel 中使用

```kotlin
class AnimeViewModel : ViewModel() {
    
    private val _animeState = MutableStateFlow<UiState<Anime>>(UiState.Loading)
    val animeState: StateFlow<UiState<Anime>> = _animeState
    
    fun loadAnime(id: Int) {
        viewModelScope.launch {
            _animeState.value = UiState.Loading
            
            JikanApiClient.animeApi.getAnimeById(id)
                .onSuccess { response ->
                    _animeState.value = response.data?.let { 
                        UiState.Success(it) 
                    } ?: UiState.Error("数据为空")
                }
                .onHttpError { exception ->
                    _animeState.value = UiState.Error("HTTP ${exception.code()}")
                }
                .onNetworkError { exception ->
                    _animeState.value = UiState.Error("网络错误")
                }
        }
    }
}

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```

### 2. 串行请求

```kotlin
viewModelScope.launch {
    // 使用 flatMapResult 进行串行请求
    val result = JikanApiClient.animeApi.getAnimeById(1)
        .flatMapResult { animeResponse ->
            val anime = animeResponse.data
            if (anime != null) {
                // 获取角色信息
                JikanApiClient.animeApi.getAnimeCharacters(anime.malId)
            } else {
                Result.failure(IllegalStateException("动漫数据为空"))
            }
        }
    
    result.onSuccess { charactersResponse ->
        println("角色列表: ${charactersResponse.data}")
    }
}
```

### 3. 并行请求

```kotlin
viewModelScope.launch {
    // 并行请求多个 API
    val animeDeferred = async { JikanApiClient.animeApi.getAnimeById(1) }
    val mangaDeferred = async { JikanApiClient.mangaApi.getMangaById(1) }
    
    val animeResult = animeDeferred.await()
    val mangaResult = mangaDeferred.await()
    
    // 处理结果
    if (animeResult.isSuccess && mangaResult.isSuccess) {
        println("两个请求都成功")
    }
}
```

### 4. 超时处理

```kotlin
viewModelScope.launch {
    val result = withTimeoutOrNull(5000) {
        JikanApiClient.animeApi.getAnimeById(1)
    }
    
    if (result == null) {
        println("请求超时")
    } else {
        result.onSuccess { response ->
            println("成功: ${response.data?.title}")
        }
    }
}
```

## 分页数据处理

```kotlin
suspend fun loadAnimePage(page: Int): List<Anime> {
    return JikanApiClient.topApi.getTopAnime(page = page)
        .map { response -> response.data ?: emptyList() }
        .getOrDefault(emptyList())
}

// 在 ViewModel 中使用
viewModelScope.launch {
    val animeList = loadAnimePage(1)
    _animeListState.value = animeList
}
```

## 合并多个 Result

### 1. 合并两个请求 (zip/combine)

```kotlin
// 同时加载动漫和角色信息
val animeResult = async { JikanApiClient.animeApi.getAnimeById(1) }
val characterResult = async { JikanApiClient.charactersApi.getCharacterById(1) }

animeResult.await()
    .zip(characterResult.await()) { anime, character ->
        CombinedData(anime.data, character.data)
    }
    .onSuccess { data ->
        println("动漫: ${data.anime?.title}")
        println("角色: ${data.character?.name}")
    }
```

### 2. 合并三个请求

```kotlin
val anime = async { JikanApiClient.animeApi.getAnimeById(1) }
val manga = async { JikanApiClient.mangaApi.getMangaById(1) }
val character = async { JikanApiClient.charactersApi.getCharacterById(1) }

anime.await()
    .zip(manga.await(), character.await()) { a, m, c ->
        Triple(a.data, m.data, c.data)
    }
    .onSuccess { (anime, manga, character) ->
        println("全部加载成功")
    }
```

### 3. 批量请求 - 全部成功模式

```kotlin
// 加载多个动漫，只有全部成功才继续
val results = listOf(1, 2, 3).map { id ->
    async { JikanApiClient.animeApi.getAnimeById(id) }
}.awaitAll()

results.combineAll()
    .onSuccess { responses ->
        val animeList = responses.mapNotNull { it.data }
        println("成功加载 ${animeList.size} 个动漫")
    }
    .onFailure { exception ->
        println("至少一个请求失败: ${exception.message}")
    }
```

### 4. 批量请求 - 部分成功模式

```kotlin
// 加载多个动漫，分别处理成功和失败
val results = listOf(1, 2, 999).map { id ->
    async { JikanApiClient.animeApi.getAnimeById(id) }
}.awaitAll()

val (successes, failures) = results.collectResults()

println("成功: ${successes.size}, 失败: ${failures.size}")

// 处理成功的结果
val animeList = successes.mapNotNull { it.data }
_animeListState.value = animeList

// 记录失败的请求
failures.forEach { exception ->
    Log.e("API", "Failed: ${exception.message}")
}
```

### 5. 只收集成功的结果

```kotlin
// 尽可能多地加载数据，忽略失败
val results = listOf(1, 2, 3, 999).map { id ->
    async { JikanApiClient.animeApi.getAnimeById(id) }
}.awaitAll()

val successfulResponses = results.collectSuccesses()
val animeList = successfulResponses.mapNotNull { it.data }

println("成功加载 ${animeList.size} 个动漫")
_animeListState.value = animeList
```

### 6. 检查批量操作状态

```kotlin
val results = batchLoadAnime(listOf(1, 2, 3))

when {
    results.allSuccess() -> {
        showMessage("✅ 全部成功")
    }
    results.anyFailure() -> {
        val failureCount = results.collectFailures().size
        showMessage("⚠️ ${failureCount} 个请求失败")
    }
}
```

更多合并 Result 的使用示例，请参考 `RESULT_COMBINE_EXAMPLES.md`。

## 错误处理最佳实践

### 1. 统一错误处理

```kotlin
fun <T> Result<T>.handleCommonErrors(): Result<T> {
    return this
        .onHttpError { exception ->
            when (exception.code()) {
                401 -> showLoginDialog()
                403 -> showPermissionDeniedDialog()
                429 -> showRateLimitDialog()
                500, 502, 503 -> showServerErrorDialog()
            }
        }
        .onNetworkError { exception ->
            showNetworkErrorDialog()
        }
}

// 使用
JikanApiClient.animeApi.getAnimeById(1)
    .handleCommonErrors()
    .onSuccess { response ->
        // 处理成功
    }
```

### 2. 日志记录

```kotlin
inline fun <T> Result<T>.logError(tag: String): Result<T> {
    return this.onFailure { exception ->
        Log.e(tag, "API Error", exception)
    }
}

// 使用
JikanApiClient.animeApi.getAnimeById(1)
    .logError("AnimeViewModel")
    .onSuccess { response ->
        // 处理成功
    }
```

### 3. 重试机制

```kotlin
suspend fun <T> retryWithExponentialBackoff(
    times: Int = 3,
    initialDelay: Long = 1000,
    maxDelay: Long = 10000,
    factor: Double = 2.0,
    block: suspend () -> Result<T>
): Result<T> {
    var currentDelay = initialDelay
    repeat(times - 1) {
        val result = block()
        if (result.isSuccess) return result
        
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return block() // 最后一次尝试
}

// 使用
val result = retryWithExponentialBackoff {
    JikanApiClient.animeApi.getAnimeById(1)
}
```

## 测试

### 单元测试示例

```kotlin
@Test
fun `test anime loading success`() = runTest {
    // Mock API 返回成功
    val mockResponse = JikanResponse(data = Anime(...))
    coEvery { animeApi.getAnimeById(1) } returns Result.success(mockResponse)
    
    // 执行
    viewModel.loadAnime(1)
    
    // 验证
    val state = viewModel.animeState.value
    assertTrue(state is UiState.Success)
    assertEquals("Expected Title", (state as UiState.Success).data.title)
}

@Test
fun `test anime loading failure`() = runTest {
    // Mock API 返回失败
    coEvery { animeApi.getAnimeById(1) } returns 
        Result.failure(IOException("Network error"))
    
    // 执行
    viewModel.loadAnime(1)
    
    // 验证
    val state = viewModel.animeState.value
    assertTrue(state is UiState.Error)
}
```

## 注意事项

1. **优先使用标准库方法**：Kotlin Result 已经提供了 `onSuccess`、`onFailure`、`map`、`getOrDefault` 等方法，无需自定义
2. **自定义扩展仅用于特定场景**：`onHttpError`、`onNetworkError`、`flatMapResult` 是针对网络请求场景的补充
3. **避免嵌套 Result**：使用 `flatMapResult` 而不是 `map` 来处理返回 Result 的操作
4. **空安全**：`response.data` 可能为 null，请注意空安全处理
5. **协程作用域**：所有 API 方法都是 suspend 函数，需在协程作用域中调用
6. **错误日志**：建议在 `onFailure` 中记录错误日志，便于调试
7. **用户体验**：对不同类型的错误提供友好的提示信息

## 相关文件

- `network/ResultCallAdapterFactory.kt` - Retrofit Result 适配器
- `network/ResultCall.kt` - Result Call 实现
- `network/ResultExtensions.kt` - Result 扩展方法（仅包含自定义扩展）
- `JikanApiClient.kt` - API 客户端配置
