package com.pusu.indexed.domain.discover.usecase

import com.pusu.indexed.domain.discover.model.AnimeItem
import com.pusu.indexed.domain.discover.repository.DiscoverRepository

/**
 * 获取随机动漫用例
 * 
 * 从数据库中随机获取一部动漫，用于推荐功能。
 * 
 * 使用示例：
 * ```kotlin
 * val useCase = GetRandomAnimeUseCase(repository)
 * 
 * val result = useCase()
 * result.onSuccess { anime ->
 *     // 显示随机推荐
 * }.onFailure { error ->
 *     // 处理错误
 * }
 * ```
 * 
 * @property repository Discover 仓库接口
 */
class GetRandomAnimeUseCase(
    private val repository: DiscoverRepository
) {
    /**
     * 执行用例
     * 
     * @return Result<AnimeItem> 成功返回随机动漫，失败返回异常
     */
    suspend operator fun invoke(): Result<AnimeItem> {
        return repository.getRandomAnime()
    }
}

