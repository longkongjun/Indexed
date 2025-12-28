package com.pusu.indexed.data.jikan.mapper

import com.pusu.indexed.domain.discover.model.AnimeItem
import com.pusu.indexed.jikan.models.anime.Anime
import com.pusu.indexed.domain.feed.model.AnimeDetailData

/**
 * Jikan API Anime 模型到 AnimeDetailData 的映射器
 */
class JikanToAnimeDetailMapper {
    
    fun mapToAnimeDetailData(anime: Anime): AnimeDetailData {
        return AnimeDetailData(
            id = anime.malId,
            title = anime.title,
            titleEnglish = anime.titleEnglish,
            titleJapanese = anime.titleJapanese,
            imageUrl = anime.images.jpg?.largeImageUrl 
                ?: anime.images.jpg?.imageUrl 
                ?: "",
            trailerUrl = anime.trailer?.url,
            score = anime.score,
            scoredBy = anime.scoredBy,
            rank = anime.rank,
            popularity = anime.popularity,
            members = anime.members,
            favorites = anime.favorites,
            synopsis = anime.synopsis,
            background = anime.background,
            type = anime.type,
            episodes = anime.episodes,
            status = anime.status,
            airing = anime.airing ?: false,
            aired = formatAiredDate(anime.aired?.string),
            season = anime.season,
            year = anime.year,
            source = anime.source,
            duration = anime.duration,
            rating = anime.rating,
            studios = anime.studios?.map { it.name } ?: emptyList(),
            genres = anime.genres?.map { it.name } ?: emptyList(),
            themes = anime.themes?.map { it.name } ?: emptyList(),
            demographics = anime.demographics?.map { it.name } ?: emptyList(),
            producers = anime.producers?.map { it.name } ?: emptyList(),
            licensors = anime.licensors?.map { it.name } ?: emptyList()
        )
    }
    
    fun mapToAnimeItem(anime: Anime): AnimeItem {
        return AnimeItem(
            id = anime.malId,
            title = anime.title,
            imageUrl = anime.images.jpg?.imageUrl ?: "",
            score = anime.score,
            type = anime.type,
            episodes = anime.episodes,
            year = anime.year
        )
    }
    
    private fun formatAiredDate(airedString: String?): String? {
        return airedString
    }
}

