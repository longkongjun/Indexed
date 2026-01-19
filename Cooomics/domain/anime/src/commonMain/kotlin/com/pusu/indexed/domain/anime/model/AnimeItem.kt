package com.pusu.indexed.domain.anime.model

/**
 * 发现页面使用的简化动漫模型
 * 
 * 只包含列表展示所需的核心信息，不包含详细信息。
 * 这是一个领域模型，独立于任何数据源。
 * 
 * @property id 动漫唯一标识（MyAnimeList ID）
 * @property title 动漫标题
 * @property titleEnglish 英文标题
 * @property titleJapanese 日文标题
 * @property imageUrl 封面图片 URL
 * @property score 评分（0-10分）
 * @property rank 排名
 * @property type 类型（TV, Movie, OVA, ONA, Special, Music）
 * @property status 播放状态（Airing, Finished, Not yet aired）
 * @property episodes 总集数
 * @property year 播放年份
 * @property season 播放季度（winter, spring, summer, fall）
 * @property genres 类型标签列表
 */
data class AnimeItem(
    val id: Int,
    val title: String,
    val titleEnglish: String? = null,
    val titleJapanese: String? = null,
    val imageUrl: String,
    val score: Double? = null,
    val rank: Int? = null,
    val type: String? = null,
    val status: String? = null,
    val episodes: Int? = null,
    val year: Int? = null,
    val season: String? = null,
    val genres: List<String> = emptyList()
) {
    /**
     * 获取显示用的评分文本（保留两位小数）
     */
    val scoreText: String
        get() = score?.let { 
            // 跨平台的格式化方案：保留两位小数
            val rounded = (it * 100).toInt() / 100.0
            // 确保始终显示两位小数
            val str = rounded.toString()
            val dotIndex = str.indexOf('.')
            if (dotIndex == -1) {
                "$str.00"
            } else {
                val decimals = str.substring(dotIndex + 1)
                when {
                    decimals.length == 0 -> "${str}00"
                    decimals.length == 1 -> "${str}0"
                    else -> str.substring(0, dotIndex + 3)
                }
            }
        } ?: "N/A"
    
    /**
     * 获取显示用的排名文本
     */
    val rankText: String
        get() = rank?.let { "#$it" } ?: ""
    
    /**
     * 获取季度年份文本（如: Winter 2024）
     */
    val seasonYearText: String?
        get() = if (season != null && year != null) {
            "${season.capitalize()} $year"
        } else {
            null
        }
    
    /**
     * 是否正在播放
     */
    val isAiring: Boolean
        get() = status?.equals("Airing", ignoreCase = true) == true
}

/**
 * 字符串首字母大写（跨平台兼容）
 */
private fun String.capitalize(): String {
    if (this.isEmpty()) return this
    return this.replaceFirstChar { 
        if (it.isLowerCase()) it.titlecase() else it.toString() 
    }
}

