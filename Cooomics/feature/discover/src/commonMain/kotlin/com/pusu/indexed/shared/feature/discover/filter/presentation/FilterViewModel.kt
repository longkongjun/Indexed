package com.pusu.indexed.shared.feature.discover.filter.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pusu.indexed.domain.anime.usecase.FilterAnimeUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Filter 页面的 ViewModel
 * 
 * 职责：
 * 1. 管理 UI 状态（UiState）
 * 2. 处理用户意图（Intent）
 * 3. 调用 UseCase（业务逻辑）
 * 4. 发送 UI 事件（UiEvent）
 * 
 * 继承自 androidx.lifecycle.ViewModel，使用 viewModelScope 管理协程生命周期
 */
class FilterViewModel(
    private val filterAnimeUseCase: FilterAnimeUseCase
) : ViewModel() {
    // UI 状态流
    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState = _uiState.asStateFlow()
    
    // UI 事件流（一次性事件）
    private val _uiEvent = MutableSharedFlow<FilterUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    
    /**
     * 处理用户意图
     */
    fun handleIntent(intent: FilterIntent) {
        when (intent) {
            is FilterIntent.ToggleType -> toggleType(intent.type)
            is FilterIntent.ToggleYear -> toggleYear(intent.year)
            is FilterIntent.ToggleGenre -> toggleGenre(intent.genre)
            is FilterIntent.ToggleStatus -> toggleStatus(intent.status)
            is FilterIntent.ApplyFilters -> applyFilters()
            is FilterIntent.ClearFilters -> clearFilters()
            is FilterIntent.OnAnimeClick -> navigateToDetail(intent.animeId)
            is FilterIntent.Retry -> retry()
        }
    }
    
    /**
     * 切换类型选择
     */
    private fun toggleType(type: String) {
        _uiState.update { currentState ->
            val newTypes = if (currentState.selectedTypes.contains(type)) {
                currentState.selectedTypes - type
            } else {
                currentState.selectedTypes + type
            }
            currentState.copy(selectedTypes = newTypes)
        }
    }
    
    /**
     * 切换年份选择
     */
    private fun toggleYear(year: Int) {
        _uiState.update { currentState ->
            val newYears = if (currentState.selectedYears.contains(year)) {
                currentState.selectedYears - year
            } else {
                currentState.selectedYears + year
            }
            currentState.copy(selectedYears = newYears)
        }
    }
    
    /**
     * 切换类型标签选择
     */
    private fun toggleGenre(genre: String) {
        _uiState.update { currentState ->
            val newGenres = if (currentState.selectedGenres.contains(genre)) {
                currentState.selectedGenres - genre
            } else {
                currentState.selectedGenres + genre
            }
            currentState.copy(selectedGenres = newGenres)
        }
    }
    
    /**
     * 切换状态选择
     */
    private fun toggleStatus(status: String) {
        _uiState.update { currentState ->
            val newStatuses = if (currentState.selectedStatuses.contains(status)) {
                currentState.selectedStatuses - status
            } else {
                currentState.selectedStatuses + status
            }
            currentState.copy(selectedStatuses = newStatuses)
        }
    }
    
    /**
     * 应用筛选
     */
    private fun applyFilters() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val currentState = _uiState.value
            val result = filterAnimeUseCase(
                types = currentState.selectedTypes,
                years = currentState.selectedYears,
                genres = currentState.selectedGenres,
                statuses = currentState.selectedStatuses,
                page = 1,
                limit = 20
            )
            
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
                            error = error.message ?: "筛选失败",
                            showEmptyState = true
                        )
                    }
                    _uiEvent.emit(FilterUiEvent.ShowError("筛选失败，请重试"))
                }
            )
        }
    }
    
    /**
     * 清除所有筛选条件
     */
    private fun clearFilters() {
        _uiState.update {
            FilterUiState()
        }
    }
    
    /**
     * 导航到详情页
     */
    private fun navigateToDetail(animeId: Int) {
        viewModelScope.launch {
            _uiEvent.emit(FilterUiEvent.NavigateToDetail(animeId))
        }
    }
    
    /**
     * 重试筛选
     */
    private fun retry() {
        applyFilters()
    }
}
