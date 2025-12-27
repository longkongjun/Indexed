package com.pusu.indexed.domain.discover.usecase

import com.pusu.indexed.domain.discover.model.AnimeItem
import com.pusu.indexed.domain.discover.repository.DiscoverRepository

/**
 * 获取热门动漫用例
 * 
 * 按人气排序获取热门动漫列表。
 * 这是一个纯业务逻辑类，不包含任何 UI 或框架相关代码。
 * 
 * ✅ 完全隔离：只依赖 Repository 接口，不依赖任何具体实现
 * 
 * 使用示例：
 * ```kotlin
 * val useCase = GetTrendingAnimeUseCase(repository)
 * 
 * // 获取前10部热门动漫
 * val result = useCase(page = 1, limit = 10)
 * result.onSuccess { animeList ->
 *     // 处理成功结果
 * }.onFailure { error ->
 *     // 处理错误
 * }
 * ```
 * 
 * @property repository Discover 仓库接口（抽象依赖）
 */
class GetTrendingAnimeUseCase(
    private val repository: DiscoverRepository  // ✅ 依赖抽象接口，不依赖具体实现
) {
    /**
     * 执行用例
     * 
     * @param page 页码，从 1 开始
     * @param limit 每页数量，默认 10 条
     * @return Result<List<AnimeItem>> 成功返回动漫列表，失败返回异常
     */
    suspend operator fun invoke(
        page: Int = 1,
        limit: Int = 10
    ): Result<List<AnimeItem>> {
        // 直接调用 Repository 接口方法
        // Repository 负责数据获取和转换
        return repository.getTrendingAnime(page, limit)
    }
    
    /**
     * 获取热门动漫（使用默认参数）
     * 便捷方法，返回前 10 部热门动漫
     */
    suspend fun getTrending(): Result<List<AnimeItem>> {
        return invoke(page = 1, limit = 10)
    }
}

