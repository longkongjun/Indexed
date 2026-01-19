package com.pusu.indexed.data.jikan.mapper

import com.pusu.indexed.domain.anime.model.AnimeItem
import com.pusu.indexed.jikan.models.anime.Anime

/**
 * 将 Jikan API 的数据模型转换为 Domain 层的模型
 * 
 * 职责：
 * 1. 数据转换 - 从复杂的 API 模型提取核心信息
 * 2. 空值处理 - 为 UI 层提供安全的数据
 * 3. 格式标准化 - 统一数据格式
 */
class JikanToDiscoverMapper {
    
    /**
     * 将 Jikan 的 Anime 模型转换为 AnimeItem
     * 
     * @param jikanAnime Jikan API 返回的动漫数据
     * @return 简化的动漫项
     */
    fun mapToAnimeItem(jikanAnime: Anime): AnimeItem {
        return AnimeItem(
            id = jikanAnime.malId,
            title = jikanAnime.title,
            titleEnglish = jikanAnime.titleEnglish,
            titleJapanese = jikanAnime.titleJapanese,
            imageUrl = extractImageUrl(jikanAnime),
            score = jikanAnime.score,
            rank = jikanAnime.rank,
            type = jikanAnime.type,
            status = jikanAnime.status,
            episodes = jikanAnime.episodes,
            year = jikanAnime.year,
            season = jikanAnime.season,
            genres = extractGenres(jikanAnime)
        )
    }
    
    /**
     * 批量转换动漫列表
     */
    fun mapToAnimeItemList(jikanAnimeList: List<Anime>): List<AnimeItem> {
        return jikanAnimeList.map { mapToAnimeItem(it) }
    }
    
    /**
     * 提取图片 URL
     * 优先使用高质量图片，如果不可用则使用默认图片
     */
    private fun extractImageUrl(anime: Anime): String {
        return anime.images.jpg.largeImageUrl 
            ?: anime.images.jpg.imageUrl 
            ?: ""
    }
    
    /**
     * 提取类型标签列表
     * 将 Jikan 的 MalUrl 对象列表转换为简单的字符串列表
     */
    private fun extractGenres(anime: Anime): List<String> {
        return anime.genres?.mapNotNull { it.name } ?: emptyList()
    }
}

