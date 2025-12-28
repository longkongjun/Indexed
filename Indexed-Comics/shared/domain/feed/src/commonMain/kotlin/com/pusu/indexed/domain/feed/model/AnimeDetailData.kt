package com.pusu.indexed.domain.feed.model

import com.pusu.indexed.domain.discover.model.AnimeItem

/**
 * 动漫详情数据模型
 */
data class AnimeDetailData(
    val id: Int,
    val title: String,
    val titleEnglish: String? = null,
    val titleJapanese: String? = null,
    val imageUrl: String,
    val trailerUrl: String? = null,
    val score: Double? = null,
    val scoredBy: Int? = null,
    val rank: Int? = null,
    val popularity: Int? = null,
    val members: Int? = null,
    val favorites: Int? = null,
    val synopsis: String? = null,
    val background: String? = null,
    val type: String,
    val episodes: Int? = null,
    val status: String? = null,
    val airing: Boolean = false,
    val aired: String? = null,
    val season: String? = null,
    val year: Int? = null,
    val source: String? = null,
    val duration: String? = null,
    val rating: String? = null,
    val studios: List<String> = emptyList(),
    val genres: List<String> = emptyList(),
    val themes: List<String> = emptyList(),
    val demographics: List<String> = emptyList(),
    val producers: List<String> = emptyList(),
    val licensors: List<String> = emptyList(),
    val relatedAnime: List<AnimeItem> = emptyList(),
    val recommendations: List<AnimeItem> = emptyList()
)

