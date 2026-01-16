package com.pusu.indexed.shared.feature.subscription.model

/**
 * 订阅条目
 */
data class SubscriptionItem(
    val animeId: Int,
    val title: String,
    val coverUrl: String?,
    val latestEpisode: String,
    val updateStatus: String,
    val updateTime: String,
    val isNotifyEnabled: Boolean,
    val tags: List<String>
)
