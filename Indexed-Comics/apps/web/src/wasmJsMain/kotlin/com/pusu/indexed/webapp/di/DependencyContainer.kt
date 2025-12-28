package com.pusu.indexed.webapp.di

import com.pusu.indexed.data.jikan.mapper.JikanToDiscoverMapper
import com.pusu.indexed.data.jikan.repository.JikanDiscoverRepository
import com.pusu.indexed.domain.discover.repository.DiscoverRepository
import com.pusu.indexed.domain.discover.usecase.GetCurrentSeasonAnimeUseCase
import com.pusu.indexed.domain.discover.usecase.GetRandomAnimeUseCase
import com.pusu.indexed.domain.discover.usecase.GetTrendingAnimeUseCase
import com.pusu.indexed.jikan.JikanApi
import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.createJikanApi
import com.pusu.indexed.shared.feature.discover.presentation.DiscoverViewModel
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
    
    private val getRandomAnimeUseCase: GetRandomAnimeUseCase by lazy {
        GetRandomAnimeUseCase(
            repository = discoverRepository
        )
    }
    
    fun createDiscoverViewModel(coroutineScope: CoroutineScope): DiscoverViewModel {
        return DiscoverViewModel(
            getTrendingAnimeUseCase = getTrendingAnimeUseCase,
            getCurrentSeasonAnimeUseCase = getCurrentSeasonAnimeUseCase,
            getRandomAnimeUseCase = getRandomAnimeUseCase,
            coroutineScope = coroutineScope
        )
    }
}

