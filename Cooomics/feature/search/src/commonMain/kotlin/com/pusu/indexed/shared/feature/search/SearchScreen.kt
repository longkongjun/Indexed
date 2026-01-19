package com.pusu.indexed.shared.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
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
import com.pusu.indexed.shared.feature.search.presentation.SearchIntent
import com.pusu.indexed.shared.feature.search.presentation.SearchUiEvent
import com.pusu.indexed.shared.feature.search.presentation.SearchViewModel

/**
 * Search 主屏幕
 * 
 * @param viewModel ViewModel 实例
 * @param onNavigateBack 返回上一页的回调
 * @param onNavigateToDetail 导航到详情页的回调
 */
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    appLanguage: AppLanguage,
    onNavigateBack: () -> Unit = {},
    onNavigateToDetail: (Int) -> Unit = {}
) {
    // 收集 UI 状态
    val uiState by viewModel.uiState.collectAsState()
    
    // 监听 UI 事件
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SearchUiEvent.NavigateToDetail -> {
                    onNavigateToDetail(event.animeId)
                }
                is SearchUiEvent.ShowError -> {
                    // TODO: 显示 Snackbar 或 Toast
                    println("Error: ${event.message}")
                }
            }
        }
    }
    
    // 渲染 UI
    Column(modifier = Modifier.fillMaxSize()) {
        // 搜索栏
        SearchBar(
            query = uiState.query,
            isLoading = uiState.isLoading,
            onQueryChange = { query ->
                viewModel.handleIntent(SearchIntent.UpdateQuery(query))
            },
            onSearch = {
                viewModel.handleIntent(SearchIntent.Search)
            },
            onClear = {
                viewModel.handleIntent(SearchIntent.Clear)
            },
            onBack = onNavigateBack
        )
        
        // 内容区域
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
                    error = uiState.error ?: "搜索失败",
                    onRetry = {
                        viewModel.handleIntent(SearchIntent.Retry)
                    }
                )
            }
            uiState.showEmptyState && uiState.query.isNotBlank() -> {
                EmptyState()
            }
            uiState.hasResults -> {
                SearchResults(
                    results = uiState.results,
                    appLanguage = appLanguage,
                    onAnimeClick = { animeId ->
                        viewModel.handleIntent(SearchIntent.OnAnimeClick(animeId))
                    }
                )
            }
            else -> {
                // 初始状态，显示提示
                InitialState()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    isLoading: Boolean,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("搜索动漫...") },
                leadingIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                trailingIcon = {
                    if (query.isNotBlank()) {
                        IconButton(onClick = onClear) {
                            Icon(Icons.Default.Clear, contentDescription = "清除")
                        }
                    }
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        IconButton(onClick = onSearch) {
                            Icon(Icons.Default.Search, contentDescription = "搜索")
                        }
                    }
                },
                singleLine = true,
                enabled = !isLoading
            )
        }
    )
}

@Composable
private fun SearchResults(
    results: List<AnimeItem>,
    appLanguage: AppLanguage,
    onAnimeClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(results) { anime ->
            AnimeItemCard(
                anime = anime,
                appLanguage = appLanguage,
                onClick = { onAnimeClick(anime.id) }
            )
        }
    }
}

@Composable
private fun AnimeItemCard(
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
                contentDescription = displayTitle,
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
                    text = displayTitle,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (anime.score != null) {
                    Text(
                        text = "评分: ${anime.score}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                anime.type?.let { type ->
                    Text(
                        text = type,
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
            text = "没有找到相关结果",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun InitialState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "输入关键词搜索动漫",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

