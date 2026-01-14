package com.pusu.indexed.shared.feature.discover.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pusu.indexed.domain.anime.model.AnimeItem
import com.pusu.indexed.shared.feature.discover.filter.presentation.FilterIntent
import com.pusu.indexed.shared.feature.discover.filter.presentation.FilterUiEvent
import com.pusu.indexed.shared.feature.discover.filter.presentation.FilterViewModel

/**
 * Filter 主屏幕
 * 
 * @param viewModel ViewModel 实例
 * @param onNavigateBack 返回上一页的回调
 * @param onNavigateToDetail 导航到详情页的回调
 */
@Composable
fun FilterScreen(
    viewModel: FilterViewModel,
    onNavigateBack: () -> Unit = {},
    onNavigateToDetail: (Int) -> Unit = {}
) {
    // 收集 UI 状态
    val uiState by viewModel.uiState.collectAsState()
    
    // 监听 UI 事件
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is FilterUiEvent.NavigateToDetail -> {
                    onNavigateToDetail(event.animeId)
                }
                is FilterUiEvent.ShowError -> {
                    // TODO: 显示 Snackbar 或 Toast
                    println("Error: ${event.message}")
                }
            }
        }
    }
    
    // 渲染 UI
    Column(modifier = Modifier.fillMaxSize()) {
        // 顶部栏
        FilterTopBar(
            onBack = onNavigateBack,
            hasActiveFilters = uiState.hasActiveFilters,
            onClearFilters = {
                viewModel.handleIntent(FilterIntent.ClearFilters)
            }
        )
        
        // 内容区域
        Row(modifier = Modifier.fillMaxSize()) {
            // 左侧筛选面板
            FilterPanel(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight(),
                uiState = uiState,
                onIntent = { viewModel.handleIntent(it) }
            )
            
            // 右侧结果列表
            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight()
            ) {
                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    uiState.hasError -> {
                        ErrorState(
                            error = uiState.error ?: "筛选失败",
                            onRetry = {
                                viewModel.handleIntent(FilterIntent.Retry)
                            }
                        )
                    }
                    uiState.showEmptyState -> {
                        EmptyState()
                    }
                    uiState.hasResults -> {
                        FilterResults(
                            results = uiState.results,
                            onAnimeClick = { animeId ->
                                viewModel.handleIntent(FilterIntent.OnAnimeClick(animeId))
                            }
                        )
                    }
                    else -> {
                        InitialState(
                            onApplyFilters = {
                                viewModel.handleIntent(FilterIntent.ApplyFilters)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterTopBar(
    onBack: () -> Unit,
    hasActiveFilters: Boolean,
    onClearFilters: () -> Unit
) {
    TopAppBar(
        title = { Text("筛选漫画") },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "返回")
            }
        },
        actions = {
            if (hasActiveFilters) {
                TextButton(onClick = onClearFilters) {
                    Text("清除筛选")
                }
            }
        }
    )
}

@Composable
private fun FilterPanel(
    modifier: Modifier = Modifier,
    uiState: com.pusu.indexed.shared.feature.discover.filter.presentation.FilterUiState,
    onIntent: (FilterIntent) -> Unit
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 类型筛选
            item {
                FilterSection(
                    title = "类型",
                    items = uiState.availableTypes,
                    selectedItems = uiState.selectedTypes,
                    onItemClick = { type ->
                        onIntent(FilterIntent.ToggleType(type))
                    }
                )
            }
            
            // 年份筛选
            item {
                FilterSection(
                    title = "年份",
                    items = uiState.availableYears.map { it.toString() },
                    selectedItems = uiState.selectedYears.map { it.toString() }.toSet(),
                    onItemClick = { year ->
                        onIntent(FilterIntent.ToggleYear(year.toInt()))
                    }
                )
            }
            
            // 类型标签筛选
            item {
                FilterSection(
                    title = "类型标签",
                    items = uiState.availableGenres,
                    selectedItems = uiState.selectedGenres,
                    onItemClick = { genre ->
                        onIntent(FilterIntent.ToggleGenre(genre))
                    }
                )
            }
            
            // 状态筛选
            item {
                FilterSection(
                    title = "播放状态",
                    items = uiState.availableStatuses,
                    selectedItems = uiState.selectedStatuses,
                    onItemClick = { status ->
                        onIntent(FilterIntent.ToggleStatus(status))
                    }
                )
            }
            
            // 应用筛选按钮
            item {
                Button(
                    onClick = { onIntent(FilterIntent.ApplyFilters) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.hasActiveFilters && !uiState.isLoading
                ) {
                    Text("应用筛选")
                }
            }
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    items: List<String>,
    selectedItems: Set<String>,
    onItemClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.height(((items.size + 1) / 2 * 48).dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                FilterChip(
                    selected = selectedItems.contains(item),
                    onClick = { onItemClick(item) },
                    label = { 
                        Text(
                            text = item,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        ) 
                    },
                    leadingIcon = if (selectedItems.contains(item)) {
                        {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "已选择",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null
                )
            }
        }
    }
}

@Composable
private fun FilterResults(
    results: List<AnimeItem>,
    onAnimeClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "找到 ${results.size} 个结果",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        items(results) { anime ->
            AnimeItemCard(
                anime = anime,
                onClick = { onAnimeClick(anime.id) }
            )
        }
    }
}

@Composable
private fun AnimeItemCard(
    anime: AnimeItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.title,
                modifier = Modifier
                    .size(80.dp)
                    .aspectRatio(3f / 4f),
                contentScale = ContentScale.Crop
            )
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    anime.type?.let { type ->
                        Text(
                            text = type,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    anime.year?.let { year ->
                        Text(
                            text = year.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                if (anime.score != null) {
                    Text(
                        text = "评分: ${anime.score}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorState(
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
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "没有找到符合条件的漫画",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun InitialState(
    onApplyFilters: () -> Unit
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
                text = "选择筛选条件并应用",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(onClick = onApplyFilters) {
                Text("应用筛选")
            }
        }
    }
}
