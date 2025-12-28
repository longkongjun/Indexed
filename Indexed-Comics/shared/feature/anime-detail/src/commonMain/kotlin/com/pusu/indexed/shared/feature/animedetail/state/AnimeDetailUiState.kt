package com.pusu.indexed.shared.feature.animedetail.state

import com.pusu.indexed.domain.feed.model.AnimeDetailData

/**
 * 动漫详情页 UI 状态
 */
data class AnimeDetailUiState(
    val isLoading: Boolean = false,
    val anime: AnimeDetailData? = null,
    val error: String? = null,
    val isRefreshing: Boolean = false
)

