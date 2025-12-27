package com.pusu.indexed.shared.feature.discover.presentation

/**
 * Discover 页面的 UI 事件
 * 
 * 表示需要 UI 层响应的一次性事件（如导航、显示 Toast 等）。
 * 这些事件不应该存储在状态中，而是通过事件流传递。
 */
sealed interface DiscoverUiEvent {
    /**
     * 显示错误消息
     */
    data class ShowError(val message: String) : DiscoverUiEvent
    
    /**
     * 导航到动漫详情页
     */
    data class NavigateToDetail(val animeId: Int) : DiscoverUiEvent
    
    /**
     * 显示成功消息
     */
    data class ShowSuccess(val message: String) : DiscoverUiEvent
}

