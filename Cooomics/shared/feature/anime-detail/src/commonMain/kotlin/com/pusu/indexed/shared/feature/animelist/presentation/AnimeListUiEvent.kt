package com.pusu.indexed.shared.feature.animedetail.animelist.presentation

/**
 * AnimeList 页面的 UI 事件
 */
sealed class AnimeListUiEvent {
    /**
     * 导航到详情页
     */
    data class NavigateToDetail(val animeId: Int) : AnimeListUiEvent()
    
    /**
     * 显示错误消息
     */
    data class ShowError(val message: String) : AnimeListUiEvent()
    
    /**
     * 显示提示消息
     */
    data class ShowMessage(val message: String) : AnimeListUiEvent()
}

