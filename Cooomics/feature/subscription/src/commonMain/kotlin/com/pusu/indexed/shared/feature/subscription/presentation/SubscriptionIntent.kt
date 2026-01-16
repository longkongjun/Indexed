package com.pusu.indexed.shared.feature.subscription.presentation

sealed interface SubscriptionIntent {
    data object Refresh : SubscriptionIntent
    data class OpenDetail(val animeId: Int) : SubscriptionIntent
    data class ToggleNotify(val animeId: Int) : SubscriptionIntent
    data class Unsubscribe(val animeId: Int) : SubscriptionIntent
}
