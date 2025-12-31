package com.pusu.indexed.shared.feature.animedetail.animelist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pusu.indexed.domain.anime.usecase.GetTrendingAnimeUseCase
import com.pusu.indexed.domain.anime.usecase.GetCurrentSeasonAnimeUseCase
import com.pusu.indexed.domain.anime.usecase.GetTopRankedAnimeUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * AnimeList 页面的 ViewModel
 *
 * 职责：
 * 1. 管理 UI 状态（UiState）
 * 2. 处理用户意图（Intent）
 * 3. 调用 UseCase（业务逻辑）
 * 4. 支持分页加载和下拉刷新
 * 
 * 继承自 androidx.lifecycle.ViewModel，使用 viewModelScope 管理协程生命周期
 */
class AnimeListViewModel(
    private val getTrendingAnimeUseCase: GetTrendingAnimeUseCase,
    private val getCurrentSeasonAnimeUseCase: GetCurrentSeasonAnimeUseCase,
    private val getTopRankedAnimeUseCase: GetTopRankedAnimeUseCase
) : ViewModel() {
    // 列表类型（通过 initListType 设置）
    private var listType: AnimeListType? = null
    
    // UI 状态流（初始状态，等待 initListType 设置）
    private val _uiState = MutableStateFlow(
        AnimeListUiState(
            listType = AnimeListType.Trending, // 默认值，会被 initListType 覆盖
            title = ""
        )
    )
    val uiState = _uiState.asStateFlow()

    // UI 事件流（一次性事件）
    private val _uiEvent = MutableSharedFlow<AnimeListUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    // 每页加载数量
    private val pageSize = 20
    
    /**
     * 初始化列表类型
     * 必须在创建 ViewModel 后立即调用
     */
    fun initListType(type: AnimeListType) {
        if (listType != null) {
            // 已经初始化过，不允许重复设置
            return
        }
        
        listType = type
        _uiState.value = AnimeListUiState(
            listType = type,
            title = when (type) {
                AnimeListType.Trending -> "🔥 热门动漫"
                AnimeListType.CurrentSeason -> "📺 本季新番"
                AnimeListType.TopRanked -> "🏆 排行榜"
            }
        )
        
        // 初始加载
        handleIntent(AnimeListIntent.LoadInitial)
    }

    /**
     * 处理用户意图
     */
    fun handleIntent(intent: AnimeListIntent) {
        when (intent) {
            is AnimeListIntent.LoadInitial -> loadInitial()
            is AnimeListIntent.Refresh -> refresh()
            is AnimeListIntent.LoadMore -> loadMore()
            is AnimeListIntent.Retry -> retry()
            is AnimeListIntent.OnAnimeClick -> navigateToDetail(intent.animeId)
        }
    }

    /**
     * 初始加载
     */
    private fun loadInitial() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = loadData(page = 1)

            result.fold(
                onSuccess = { animeList ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            animeList = animeList,
                            currentPage = 1,
                            hasMore = animeList.size >= pageSize,
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
                    _uiEvent.emit(AnimeListUiEvent.ShowError("加载失败，请重试"))
                }
            )
        }
    }

    /**
     * 下拉刷新
     */
    private fun refresh() {
        if (_uiState.value.isRefreshing) return

        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }

            val result = loadData(page = 1)

            result.fold(
                onSuccess = { animeList ->
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            animeList = animeList,
                            currentPage = 1,
                            hasMore = animeList.size >= pageSize,
                            error = null
                        )
                    }
                    _uiEvent.emit(AnimeListUiEvent.ShowMessage("刷新成功"))
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            error = error.message ?: "刷新失败"
                        )
                    }
                    _uiEvent.emit(AnimeListUiEvent.ShowError("刷新失败"))
                }
            )
        }
    }

    /**
     * 加载更多
     */
    private fun loadMore() {
        val currentState = _uiState.value
        
        // 如果正在加载、没有更多数据、或有错误，则不加载
        if (currentState.isLoadingMore || !currentState.hasMore || currentState.hasError) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }

            val nextPage = currentState.currentPage + 1
            val result = loadData(page = nextPage)

            result.fold(
                onSuccess = { newAnimeList ->
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            animeList = it.animeList + newAnimeList,
                            currentPage = nextPage,
                            hasMore = newAnimeList.size >= pageSize
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            error = error.message ?: "加载更多失败"
                        )
                    }
                    _uiEvent.emit(AnimeListUiEvent.ShowError("加载更多失败"))
                }
            )
        }
    }

    /**
     * 重试
     */
    private fun retry() {
        loadInitial()
    }

    /**
     * 导航到详情页
     */
    private fun navigateToDetail(animeId: Int) {
        viewModelScope.launch {
            _uiEvent.emit(AnimeListUiEvent.NavigateToDetail(animeId))
        }
    }

    /**
     * 根据列表类型加载数据
     */
    private suspend fun loadData(page: Int): Result<List<com.pusu.indexed.domain.anime.model.AnimeItem>> {
        val currentListType = listType ?: return Result.failure(IllegalStateException("ListType not initialized"))
        
        return when (currentListType) {
            AnimeListType.Trending -> {
                getTrendingAnimeUseCase(page = page, limit = pageSize)
            }
            AnimeListType.CurrentSeason -> {
                getCurrentSeasonAnimeUseCase(page = page, limit = pageSize)
            }
            AnimeListType.TopRanked -> {
                getTopRankedAnimeUseCase(page = page, limit = pageSize)
            }
        }
    }
}
