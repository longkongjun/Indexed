package com.pusu.indexed.webapp.di

import com.pusu.indexed.data.jikan.mapper.JikanToDiscoverMapper
import com.pusu.indexed.data.jikan.mapper.JikanToAnimeDetailMapper
import com.pusu.indexed.data.jikan.repository.JikanDiscoverRepository
import com.pusu.indexed.data.jikan.repository.JikanAnimeDetailRepository
import com.pusu.indexed.data.jikan.repository.JikanRelatedAnimeRepository
import com.pusu.indexed.data.jikan.repository.JikanAnimeRecommendationsRepository
import com.pusu.indexed.domain.discover.repository.DiscoverRepository
import com.pusu.indexed.domain.discover.usecase.GetCurrentSeasonAnimeUseCase
import com.pusu.indexed.domain.discover.usecase.GetTopAnimeUseCase
import com.pusu.indexed.domain.discover.usecase.GetTrendingAnimeUseCase
import com.pusu.indexed.domain.feed.usecase.GetAnimeDetailUseCase
import com.pusu.indexed.domain.feed.usecase.GetRelatedAnimeUseCase
import com.pusu.indexed.domain.feed.usecase.GetAnimeRecommendationsUseCase
import com.pusu.indexed.jikan.JikanApi
import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.createJikanApi
import com.pusu.indexed.shared.feature.discover.presentation.DiscoverViewModel
import com.pusu.indexed.shared.feature.animedetail.presentation.AnimeDetailViewModel
import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope

/**
 * Web 应用的依赖注入容器
 */
class DependencyContainer(
    httpClient: HttpClient
) {
    private val httpClient: HttpClient = httpClient
    
    private val jikanClient: JikanClient by lazy {
        JikanClient(
            baseUrl = "https://api.jikan.moe/v4",
            httpClient = httpClient
        )
    }
    
    private val jikanApi: JikanApi by lazy {
        createJikanApi(jikanClient)
    }
    
    private val jikanToDiscoverMapper: JikanToDiscoverMapper by lazy {
        JikanToDiscoverMapper()
    }
    
    private val jikanToAnimeDetailMapper: JikanToAnimeDetailMapper by lazy {
        JikanToAnimeDetailMapper()
    }
    
    private val discoverRepository: DiscoverRepository by lazy {
        JikanDiscoverRepository(
            jikanApi = jikanApi,
            mapper = jikanToDiscoverMapper
        )
    }
    
    private val getTrendingAnimeUseCase: GetTrendingAnimeUseCase by lazy {
        GetTrendingAnimeUseCase(
            repository = discoverRepository
        )
    }
    
    private val getCurrentSeasonAnimeUseCase: GetCurrentSeasonAnimeUseCase by lazy {
        GetCurrentSeasonAnimeUseCase(
            repository = discoverRepository
        )
    }
    
    private val getTopAnimeUseCase: GetTopAnimeUseCase by lazy {
        GetTopAnimeUseCase(
            repository = discoverRepository
        )
    }
    
    private val getAnimeDetailUseCase: GetAnimeDetailUseCase by lazy {
        JikanAnimeDetailRepository(
            jikanApi = jikanApi,
            mapper = jikanToAnimeDetailMapper
        )
    }
    
    private val getRelatedAnimeUseCase: GetRelatedAnimeUseCase by lazy {
        JikanRelatedAnimeRepository(
            jikanApi = jikanApi,
            mapper = jikanToAnimeDetailMapper
        )
    }
    
    private val getAnimeRecommendationsUseCase: GetAnimeRecommendationsUseCase by lazy {
        JikanAnimeRecommendationsRepository(
            jikanApi = jikanApi,
            mapper = jikanToAnimeDetailMapper
        )
    }
    
    fun createDiscoverViewModel(coroutineScope: CoroutineScope): DiscoverViewModel {
        return DiscoverViewModel(
            getTrendingAnimeUseCase = getTrendingAnimeUseCase,
            getCurrentSeasonAnimeUseCase = getCurrentSeasonAnimeUseCase,
            getTopAnimeUseCase = getTopAnimeUseCase,
            coroutineScope = coroutineScope
        )
    }
    
    fun createAnimeDetailViewModel(coroutineScope: CoroutineScope): AnimeDetailViewModel {
        return AnimeDetailViewModel(
            getAnimeDetailUseCase = getAnimeDetailUseCase,
            getRelatedAnimeUseCase = getRelatedAnimeUseCase,
            getAnimeRecommendationsUseCase = getAnimeRecommendationsUseCase,
            coroutineScope = coroutineScope
        )
    }
}

