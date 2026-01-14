package com.pusu.indexed.domain.anime.usecase

import com.pusu.indexed.domain.anime.model.AnimeItem
import com.pusu.indexed.domain.anime.repository.AnimeRepository

/**
 * 筛选动漫用例
 * 
 * 根据类型、年份、类型标签、状态等条件筛选动漫列表。
 * 这是一个纯业务逻辑类，不包含任何 UI 或框架相关代码。
 * 
 * ✅ 完全隔离：只依赖 Repository 接口，不依赖任何具体实现
 * 
 * 使用示例：
 * ```kotlin
 * val useCase = FilterAnimeUseCase(repository)
 * 
 * // 筛选类型为 TV 且年份为 2024 的动漫
 * val result = useCase(
 *     types = setOf("TV"),
 *     years = setOf(2024),
 *     genres = emptySet(),
 *     statuses = emptySet(),
 *     page = 1,
 *     limit = 20
 * )
 * result.onSuccess { animeList ->
 *     // 处理成功结果
 * }.onFailure { error ->
 *     // 处理错误
 * }
 * ```
 * 
 * @property repository Anime 仓库接口（抽象依赖）
 */
class FilterAnimeUseCase(
    private val repository: AnimeRepository  // ✅ 依赖抽象接口，不依赖具体实现
) {
    /**
     * 执行用例
     * 
     * @param types 类型筛选条件（TV, Movie, OVA等）
     * @param years 年份筛选条件
     * @param genres 类型标签筛选条件（Action, Adventure等）
     * @param statuses 状态筛选条件（Airing, Finished等）
     * @param page 页码，从 1 开始
     * @param limit 每页数量，默认 20 条
     * @return Result<List<AnimeItem>> 成功返回动漫列表，失败返回异常
     */
    suspend operator fun invoke(
        types: Set<String> = emptySet(),
        years: Set<Int> = emptySet(),
        genres: Set<String> = emptySet(),
        statuses: Set<String> = emptySet(),
        page: Int = 1,
        limit: Int = 20
    ): Result<List<AnimeItem>> {
        // 如果没有任何筛选条件，返回当前季度动漫
        if (types.isEmpty() && years.isEmpty() && genres.isEmpty() && statuses.isEmpty()) {
            return repository.getCurrentSeasonAnime(page = page, limit = limit)
        }
        
        // 获取更大范围的数据进行筛选
        // 由于我们需要在本地筛选，所以获取更多数据以确保有足够的结果
        val fetchLimit = limit * 5 // 获取更多数据以便筛选
        
        return try {
            // 获取当前季度动漫作为基础数据
            val result = repository.getCurrentSeasonAnime(page = 1, limit = fetchLimit)
            
            result.mapCatching { animeList ->
                // 应用筛选条件
                var filteredList = animeList
                
                // 按类型筛选
                if (types.isNotEmpty()) {
                    filteredList = filteredList.filter { anime ->
                        anime.type?.let { types.contains(it) } ?: false
                    }
                }
                
                // 按年份筛选
                if (years.isNotEmpty()) {
                    filteredList = filteredList.filter { anime ->
                        anime.year?.let { years.contains(it) } ?: false
                    }
                }
                
                // 按类型标签筛选
                if (genres.isNotEmpty()) {
                    filteredList = filteredList.filter { anime ->
                        // 如果动漫的genres包含任意一个选中的genre，就保留
                        anime.genres.any { genre -> genres.contains(genre) }
                    }
                }
                
                // 按状态筛选
                if (statuses.isNotEmpty()) {
                    filteredList = filteredList.filter { anime ->
                        anime.status?.let { statuses.contains(it) } ?: false
                    }
                }
                
                // 分页处理
                val startIndex = (page - 1) * limit
                val endIndex = minOf(startIndex + limit, filteredList.size)
                
                if (startIndex < filteredList.size) {
                    filteredList.subList(startIndex, endIndex)
                } else {
                    emptyList()
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 筛选动漫（使用默认参数）
     * 便捷方法，返回第一页的筛选结果
     * 
     * @param types 类型筛选条件
     * @param years 年份筛选条件
     * @param genres 类型标签筛选条件
     * @param statuses 状态筛选条件
     * @return Result<List<AnimeItem>>
     */
    suspend fun filter(
        types: Set<String> = emptySet(),
        years: Set<Int> = emptySet(),
        genres: Set<String> = emptySet(),
        statuses: Set<String> = emptySet()
    ): Result<List<AnimeItem>> {
        return invoke(
            types = types,
            years = years,
            genres = genres,
            statuses = statuses,
            page = 1,
            limit = 20
        )
    }
}
