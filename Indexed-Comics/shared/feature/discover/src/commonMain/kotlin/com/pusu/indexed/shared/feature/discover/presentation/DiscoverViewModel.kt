package com.pusu.indexed.shared.feature.discover.presentation

import com.pusu.indexed.domain.discover.usecase.GetTrendingAnimeUseCase
import com.pusu.indexed.domain.discover.usecase.GetCurrentSeasonAnimeUseCase
import com.pusu.indexed.domain.discover.usecase.GetTopAnimeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Discover 页面的 ViewModel
 * 
 * 职责：
 * 1. 管理 UI 状态（UiState）
 * 2. 处理用户意图（Intent）
 * 3. 调用 UseCase（业务逻辑）
 * 4. 发送 UI 事件（UiEvent）
 * 
 * 依赖链：
 * UI → ViewModel → UseCase → Repository → API
 * 
 * 注意：这里使用 CoroutineScope 而不是 ViewModel，因为我们需要跨平台支持。
 * 在实际使用中：
 * - Android: 可以继承 androidx.lifecycle.ViewModel
 * - iOS/Desktop/Web: 手动管理生命周期
 */
class DiscoverViewModel(
    private val getTrendingAnimeUseCase: GetTrendingAnimeUseCase,
    private val getCurrentSeasonAnimeUseCase: GetCurrentSeasonAnimeUseCase,
    private val getTopAnimeUseCase: GetTopAnimeUseCase,
    private val coroutineScope: CoroutineScope
) {
    // UI 状态流
    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState = _uiState.asStateFlow()
    
    // UI 事件流（一次性事件）
    private val _uiEvent = MutableSharedFlow<DiscoverUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    
    init {
        // 初始化时自动加载内容
        handleIntent(DiscoverIntent.LoadContent)
    }
    
    /**
     * 处理用户意图
     */
    fun handleIntent(intent: DiscoverIntent) {
        when (intent) {
            is DiscoverIntent.LoadContent -> loadContent()
            is DiscoverIntent.Refresh -> refresh()
            is DiscoverIntent.OnAnimeClick -> navigateToDetail(intent.animeId)
            is DiscoverIntent.Retry -> retry()
        }
    }
    
    /**
     * 加载内容
     * 
     * 依赖链：
     * ViewModel → UseCase → Repository → Jikan API
     */
    private fun loadContent() {
        coroutineScope.launch {
            // 1. 设置加载状态
            _uiState.update { it.copy(isLoading = true, error = null) }

            // 2. 并行加载多个数据源
            val trendingResult = getTrendingAnimeUseCase(page = 1, limit = 10)
            val currentSeasonResult = getCurrentSeasonAnimeUseCase(page = 1, limit = 10)
            val topAnimeResult = getTopAnimeUseCase(page = 1, limit = 10)
            
            // 3. 处理结果
            if (trendingResult.isSuccess || currentSeasonResult.isSuccess || topAnimeResult.isSuccess) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        trendingAnime = trendingResult.getOrNull() ?: emptyList(),
                        currentSeasonAnime = currentSeasonResult.getOrNull() ?: emptyList(),
                        topAnime = topAnimeResult.getOrNull() ?: emptyList(),
                        error = null
                    )
                }
            } else {
                // 所有请求都失败
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = trendingResult.exceptionOrNull()?.message ?: "加载失败"
                    )
                }
                _uiEvent.emit(
                    DiscoverUiEvent.ShowError("加载失败，请重试")
                )
            }
        }
    }
    
    /**
     * 刷新内容
     */
    private fun refresh() {
        coroutineScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            
            // 并行刷新所有数据
            val trendingResult = getTrendingAnimeUseCase(page = 1, limit = 10)
            val currentSeasonResult = getCurrentSeasonAnimeUseCase(page = 1, limit = 10)
            val topAnimeResult = getTopAnimeUseCase(page = 1, limit = 10)
            
            _uiState.update {
                it.copy(
                    isRefreshing = false,
                    trendingAnime = trendingResult.getOrNull() ?: it.trendingAnime,
                    currentSeasonAnime = currentSeasonResult.getOrNull() ?: it.currentSeasonAnime,
                    topAnime = topAnimeResult.getOrNull() ?: it.topAnime,
                    error = null
                )
            }
            
            if (trendingResult.isSuccess || currentSeasonResult.isSuccess || topAnimeResult.isSuccess) {
                _uiEvent.emit(DiscoverUiEvent.ShowSuccess("刷新成功"))
            } else {
                _uiEvent.emit(DiscoverUiEvent.ShowError("刷新失败，请重试"))
            }
        }
    }
    
    /**
     * 导航到详情页
     */
    private fun navigateToDetail(animeId: Int) {
        coroutineScope.launch {
            _uiEvent.emit(DiscoverUiEvent.NavigateToDetail(animeId))
        }
    }
    
    /**
     * 重试加载
     */
    private fun retry() {
        loadContent()
    }
    
    /**
     * 清理资源
     * 
     * 在 ViewModel 销毁时调用（如果有生命周期管理的话）
     */
    fun onCleared() {
        // 取消所有协程
        // 注意：在实际使用中，coroutineScope 应该在这里被取消
    }
}

