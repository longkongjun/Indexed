package com.pusu.indexed.shared.feature.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pusu.indexed.core.locale.AppLanguage
import com.pusu.indexed.core.locale.resolveTitle
import com.pusu.indexed.domain.anime.model.AnimeItem
import com.pusu.indexed.shared.feature.animedetail.animelist.presentation.AnimeListType
import com.pusu.indexed.shared.feature.discover.presentation.DiscoverIntent
import com.pusu.indexed.shared.feature.discover.presentation.DiscoverUiEvent
import com.pusu.indexed.shared.feature.discover.presentation.DiscoverUiState
import com.pusu.indexed.shared.feature.discover.presentation.DiscoverViewModel
import kotlinx.coroutines.launch

/**
 * Discover 主屏幕
 * 
 * 这是 UI 层的入口组件，展示整个发现页面。
 * 
 * 完整的依赖链：
 * DiscoverScreen → DiscoverViewModel → GetTrendingAnimeUseCase → 
 * AnimeRepository (接口) → JikanAnimeRepository (实现) → 
 * JikanApi → HTTP Request
 * 
 * @param viewModel ViewModel 实例
 * @param onNavigateToDetail 导航到详情页的回调
 */
@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel,
    appLanguage: AppLanguage,
    onNavigateToDetail: (Int) -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToFilter: () -> Unit = {},
    onNavigateToSubscription: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToList: (AnimeListType) -> Unit = {}
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
        appLanguage = appLanguage,
        onIntent = viewModel::handleIntent,
        onSearchClick = onNavigateToSearch,
        onFilterClick = onNavigateToFilter,
        onSubscriptionClick = onNavigateToSubscription,
        onSettingsClick = onNavigateToSettings,
        onSeeAllClick = onNavigateToList
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
    appLanguage: AppLanguage,
    onIntent: (DiscoverIntent) -> Unit,
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit,
    onSubscriptionClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSeeAllClick: (AnimeListType) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "菜单",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                NavigationDrawerItem(
                    label = { Text("发现") },
                    selected = true,
                    onClick = {
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    label = { Text("我的订阅") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onSubscriptionClick()
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    label = { Text("设置") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onSettingsClick()
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 顶部标题栏
            TopAppBar(
                title = { Text("发现") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "菜单")
                    }
                },
                actions = {
                    // 筛选按钮
                    TextButton(onClick = onFilterClick) {
                        Text("🎯 筛选")
                    }
                    // 搜索按钮
                    TextButton(onClick = onSearchClick) {
                        Text("🔍 搜索")
                    }
                }
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
                    appLanguage = appLanguage,
                        onIntent = onIntent,
                        onSeeAllClick = onSeeAllClick
                    )
                }

                // 空状态
                else -> {
                    EmptyContent()
                }
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
    appLanguage: AppLanguage,
    onIntent: (DiscoverIntent) -> Unit,
    onSeeAllClick: (AnimeListType) -> Unit
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
                    onSeeAllClick = { 
                        onSeeAllClick(AnimeListType.Trending)
                    }
                )
            }
            
            item {
                TrendingAnimeRow(
                    animeList = uiState.trendingAnime,
                    appLanguage = appLanguage,
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
                    onSeeAllClick = { 
                        onSeeAllClick(AnimeListType.CurrentSeason)
                    }
                )
            }
            
            item {
                TrendingAnimeRow(
                    animeList = uiState.currentSeasonAnime,
                    appLanguage = appLanguage,
                    onAnimeClick = { animeId ->
                        onIntent(DiscoverIntent.OnAnimeClick(animeId))
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        
        // 排行榜区域
        if (uiState.topAnime.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "🏆 排行榜",
                    onSeeAllClick = { 
                        onSeeAllClick(AnimeListType.TopRanked)
                    }
                )
            }
            
            item {
                TrendingAnimeRow(
                    animeList = uiState.topAnime,
                    appLanguage = appLanguage,
                    onAnimeClick = { animeId ->
                        onIntent(DiscoverIntent.OnAnimeClick(animeId))
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
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
    appLanguage: AppLanguage,
    onAnimeClick: (Int) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = animeList,
            key = { anime -> anime.id }  // ✅ 添加稳定的 key
        ) { anime ->
            AnimeCard(
                anime = anime,
                appLanguage = appLanguage,
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
    appLanguage: AppLanguage,
    onClick: () -> Unit
) {
    val displayTitle = resolveTitle(
        defaultTitle = anime.title,
        titleEnglish = anime.titleEnglish,
        titleJapanese = anime.titleJapanese,
        language = appLanguage
    )
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
                    .background(MaterialTheme.colorScheme.surfaceVariant)  // 占位背景色
            ) {
                AsyncImage(
                    model = anime.imageUrl,
                    contentDescription = displayTitle,
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
                    text = displayTitle,
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

