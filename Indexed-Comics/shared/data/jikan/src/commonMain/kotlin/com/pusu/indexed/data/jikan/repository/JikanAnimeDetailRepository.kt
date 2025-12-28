package com.pusu.indexed.data.jikan.repository

import com.pusu.indexed.domain.discover.model.AnimeItem
import com.pusu.indexed.data.jikan.mapper.JikanToAnimeDetailMapper
import com.pusu.indexed.domain.feed.usecase.GetAnimeDetailUseCase
import com.pusu.indexed.domain.feed.usecase.GetAnimeRecommendationsUseCase
import com.pusu.indexed.domain.feed.usecase.GetRelatedAnimeUseCase
import com.pusu.indexed.domain.feed.model.AnimeDetailData
import com.pusu.indexed.jikan.JikanApi

/**
 * 动漫详情仓库实现
 */
class JikanAnimeDetailRepository(
    private val jikanApi: JikanApi,
    private val mapper: JikanToAnimeDetailMapper
) : GetAnimeDetailUseCase {
    
    override suspend fun invoke(animeId: Int): Result<AnimeDetailData> {
        return try {
            val result = jikanApi.anime.getAnimeFullById(animeId)
            result.fold(
                onSuccess = { response ->
                    response.data?.let { anime ->
                        val animeDetailData = mapper.mapToAnimeDetailData(anime)
                        Result.success(animeDetailData)
                    } ?: Result.failure(Exception("Anime data is null"))
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * 相关动漫仓库实现
 */
class JikanRelatedAnimeRepository(
    private val jikanApi: JikanApi,
    private val mapper: JikanToAnimeDetailMapper
) : GetRelatedAnimeUseCase {
    
    override suspend fun invoke(animeId: Int): Result<List<AnimeItem>> {
        // 暂时返回空列表，因为 Jikan API 的关系接口结构复杂
        return Result.success(emptyList())
    }
}

/**
 * 推荐动漫仓库实现
 */
class JikanAnimeRecommendationsRepository(
    private val jikanApi: JikanApi,
    private val mapper: JikanToAnimeDetailMapper
) : GetAnimeRecommendationsUseCase {
    
    override suspend fun invoke(animeId: Int): Result<List<AnimeItem>> {
        return try {
            val result = jikanApi.anime.getAnimeRecommendations(animeId)
            result.fold(
                onSuccess = { response ->
                    val recommendations = response.data?.mapNotNull { recommendation ->
                        recommendation.entry?.let { entry ->
                            AnimeItem(
                                id = entry.malId,
                                title = entry.name,
                                imageUrl = "", // Recommendation API 不返回图片
                                score = null,
                                type = entry.type ?: "",
                                episodes = null,
                                year = null
                            )
                        }
                    }?.take(10) ?: emptyList() // 限制数量
                    Result.success(recommendations)
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

