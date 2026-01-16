package com.pusu.indexed.shared.feature.subscription.presentation

import com.pusu.indexed.shared.feature.subscription.model.SubscriptionItem

data class SubscriptionUiState(
    val isLoading: Boolean = false,
    val subscriptions: List<SubscriptionItem> = emptyList(),
    val error: String? = null
) {
    val hasContent: Boolean get() = subscriptions.isNotEmpty()
    val hasError: Boolean get() = error != null
}
