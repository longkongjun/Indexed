package com.pusu.indexed.shared.feature.discover.presentation

/**
 * Discover 页面的用户意图
 * 
 * 表示用户可以执行的所有操作。
 * 这是 MVI 架构中的 Intent 部分。
 */
sealed interface DiscoverIntent {
    /**
     * 加载内容
     */
    data object LoadContent : DiscoverIntent
    
    /**
     * 刷新内容
     */
    data object Refresh : DiscoverIntent
    
    /**
     * 点击动漫
     */
    data class OnAnimeClick(val animeId: Int) : DiscoverIntent
    
    /**
     * 重试加载
     */
    data object Retry : DiscoverIntent
}

