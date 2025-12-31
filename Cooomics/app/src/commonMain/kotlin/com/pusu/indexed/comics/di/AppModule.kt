package com.pusu.indexed.comics.di

import com.pusu.indexed.data.jikan.mapper.JikanToAnimeDetailMapper
import com.pusu.indexed.data.jikan.mapper.JikanToDiscoverMapper
import com.pusu.indexed.data.jikan.repository.JikanAnimeDetailRepository
import com.pusu.indexed.data.jikan.repository.JikanAnimeRecommendationsRepository
import com.pusu.indexed.data.jikan.repository.JikanAnimeRepository
import com.pusu.indexed.data.jikan.repository.JikanRelatedAnimeRepository
import com.pusu.indexed.domain.anime.repository.AnimeRepository
import com.pusu.indexed.domain.anime.usecase.GetAnimeDetailUseCase
import com.pusu.indexed.domain.anime.usecase.GetAnimeRecommendationsUseCase
import com.pusu.indexed.domain.anime.usecase.GetCurrentSeasonAnimeUseCase
import com.pusu.indexed.domain.anime.usecase.GetRelatedAnimeUseCase
import com.pusu.indexed.domain.anime.usecase.GetTopRankedAnimeUseCase
import com.pusu.indexed.domain.anime.usecase.GetTrendingAnimeUseCase
import com.pusu.indexed.domain.anime.usecase.SearchAnimeUseCase
import com.pusu.indexed.jikan.JikanApi
import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.createJikanApi
import com.pusu.indexed.shared.feature.animedetail.animelist.presentation.AnimeListType
import com.pusu.indexed.shared.feature.animedetail.animelist.presentation.AnimeListViewModel
import com.pusu.indexed.shared.feature.animedetail.presentation.AnimeDetailViewModel
import com.pusu.indexed.shared.feature.discover.presentation.DiscoverViewModel
import com.pusu.indexed.shared.feature.search.presentation.SearchViewModel
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * 应用 Koin 模块
 * 
 * 定义所有依赖的创建方式
 */
fun appModule(httpClient: HttpClient) = module {
    
    // ========================================
    // Core 层：网络配置
    // ========================================
    
    single<HttpClient> { httpClient }
    
    // ========================================
    // Data 层：API 和 Repository
    // ========================================
    
    // Jikan 客户端
    single<JikanClient> {
        JikanClient(
            baseUrl = "https://api.jikan.moe/v4",
            httpClient = get()
        )
    }
    
    // Jikan API 客户端
    single<JikanApi> {
        createJikanApi(get<JikanClient>())
    }
    
    // Mappers
    singleOf(::JikanToDiscoverMapper)
    singleOf(::JikanToAnimeDetailMapper)
    
    // Repository 实现
    single<AnimeRepository> {
        JikanAnimeRepository(
            jikanApi = get(),
            mapper = get<JikanToDiscoverMapper>()
        )
    }
    
    // AnimeDetail Repository (实现了 GetAnimeDetailUseCase 接口)
    single<GetAnimeDetailUseCase> {
        JikanAnimeDetailRepository(
            jikanApi = get(),
            mapper = get<JikanToAnimeDetailMapper>()
        )
    }
    
    // Related Anime Repository
    single<GetRelatedAnimeUseCase> {
        JikanRelatedAnimeRepository(
            jikanApi = get(),
            mapper = get<JikanToAnimeDetailMapper>()
        )
    }
    
    // Recommendations Repository
    single<GetAnimeRecommendationsUseCase> {
        JikanAnimeRecommendationsRepository(
            jikanApi = get(),
            mapper = get<JikanToAnimeDetailMapper>()
        )
    }
    
    // ========================================
    // Domain 层：UseCase
    // ========================================
    
    singleOf(::GetTrendingAnimeUseCase)
    singleOf(::GetCurrentSeasonAnimeUseCase)
    singleOf(::GetTopRankedAnimeUseCase)
    singleOf(::SearchAnimeUseCase)
    
    // ========================================
    // Feature 层：ViewModel
    // ========================================
    
    // ViewModel 使用 factory，每次获取时创建新实例
    factory<DiscoverViewModel> {
        DiscoverViewModel(
            getTrendingAnimeUseCase = get(),
            getCurrentSeasonAnimeUseCase = get(),
            getTopRankedAnimeUseCase = get()
        )
    }
    
    factory<AnimeDetailViewModel> {
        AnimeDetailViewModel(
            getAnimeDetailUseCase = get(),
            getRelatedAnimeUseCase = get(),
            getAnimeRecommendationsUseCase = get()
        )
    }
    
    factory<SearchViewModel> {
        SearchViewModel(
            searchAnimeUseCase = get()
        )
    }
    
    // AnimeListViewModel 使用 factory，通过 initListType 设置参数
    factory<com.pusu.indexed.shared.feature.animedetail.animelist.presentation.AnimeListViewModel> {
        com.pusu.indexed.shared.feature.animedetail.animelist.presentation.AnimeListViewModel(
            getTrendingAnimeUseCase = get(),
            getCurrentSeasonAnimeUseCase = get(),
            getTopRankedAnimeUseCase = get()
        )
    }
}

