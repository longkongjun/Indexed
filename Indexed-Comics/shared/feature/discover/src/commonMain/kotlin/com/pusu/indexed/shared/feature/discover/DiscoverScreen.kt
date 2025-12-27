package com.pusu.indexed.shared.feature.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pusu.indexed.domain.discover.model.AnimeItem
import com.pusu.indexed.shared.feature.discover.presentation.DiscoverIntent
import com.pusu.indexed.shared.feature.discover.presentation.DiscoverUiEvent
import com.pusu.indexed.shared.feature.discover.presentation.DiscoverUiState
import com.pusu.indexed.shared.feature.discover.presentation.DiscoverViewModel

/**
 * Discover 主屏幕
 * 
 * 这是 UI 层的入口组件，展示整个发现页面。
 * 
 * 完整的依赖链：
 * DiscoverScreen → DiscoverViewModel → GetTrendingAnimeUseCase → 
 * DiscoverRepository (接口) → JikanDiscoverRepository (实现) → 
 * JikanApi → HTTP Request
 * 
 * @param viewModel ViewModel 实例
 * @param onNavigateToDetail 导航到详情页的回调
 */
@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel,
    onNavigateToDetail: (Int) -> Unit = {}
) {
    // 1. 收集 UI 状态
    val uiState by viewModel.uiState.collectAsState()
    
    // 2. 监听 UI 事件
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is DiscoverUiEvent.NavigateToDetail -> {
                    onNavigateToDetail(event.animeId)
                }
                is DiscoverUiEvent.ShowError -> {
                    // TODO: 显示 Snackbar 或 Toast
                    println("Error: ${event.message}")
                }
                is DiscoverUiEvent.ShowSuccess -> {
                    // TODO: 显示成功消息
                    println("Success: ${event.message}")
                }
            }
        }
    }
    
    // 3. 渲染 UI
    DiscoverContent(
        uiState = uiState,
        onIntent = viewModel::handleIntent
    )
}

/**
 * Discover 内容
 * 
 * 无状态组件，只负责 UI 展示。
 * 
 * @param uiState UI 状态
 * @param onIntent 处理用户意图的回调
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscoverContent(
    uiState: DiscoverUiState,
    onIntent: (DiscoverIntent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // 顶部标题栏
        TopAppBar(
            title = { Text("发现") }
        )
        
        // 主内容区域
        when {
            // 加载状态
            uiState.isLoading && !uiState.hasContent -> {
                LoadingContent()
            }
            
            // 错误状态
            uiState.hasError && !uiState.hasContent -> {
                ErrorContent(
                    message = uiState.error ?: "加载失败",
                    onRetry = { onIntent(DiscoverIntent.Retry) }
                )
            }
            
            // 有内容
            uiState.hasContent -> {
                ContentList(
                    uiState = uiState,
                    onIntent = onIntent
                )
            }
            
            // 空状态
            else -> {
                EmptyContent()
            }
        }
    }
}

/**
 * 加载状态
 */
@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator()
            Text(
                text = "加载中...",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * 错误状态
 */
@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "😕",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
            Button(onClick = onRetry) {
                Text("重试")
            }
        }
    }
}

/**
 * 空状态
 */
@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "暂无内容",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 内容列表
 */
@Composable
private fun ContentList(
    uiState: DiscoverUiState,
    onIntent: (DiscoverIntent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // 热门动漫区域
        if (uiState.trendingAnime.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "🔥 热门动漫",
                    onSeeAllClick = { /* TODO */ }
                )
            }
            
            item {
                TrendingAnimeRow(
                    animeList = uiState.trendingAnime,
                    onAnimeClick = { animeId ->
                        onIntent(DiscoverIntent.OnAnimeClick(animeId))
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        
        // 本季新番区域
        if (uiState.currentSeasonAnime.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "📺 本季新番",
                    onSeeAllClick = { /* TODO */ }
                )
            }
            
            item {
                TrendingAnimeRow(
                    animeList = uiState.currentSeasonAnime,
                    onAnimeClick = { animeId ->
                        onIntent(DiscoverIntent.OnAnimeClick(animeId))
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        
        // 随机推荐区域
        item {
            RandomPickSection(
                randomAnime = uiState.randomPick,
                onGetRandomClick = { onIntent(DiscoverIntent.GetRandomPick) },
                onAnimeClick = { animeId ->
                    onIntent(DiscoverIntent.OnAnimeClick(animeId))
                }
            )
        }
    }
}

/**
 * 随机推荐区域
 */
@Composable
private fun RandomPickSection(
    randomAnime: AnimeItem?,
    onGetRandomClick: () -> Unit,
    onAnimeClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🎲 随机推荐",
                style = MaterialTheme.typography.titleLarge
            )
            Button(onClick = onGetRandomClick) {
                Text("换一个")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (randomAnime != null) {
            Card(
                onClick = { onAnimeClick(randomAnime.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // 封面
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(140.dp)
                    ) {
                        AsyncImage(
                            model = randomAnime.imageUrl,
                            contentDescription = randomAnime.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // 信息
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = randomAnime.title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        randomAnime.score?.let {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "⭐", style = MaterialTheme.typography.bodyMedium)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = randomAnime.scoreText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        
                        randomAnime.type?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        if (randomAnime.genres.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = randomAnime.genres.take(3).joinToString(" · "),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "🎲",
                            style = MaterialTheme.typography.displayMedium
                        )
                        Text(
                            text = "点击按钮获取随机推荐",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * 区域标题
 */
@Composable
private fun SectionHeader(
    title: String,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
        TextButton(onClick = onSeeAllClick) {
            Text("查看全部")
        }
    }
}

/**
 * 热门动漫横向列表
 */
@Composable
private fun TrendingAnimeRow(
    animeList: List<AnimeItem>,
    onAnimeClick: (Int) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(animeList) { anime ->
            AnimeCard(
                anime = anime,
                onClick = { onAnimeClick(anime.id) }
            )
        }
    }
}

/**
 * 动漫卡片
 */
@Composable
private fun AnimeCard(
    anime: AnimeItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(150.dp)
    ) {
        Column {
            // 封面图片区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AsyncImage(
                    model = anime.imageUrl,
                    contentDescription = anime.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                
                // 排名标签
                anime.rank?.let { rank ->
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = anime.rankText,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            
            // 标题和评分
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // 评分
                anime.score?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⭐",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = anime.scoreText,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

