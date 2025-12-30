package com.pusu.indexed.shared.feature.animedetail.animelist.presentation

/**
 * AnimeList 页面的用户意图
 */
sealed class AnimeListIntent {
    /**
     * 初始加载
     */
    data object LoadInitial : AnimeListIntent()
    
    /**
     * 下拉刷新
     */
    data object Refresh : AnimeListIntent()
    
    /**
     * 加载更多
     */
    data object LoadMore : AnimeListIntent()
    
    /**
     * 重试
     */
    data object Retry : AnimeListIntent()
    
    /**
     * 点击动漫
     */
    data class OnAnimeClick(val animeId: Int) : AnimeListIntent()
}

