package com.pusu.indexed.shared.feature.discover.filter.presentation

/**
 * Filter 页面的 UI 事件
 * 
 * 表示 ViewModel 发送给 UI 的一次性事件（如导航、Toast 等）。
 */
sealed interface FilterUiEvent {
    /**
     * 导航到详情页
     */
    data class NavigateToDetail(val animeId: Int) : FilterUiEvent
    
    /**
     * 显示错误信息
     */
    data class ShowError(val message: String) : FilterUiEvent
}
