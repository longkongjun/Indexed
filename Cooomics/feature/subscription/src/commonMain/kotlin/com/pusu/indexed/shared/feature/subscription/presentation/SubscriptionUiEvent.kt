package com.pusu.indexed.shared.feature.subscription.presentation

sealed interface SubscriptionUiEvent {
    data class NavigateToDetail(val animeId: Int) : SubscriptionUiEvent
    data class ShowMessage(val message: String) : SubscriptionUiEvent
}
