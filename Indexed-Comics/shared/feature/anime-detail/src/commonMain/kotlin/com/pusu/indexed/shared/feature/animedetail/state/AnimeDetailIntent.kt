package com.pusu.indexed.shared.feature.animedetail.state

/**
 * 动漫详情页用户意图
 */
sealed class AnimeDetailIntent {
    /**
     * 加载动漫详情
     */
    data class LoadAnimeDetail(val animeId: Int) : AnimeDetailIntent()
    
    /**
     * 刷新详情
     */
    data object Refresh : AnimeDetailIntent()
    
    /**
     * 播放预告片
     */
    data object PlayTrailer : AnimeDetailIntent()
    
    /**
     * 添加到收藏
     */
    data object AddToFavorites : AnimeDetailIntent()
    
    /**
     * 点击相关动漫
     */
    data class OnRelatedAnimeClick(val animeId: Int) : AnimeDetailIntent()
    
    /**
     * 点击推荐动漫
     */
    data class OnRecommendationClick(val animeId: Int) : AnimeDetailIntent()
    
    /**
     * 重试加载
     */
    data object Retry : AnimeDetailIntent()
    
    /**
     * 返回
     */
    data object NavigateBack : AnimeDetailIntent()
}

