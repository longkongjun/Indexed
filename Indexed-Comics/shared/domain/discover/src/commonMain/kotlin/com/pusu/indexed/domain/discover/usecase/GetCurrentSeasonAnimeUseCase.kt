package com.pusu.indexed.domain.discover.usecase

import com.pusu.indexed.domain.discover.model.AnimeItem
import com.pusu.indexed.domain.discover.repository.DiscoverRepository

/**
 * 获取本季新番用例
 * 
 * 获取当前季度正在播放的动漫列表。
 * 
 * 使用示例：
 * ```kotlin
 * val useCase = GetCurrentSeasonAnimeUseCase(repository)
 * 
 * val result = useCase(page = 1, limit = 25)
 * result.onSuccess { animeList ->
 *     // 处理成功结果
 * }.onFailure { error ->
 *     // 处理错误
 * }
 * ```
 * 
 * @property repository Discover 仓库接口
 */
class GetCurrentSeasonAnimeUseCase(
    private val repository: DiscoverRepository
) {
    /**
     * 执行用例
     * 
     * @param page 页码，从 1 开始
     * @param limit 每页数量，默认 25 条
     * @return Result<List<AnimeItem>> 成功返回动漫列表，失败返回异常
     */
    suspend operator fun invoke(
        page: Int = 1,
        limit: Int = 25
    ): Result<List<AnimeItem>> {
        return repository.getCurrentSeasonAnime(page, limit)
    }
}

