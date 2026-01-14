package com.pusu.indexed.shared.feature.discover.filter.presentation

import com.pusu.indexed.domain.anime.model.AnimeItem

/**
 * Filter 页面的 UI 状态
 * 
 * 使用不可变数据类来表示 UI 的所有可能状态。
 */
data class FilterUiState(
    /**
     * 是否正在加载
     */
    val isLoading: Boolean = false,
    
    /**
     * 错误消息（如果有）
     */
    val error: String? = null,
    
    /**
     * 筛选结果列表
     */
    val results: List<AnimeItem> = emptyList(),
    
    /**
     * 是否显示空状态
     */
    val showEmptyState: Boolean = false,
    
    /**
     * 已选择的类型（TV, Movie, OVA, ONA, Special, Music）
     */
    val selectedTypes: Set<String> = emptySet(),
    
    /**
     * 已选择的年份
     */
    val selectedYears: Set<Int> = emptySet(),
    
    /**
     * 已选择的类型标签（动作、冒险等）
     */
    val selectedGenres: Set<String> = emptySet(),
    
    /**
     * 已选择的状态（Airing, Finished, Not yet aired）
     */
    val selectedStatuses: Set<String> = emptySet(),
    
    /**
     * 可用的年份列表（从当前年份往前推20年）
     */
    val availableYears: List<Int> = generateYearList(),
    
    /**
     * 可用的类型列表
     */
    val availableTypes: List<String> = listOf("TV", "Movie", "OVA", "ONA", "Special", "Music"),
    
    /**
     * 可用的状态列表
     */
    val availableStatuses: List<String> = listOf("Airing", "Finished", "Not yet aired"),
    
    /**
     * 可用的类型标签列表
     */
    val availableGenres: List<String> = listOf(
        "Action", "Adventure", "Comedy", "Drama", "Fantasy", 
        "Horror", "Mystery", "Romance", "Sci-Fi", "Slice of Life",
        "Sports", "Supernatural", "Thriller"
    )
) {
    /**
     * 是否有错误
     */
    val hasError: Boolean
        get() = error != null
    
    /**
     * 是否有筛选结果
     */
    val hasResults: Boolean
        get() = results.isNotEmpty()
    
    /**
     * 是否已应用任何筛选条件
     */
    val hasActiveFilters: Boolean
        get() = selectedTypes.isNotEmpty() || 
                selectedYears.isNotEmpty() || 
                selectedGenres.isNotEmpty() || 
                selectedStatuses.isNotEmpty()
                
    companion object {
        /**
         * 生成年份列表（从当前年份往前推20年）
         */
        private fun generateYearList(): List<Int> {
            val currentYear = 2026 // 可以根据需要动态获取
            return (currentYear downTo currentYear - 20).toList()
        }
    }
}
