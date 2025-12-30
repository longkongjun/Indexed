package com.pusu.indexed.domain.discover.usecase

import com.pusu.indexed.domain.discover.model.AnimeItem
import com.pusu.indexed.domain.discover.repository.DiscoverRepository

/**
 * 搜索动漫用例
 * 
 * 根据关键词搜索动漫列表。
 * 这是一个纯业务逻辑类，不包含任何 UI 或框架相关代码。
 * 
 * ✅ 完全隔离：只依赖 Repository 接口，不依赖任何具体实现
 * 
 * 使用示例：
 * ```kotlin
 * val useCase = SearchAnimeUseCase(repository)
 * 
 * // 搜索关键词为 "Naruto" 的动漫
 * val result = useCase(query = "Naruto", page = 1, limit = 20)
 * result.onSuccess { animeList ->
 *     // 处理成功结果
 * }.onFailure { error ->
 *     // 处理错误
 * }
 * ```
 * 
 * @property repository Discover 仓库接口（抽象依赖）
 */
class SearchAnimeUseCase(
    private val repository: DiscoverRepository  // ✅ 依赖抽象接口，不依赖具体实现
) {
    /**
     * 执行用例
     * 
     * @param query 搜索关键词
     * @param page 页码，从 1 开始
     * @param limit 每页数量，默认 20 条
     * @return Result<List<AnimeItem>> 成功返回动漫列表，失败返回异常
     */
    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        limit: Int = 20
    ): Result<List<AnimeItem>> {
        // 直接调用 Repository 接口方法
        // Repository 负责数据获取和转换
        return repository.searchAnime(query, page, limit)
    }
    
    /**
     * 搜索动漫（使用默认参数）
     * 便捷方法，返回第一页的搜索结果
     * 
     * @param query 搜索关键词
     * @return Result<List<AnimeItem>>
     */
    suspend fun search(query: String): Result<List<AnimeItem>> {
        return invoke(query = query, page = 1, limit = 20)
    }
}

