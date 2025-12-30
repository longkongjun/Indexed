package com.pusu.indexed.shared.feature.search.presentation

/**
 * Search 页面的用户意图
 * 
 * 表示用户可以执行的所有操作。
 */
sealed interface SearchIntent {
    /**
     * 更新搜索关键词
     */
    data class UpdateQuery(val query: String) : SearchIntent
    
    /**
     * 执行搜索
     */
    data object Search : SearchIntent
    
    /**
     * 清除搜索
     */
    data object Clear : SearchIntent
    
    /**
     * 点击动漫
     */
    data class OnAnimeClick(val animeId: Int) : SearchIntent
    
    /**
     * 重试搜索
     */
    data object Retry : SearchIntent
}

