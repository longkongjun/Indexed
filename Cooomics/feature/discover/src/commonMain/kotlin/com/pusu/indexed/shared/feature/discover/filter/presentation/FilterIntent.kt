package com.pusu.indexed.shared.feature.discover.filter.presentation

/**
 * Filter 页面的用户意图
 * 
 * 表示用户可以执行的所有操作。
 */
sealed interface FilterIntent {
    /**
     * 切换类型选择
     */
    data class ToggleType(val type: String) : FilterIntent
    
    /**
     * 切换年份选择
     */
    data class ToggleYear(val year: Int) : FilterIntent
    
    /**
     * 切换类型标签选择
     */
    data class ToggleGenre(val genre: String) : FilterIntent
    
    /**
     * 切换状态选择
     */
    data class ToggleStatus(val status: String) : FilterIntent
    
    /**
     * 应用筛选
     */
    data object ApplyFilters : FilterIntent
    
    /**
     * 清除所有筛选条件
     */
    data object ClearFilters : FilterIntent
    
    /**
     * 点击动漫
     */
    data class OnAnimeClick(val animeId: Int) : FilterIntent
    
    /**
     * 重试加载
     */
    data object Retry : FilterIntent
}
