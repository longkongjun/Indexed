package com.pusu.indexed.shared.feature.animedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pusu.indexed.domain.discover.model.AnimeItem
import com.pusu.indexed.domain.feed.model.AnimeDetailData
import com.pusu.indexed.shared.feature.animedetail.presentation.AnimeDetailViewModel
import com.pusu.indexed.shared.feature.animedetail.state.AnimeDetailIntent
import com.pusu.indexed.shared.feature.animedetail.state.AnimeDetailUiEvent
import kotlinx.coroutines.flow.collectLatest

/**
 * 动漫详情页主界面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailScreen(
    animeId: Int,
    viewModel: AnimeDetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // 处理 UI 事件
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is AnimeDetailUiEvent.NavigateToAnimeDetail -> {
                    onNavigateToAnimeDetail(event.animeId)
                }
                is AnimeDetailUiEvent.OpenTrailer -> {
                    // TODO: 打开浏览器播放预告片
                    println("Open trailer: ${event.url}")
                }
                is AnimeDetailUiEvent.ShowMessage -> {
                    // TODO: 显示 Snackbar
                    println("Message: ${event.message}")
                }
                is AnimeDetailUiEvent.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }
    
    // 加载数据
    LaunchedEffect(animeId) {
        viewModel.handleIntent(AnimeDetailIntent.LoadAnimeDetail(animeId))
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.anime?.title ?: "详情") },
                navigationIcon = {
                    TextButton(onClick = { viewModel.handleIntent(AnimeDetailIntent.NavigateBack) }) {
                        Text("← 返回")
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.handleIntent(AnimeDetailIntent.AddToFavorites) }) {
                        Text("♥ 收藏")
                    }
                    TextButton(onClick = { viewModel.handleIntent(AnimeDetailIntent.Refresh) }) {
                        Text("⟳ 刷新")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingContent()
                }
                uiState.error != null -> {
                    ErrorContent(
                        error = uiState.error!!,
                        onRetry = { viewModel.handleIntent(AnimeDetailIntent.Retry) }
                    )
                }
                uiState.anime != null -> {
                    AnimeDetailContent(
                        anime = uiState.anime!!,
                        onIntent = viewModel::handleIntent
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "⚠️",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = error,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
            Button(onClick = onRetry) {
                Text("重试")
            }
        }
    }
}

@Composable
private fun AnimeDetailContent(
    anime: AnimeDetailData,
    onIntent: (AnimeDetailIntent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // 1. 封面头部区域（带模糊背景）
        item {
            HeaderSection(anime = anime, onIntent = onIntent)
        }
        
        // 2. 评分和统计信息
        item {
            ScoreSection(anime = anime)
        }
        
        // 3. 标签区域
        item {
            TagsSection(anime = anime)
        }
        
        // 4. 基本信息
        item {
            InfoSection(anime = anime)
        }
        
        // 5. 简介
        item {
            SynopsisSection(anime = anime)
        }
        
        // 6. 制作信息
        item {
            ProductionSection(anime = anime)
        }
        
        // 7. 相关动漫
        if (anime.relatedAnime.isNotEmpty()) {
            item {
                RelatedAnimeSection(
                    relatedAnime = anime.relatedAnime,
                    onAnimeClick = { animeId ->
                        onIntent(AnimeDetailIntent.OnRelatedAnimeClick(animeId))
                    }
                )
            }
        }
        
        // 8. 推荐动漫
        if (anime.recommendations.isNotEmpty()) {
            item {
                RecommendationsSection(
                    recommendations = anime.recommendations,
                    onAnimeClick = { animeId ->
                        onIntent(AnimeDetailIntent.OnRecommendationClick(animeId))
                    }
                )
            }
        }
        
        // 底部间距
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * 头部封面区域（参考 Bilibili、Netflix 等主流 app）
 */
@Composable
private fun HeaderSection(
    anime: AnimeDetailData,
    onIntent: (AnimeDetailIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)  // 占位背景色
    ) {
        // 模糊背景
        AsyncImage(
            model = anime.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(20.dp),
            contentScale = ContentScale.Crop
        )
        
        // 渐变遮罩
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.surface
                        ),
                        startY = 200f
                    )
                )
        )
        
        // 内容区域
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 封面图
            Card(
                modifier = Modifier
                    .width(140.dp)
                    .height(200.dp)
                    .align(Alignment.Bottom),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                AsyncImage(
                    model = anime.imageUrl,
                    contentDescription = anime.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            
            // 标题和操作按钮
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Bottom),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                anime.titleEnglish?.let { englishTitle ->
                    Text(
                        text = englishTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // 播放预告片按钮
                if (anime.trailerUrl != null) {
                    Button(
                        onClick = { onIntent(AnimeDetailIntent.PlayTrailer) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("▶ 播放预告片")
                    }
                }
            }
        }
    }
}

/**
 * 评分和统计信息区域
 */
@Composable
private fun ScoreSection(anime: AnimeDetailData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // 评分
            anime.score?.let { score ->
                // 跨平台格式化：保留两位小数
                val scoreText = ((score * 100).toInt() / 100.0).toString()
                StatItem(
                    label = "评分",
                    value = scoreText,
                    subtitle = "${anime.scoredBy ?: 0} 人评价"
                )
            }
            
            // 排名
            anime.rank?.let { rank ->
                StatItem(
                    label = "排名",
                    value = "#$rank"
                )
            }
            
            // 人气
            anime.popularity?.let { popularity ->
                StatItem(
                    label = "人气",
                    value = "#$popularity"
                )
            }
            
            // 收藏数
            anime.favorites?.let { favorites ->
                StatItem(
                    label = "收藏",
                    value = formatNumber(favorites)
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    subtitle: String? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 标签区域（类型、主题等）
 */
@Composable
private fun TagsSection(anime: AnimeDetailData) {
    if (anime.genres.isEmpty() && anime.themes.isEmpty() && anime.demographics.isEmpty()) {
        return
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "标签",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val allTags = anime.genres + anime.themes + anime.demographics
            items(
                items = allTags,
                key = { tag -> tag }  // ✅ 使用标签文本作为 key
            ) { tag ->
                TagChip(text = tag)
            }
        }
    }
}

@Composable
private fun TagChip(text: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

/**
 * 基本信息区域
 */
@Composable
private fun InfoSection(anime: AnimeDetailData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "基本信息",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            InfoRow(label = "类型", value = anime.type)
            anime.episodes?.let { InfoRow(label = "集数", value = "$it 集") }
            anime.status?.let { InfoRow(label = "状态", value = it) }
            anime.aired?.let { InfoRow(label = "播放时间", value = it) }
            anime.season?.let { season ->
                val seasonText = "${anime.year ?: ""} $season".trim()
                InfoRow(label = "季度", value = seasonText)
            }
            anime.source?.let { InfoRow(label = "原作", value = it) }
            anime.duration?.let { InfoRow(label = "时长", value = it) }
            anime.rating?.let { InfoRow(label = "分级", value = it) }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * 简介区域
 */
@Composable
private fun SynopsisSection(anime: AnimeDetailData) {
    if (anime.synopsis.isNullOrBlank()) return
    
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "简介",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            val synopsisText = anime.synopsis ?: ""
            Text(
                text = synopsisText,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if (expanded) Int.MAX_VALUE else 4,
                overflow = TextOverflow.Ellipsis
            )
            
            TextButton(onClick = { expanded = !expanded }) {
                Text(if (expanded) "收起" else "展开")
            }
        }
    }
}

/**
 * 制作信息区域
 */
@Composable
private fun ProductionSection(anime: AnimeDetailData) {
    if (anime.studios.isEmpty() && anime.producers.isEmpty()) return
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "制作信息",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            if (anime.studios.isNotEmpty()) {
                InfoRow(label = "制作公司", value = anime.studios.joinToString(", "))
            }
            
            if (anime.producers.isNotEmpty()) {
                InfoRow(label = "制作方", value = anime.producers.joinToString(", "))
            }
        }
    }
}

/**
 * 相关动漫区域
 */
@Composable
private fun RelatedAnimeSection(
    relatedAnime: List<AnimeItem>,
    onAnimeClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "相关动漫",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = relatedAnime,
                key = { anime -> anime.id }  // ✅ 添加稳定的 key
            ) { anime ->
                SmallAnimeCard(
                    anime = anime,
                    onClick = { onAnimeClick(anime.id) }
                )
            }
        }
    }
}

/**
 * 推荐动漫区域
 */
@Composable
private fun RecommendationsSection(
    recommendations: List<AnimeItem>,
    onAnimeClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "推荐动漫",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = recommendations,
                key = { anime -> anime.id }  // ✅ 添加稳定的 key
            ) { anime ->
                SmallAnimeCard(
                    anime = anime,
                    onClick = { onAnimeClick(anime.id) }
                )
            }
        }
    }
}

/**
 * 小型动漫卡片
 */
@Composable
private fun SmallAnimeCard(
    anime: AnimeItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(120.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)  // 占位背景色
            ) {
                AsyncImage(
                    model = anime.imageUrl,
                    contentDescription = anime.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                anime.score?.let { score ->
                    // 跨平台格式化：保留一位小数
                    val scoreText = ((score * 10).toInt() / 10.0).toString()
                    Text(
                        text = "⭐ $scoreText",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

/**
 * 格式化数字（例如：10000 -> 10K）
 */
private fun formatNumber(number: Int): String {
    return when {
        number >= 1_000_000 -> {
            val millions = (number / 1_000_000.0 * 10).toInt() / 10.0
            "${millions}M"
        }
        number >= 1_000 -> {
            val thousands = (number / 1_000.0 * 10).toInt() / 10.0
            "${thousands}K"
        }
        else -> number.toString()
    }
}

