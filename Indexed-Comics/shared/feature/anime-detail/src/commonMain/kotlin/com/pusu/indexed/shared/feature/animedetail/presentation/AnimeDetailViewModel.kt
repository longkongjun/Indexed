package com.pusu.indexed.shared.feature.animedetail.presentation

import com.pusu.indexed.domain.feed.usecase.GetAnimeDetailUseCase
import com.pusu.indexed.domain.feed.usecase.GetAnimeRecommendationsUseCase
import com.pusu.indexed.domain.feed.usecase.GetRelatedAnimeUseCase
import com.pusu.indexed.shared.feature.animedetail.state.AnimeDetailIntent
import com.pusu.indexed.shared.feature.animedetail.state.AnimeDetailUiEvent
import com.pusu.indexed.shared.feature.animedetail.state.AnimeDetailUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 动漫详情页 ViewModel
 */
class AnimeDetailViewModel(
    private val getAnimeDetailUseCase: GetAnimeDetailUseCase,
    private val getRelatedAnimeUseCase: GetRelatedAnimeUseCase,
    private val getAnimeRecommendationsUseCase: GetAnimeRecommendationsUseCase,
    private val coroutineScope: CoroutineScope
) {
    private val _uiState = MutableStateFlow(AnimeDetailUiState())
    val uiState = _uiState.asStateFlow()
    
    private val _uiEvent = MutableSharedFlow<AnimeDetailUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    
    private var currentAnimeId: Int? = null
    
    fun handleIntent(intent: AnimeDetailIntent) {
        when (intent) {
            is AnimeDetailIntent.LoadAnimeDetail -> loadAnimeDetail(intent.animeId)
            is AnimeDetailIntent.Refresh -> refresh()
            is AnimeDetailIntent.PlayTrailer -> playTrailer()
            is AnimeDetailIntent.AddToFavorites -> addToFavorites()
            is AnimeDetailIntent.OnRelatedAnimeClick -> navigateToAnimeDetail(intent.animeId)
            is AnimeDetailIntent.OnRecommendationClick -> navigateToAnimeDetail(intent.animeId)
            is AnimeDetailIntent.Retry -> retry()
            is AnimeDetailIntent.NavigateBack -> navigateBack()
        }
    }
    
    private fun loadAnimeDetail(animeId: Int) {
        currentAnimeId = animeId
        coroutineScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                // 加载动漫详情
                val detailResult = getAnimeDetailUseCase(animeId)
                
                detailResult.fold(
                    onSuccess = { detailData ->
                        // 加载相关动漫和推荐
                        val relatedResult = getRelatedAnimeUseCase(animeId)
                        val recommendationsResult = getAnimeRecommendationsUseCase(animeId)
                        
                        val relatedAnime = relatedResult.getOrNull() ?: emptyList()
                        val recommendations = recommendationsResult.getOrNull() ?: emptyList()
                        
                        val completeData = detailData.copy(
                            relatedAnime = relatedAnime,
                            recommendations = recommendations
                        )
                        
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                anime = completeData,
                                error = null
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = error.message ?: "加载失败"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "未知错误"
                    )
                }
            }
        }
    }
    
    private fun refresh() {
        currentAnimeId?.let { animeId ->
            coroutineScope.launch {
                _uiState.update { it.copy(isRefreshing = true) }
                
                try {
                    val detailResult = getAnimeDetailUseCase(animeId)
                    
                    detailResult.fold(
                        onSuccess = { detailData ->
                            val relatedResult = getRelatedAnimeUseCase(animeId)
                            val recommendationsResult = getAnimeRecommendationsUseCase(animeId)
                            
                            val relatedAnime = relatedResult.getOrNull() ?: emptyList()
                            val recommendations = recommendationsResult.getOrNull() ?: emptyList()
                            
                            val completeData = detailData.copy(
                                relatedAnime = relatedAnime,
                                recommendations = recommendations
                            )
                            
                            _uiState.update {
                                it.copy(
                                    isRefreshing = false,
                                    anime = completeData,
                                    error = null
                                )
                            }
                        },
                        onFailure = { error ->
                            _uiState.update { it.copy(isRefreshing = false) }
                            _uiEvent.emit(
                                AnimeDetailUiEvent.ShowMessage(
                                    error.message ?: "刷新失败"
                                )
                            )
                        }
                    )
                } catch (e: Exception) {
                    _uiState.update { it.copy(isRefreshing = false) }
                    _uiEvent.emit(
                        AnimeDetailUiEvent.ShowMessage(
                            e.message ?: "刷新失败"
                        )
                    )
                }
            }
        }
    }
    
    private fun playTrailer() {
        coroutineScope.launch {
            _uiState.value.anime?.trailerUrl?.let { url ->
                _uiEvent.emit(AnimeDetailUiEvent.OpenTrailer(url))
            } ?: run {
                _uiEvent.emit(AnimeDetailUiEvent.ShowMessage("暂无预告片"))
            }
        }
    }
    
    private fun addToFavorites() {
        coroutineScope.launch {
            // TODO: 实现收藏功能
            _uiEvent.emit(AnimeDetailUiEvent.ShowMessage("已添加到收藏"))
        }
    }
    
    private fun navigateToAnimeDetail(animeId: Int) {
        coroutineScope.launch {
            _uiEvent.emit(AnimeDetailUiEvent.NavigateToAnimeDetail(animeId))
        }
    }
    
    private fun retry() {
        currentAnimeId?.let { loadAnimeDetail(it) }
    }
    
    private fun navigateBack() {
        coroutineScope.launch {
            _uiEvent.emit(AnimeDetailUiEvent.NavigateBack)
        }
    }
}

