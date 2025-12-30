package com.pusu.indexed.shared.feature.search.presentation

import com.pusu.indexed.domain.discover.usecase.SearchAnimeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Search 页面的 ViewModel
 * 
 * 职责：
 * 1. 管理 UI 状态（UiState）
 * 2. 处理用户意图（Intent）
 * 3. 调用 UseCase（业务逻辑）
 * 4. 发送 UI 事件（UiEvent）
 */
class SearchViewModel(
    private val searchAnimeUseCase: SearchAnimeUseCase,
    private val coroutineScope: CoroutineScope
) {
    // UI 状态流
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()
    
    // UI 事件流（一次性事件）
    private val _uiEvent = MutableSharedFlow<SearchUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    
    /**
     * 处理用户意图
     */
    fun handleIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.UpdateQuery -> updateQuery(intent.query)
            is SearchIntent.Search -> performSearch()
            is SearchIntent.Clear -> clearSearch()
            is SearchIntent.OnAnimeClick -> navigateToDetail(intent.animeId)
            is SearchIntent.Retry -> retry()
        }
    }
    
    /**
     * 更新搜索关键词
     */
    private fun updateQuery(query: String) {
        _uiState.update { it.copy(query = query) }
    }
    
    /**
     * 执行搜索
     */
    private fun performSearch() {
        val query = _uiState.value.query.trim()
        if (query.isBlank()) {
            return
        }
        
        coroutineScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val result = searchAnimeUseCase(query = query, page = 1, limit = 20)
            
            result.fold(
                onSuccess = { animeList ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            results = animeList,
                            showEmptyState = animeList.isEmpty(),
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "搜索失败",
                            showEmptyState = true
                        )
                    }
                    _uiEvent.emit(SearchUiEvent.ShowError("搜索失败，请重试"))
                }
            )
        }
    }
    
    /**
     * 清除搜索
     */
    private fun clearSearch() {
        _uiState.update {
            SearchUiState()
        }
    }
    
    /**
     * 导航到详情页
     */
    private fun navigateToDetail(animeId: Int) {
        coroutineScope.launch {
            _uiEvent.emit(SearchUiEvent.NavigateToDetail(animeId))
        }
    }
    
    /**
     * 重试搜索
     */
    private fun retry() {
        performSearch()
    }
}

