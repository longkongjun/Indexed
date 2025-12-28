package com.pusu.indexed.shared.feature.animedetail.state

/**
 * 动漫详情页 UI 事件
 */
sealed class AnimeDetailUiEvent {
    /**
     * 导航到其他动漫详情
     */
    data class NavigateToAnimeDetail(val animeId: Int) : AnimeDetailUiEvent()
    
    /**
     * 打开预告片
     */
    data class OpenTrailer(val url: String) : AnimeDetailUiEvent()
    
    /**
     * 显示提示消息
     */
    data class ShowMessage(val message: String) : AnimeDetailUiEvent()
    
    /**
     * 返回上一页
     */
    data object NavigateBack : AnimeDetailUiEvent()
}

