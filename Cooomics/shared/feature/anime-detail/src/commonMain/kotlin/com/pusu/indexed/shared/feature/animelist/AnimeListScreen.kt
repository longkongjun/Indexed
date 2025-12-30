package com.pusu.indexed.shared.feature.animedetail.animelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pusu.indexed.domain.discover.model.AnimeItem
import com.pusu.indexed.shared.feature.animedetail.animelist.presentation.AnimeListIntent
import com.pusu.indexed.shared.feature.animedetail.animelist.presentation.AnimeListUiEvent
import com.pusu.indexed.shared.feature.animedetail.animelist.presentation.AnimeListUiState
import com.pusu.indexed.shared.feature.animedetail.animelist.presentation.AnimeListViewModel

/**
 * 动漫列表页面
 *
 * 支持：
 * - 下拉刷新
 * - 自动加载更多（滚动到底部）
 * - 网格布局
 */
@Composable
fun AnimeListScreen(
    viewModel: AnimeListViewModel,
    onNavigateBack: () -> Unit = {},
    onNavigateToDetail: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // 监听 UI 事件
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AnimeListUiEvent.NavigateToDetail -> onNavigateToDetail(event.animeId)
                is AnimeListUiEvent.ShowError -> println("Error: ${event.message}")
                is AnimeListUiEvent.ShowMessage -> println("Message: ${event.message}")
            }
        }
    }

    AnimeListContent(
        uiState = uiState,
        onIntent = viewModel::handleIntent,
        onNavigateBack = onNavigateBack
    )
}

/**
 * 动漫列表内容
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimeListContent(
    uiState: AnimeListUiState,
    onIntent: (AnimeListIntent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
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
                // 初始加载状态
                uiState.isLoading && !uiState.hasContent -> {
                    LoadingView()
                }
                // 错误状态（无内容时）
                uiState.hasError && !uiState.hasContent -> {
                    ErrorView(
                        message = uiState.error ?: "加载失败",
                        onRetry = { onIntent(AnimeListIntent.Retry) }
                    )
                }
                // 空状态
                uiState.showEmptyState -> {
                    EmptyView()
                }
                // 有内容
                uiState.hasContent -> {
                    AnimeGridList(
                        uiState = uiState,
                        onIntent = onIntent
                    )
                }
            }
        }
    }
}

/**
 * 动漫网格列表（支持下拉刷新和自动加载更多）
 */
@Composable
private fun AnimeGridList(
    uiState: AnimeListUiState,
    onIntent: (AnimeListIntent) -> Unit
) {
    val listState = rememberLazyGridState()
    
    // 监听滚动到底部，触发加载更多
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null) {
                    val totalItems = listState.layoutInfo.totalItemsCount
                    // 当滚动到倒数第 3 个时，触发加载更多
                    if (lastVisibleIndex >= totalItems - 3 && uiState.hasMore && !uiState.isLoadingMore) {
                        onIntent(AnimeListIntent.LoadMore)
                    }
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 下拉刷新指示器
        if (uiState.isRefreshing) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 网格列表
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = listState,
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = uiState.animeList,
                key = { anime -> anime.id }
            ) { anime ->
                AnimeGridItem(
                    anime = anime,
                    onClick = { onIntent(AnimeListIntent.OnAnimeClick(anime.id)) }
                )
            }

            // 加载更多指示器
            if (uiState.isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                    }
                }
            }

            // 没有更多数据提示
            if (!uiState.hasMore && uiState.animeList.isNotEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "没有更多了",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * 动漫网格项
 */
@Composable
private fun AnimeGridItem(
    anime: AnimeItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        Column {
            // 封面图片
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            // 标题和评分
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⭐ ${anime.score}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * 加载状态
 */
@Composable
private fun LoadingView() {
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
private fun ErrorView(
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
private fun EmptyView() {
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

