package com.pusu.indexed.shared.feature.subscription.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pusu.indexed.shared.feature.subscription.model.SubscriptionItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SubscriptionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SubscriptionUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<SubscriptionUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        loadSubscriptions()
    }

    fun handleIntent(intent: SubscriptionIntent) {
        when (intent) {
            is SubscriptionIntent.Refresh -> loadSubscriptions()
            is SubscriptionIntent.OpenDetail -> navigateToDetail(intent.animeId)
            is SubscriptionIntent.ToggleNotify -> toggleNotify(intent.animeId)
            is SubscriptionIntent.Unsubscribe -> unsubscribe(intent.animeId)
        }
    }

    private fun loadSubscriptions() {
        _uiState.update {
            it.copy(
                isLoading = false,
                error = null,
                subscriptions = sampleSubscriptions()
            )
        }
    }

    private fun navigateToDetail(animeId: Int) {
        viewModelScope.launch {
            _uiEvent.emit(SubscriptionUiEvent.NavigateToDetail(animeId))
        }
    }

    private fun toggleNotify(animeId: Int) {
        _uiState.update { state ->
            val updated = state.subscriptions.map { item ->
                if (item.animeId == animeId) {
                    item.copy(isNotifyEnabled = !item.isNotifyEnabled)
                } else {
                    item
                }
            }
            state.copy(subscriptions = updated)
        }
    }

    private fun unsubscribe(animeId: Int) {
        _uiState.update { state ->
            val updated = state.subscriptions.filterNot { it.animeId == animeId }
            state.copy(subscriptions = updated)
        }
        viewModelScope.launch {
            _uiEvent.emit(SubscriptionUiEvent.ShowMessage("已取消订阅"))
        }
    }

    private fun sampleSubscriptions(): List<SubscriptionItem> {
        return listOf(
            SubscriptionItem(
                animeId = 1,
                title = "孤独摇滚！",
                coverUrl = "https://cdn.myanimelist.net/images/anime/1448/127956.jpg",
                latestEpisode = "更新至第 12 话",
                updateStatus = "已完结",
                updateTime = "更新于 昨天",
                isNotifyEnabled = true,
                tags = listOf("音乐", "校园", "日常")
            ),
            SubscriptionItem(
                animeId = 2,
                title = "迷宫饭",
                coverUrl = "https://cdn.myanimelist.net/images/anime/1009/138006.jpg",
                latestEpisode = "更新至第 8 话",
                updateStatus = "连载中",
                updateTime = "更新于 2 天前",
                isNotifyEnabled = false,
                tags = listOf("奇幻", "冒险")
            ),
            SubscriptionItem(
                animeId = 3,
                title = "我推的孩子",
                coverUrl = "https://cdn.myanimelist.net/images/anime/1812/134736.jpg",
                latestEpisode = "更新至第 11 话",
                updateStatus = "连载中",
                updateTime = "更新于 1 周前",
                isNotifyEnabled = true,
                tags = listOf("剧情", "偶像")
            )
        )
    }
}
