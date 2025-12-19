package com.pusu.indexed.jikan

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pusu.indexed.jikan.api.*
import com.pusu.indexed.jikan.network.JikanJson
import com.pusu.indexed.jikan.network.ResultCallAdapterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Jikan API 客户端
 * 提供统一的 Retrofit 实例和 API 服务访问入口
 */
object JikanApiClient {
    
    /**
     * Jikan API 基础 URL
     */
    private const val BASE_URL = "https://api.jikan.moe/v4/"
    
    /**
     * 连接超时时间（秒）
     */
    private const val CONNECT_TIMEOUT = 30L
    
    /**
     * 读取超时时间（秒）
     */
    private const val READ_TIMEOUT = 30L
    
    /**
     * 写入超时时间（秒）
     */
    private const val WRITE_TIMEOUT = 30L
    
    /**
     * OkHttp 客户端实例
     * 配置了日志拦截器和超时设置
     */
    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
    
    /**
     * Retrofit 实例
     * 使用 Kotlin Serialization 作为 JSON 转换器
     * 使用 ResultCallAdapterFactory 支持 Kotlin Result
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .addConverterFactory(JikanJson.json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
    
    /**
     * 动漫相关 API 服务
     */
    val animeApi: AnimeApi by lazy {
        retrofit.create(AnimeApi::class.java)
    }
    
    /**
     * 漫画相关 API 服务
     */
    val mangaApi: MangaApi by lazy {
        retrofit.create(MangaApi::class.java)
    }
    
    /**
     * 角色相关 API 服务
     */
    val charactersApi: CharactersApi by lazy {
        retrofit.create(CharactersApi::class.java)
    }
    
    /**
     * 人物相关 API 服务
     */
    val peopleApi: PeopleApi by lazy {
        retrofit.create(PeopleApi::class.java)
    }
    
    /**
     * 季度动漫相关 API 服务
     */
    val seasonsApi: SeasonsApi by lazy {
        retrofit.create(SeasonsApi::class.java)
    }
    
    /**
     * 播放时间表相关 API 服务
     */
    val schedulesApi: SchedulesApi by lazy {
        retrofit.create(SchedulesApi::class.java)
    }
    
    /**
     * 排行榜相关 API 服务
     */
    val topApi: TopApi by lazy {
        retrofit.create(TopApi::class.java)
    }
    
    /**
     * 类型/标签相关 API 服务
     */
    val genresApi: GenresApi by lazy {
        retrofit.create(GenresApi::class.java)
    }
    
    /**
     * 制作公司相关 API 服务
     */
    val producersApi: ProducersApi by lazy {
        retrofit.create(ProducersApi::class.java)
    }
    
    /**
     * 杂志/出版社相关 API 服务
     */
    val magazinesApi: MagazinesApi by lazy {
        retrofit.create(MagazinesApi::class.java)
    }
    
    /**
     * 俱乐部相关 API 服务
     */
    val clubsApi: ClubsApi by lazy {
        retrofit.create(ClubsApi::class.java)
    }
    
    /**
     * 用户相关 API 服务
     */
    val usersApi: UsersApi by lazy {
        retrofit.create(UsersApi::class.java)
    }
    
    /**
     * 评论相关 API 服务
     */
    val reviewsApi: ReviewsApi by lazy {
        retrofit.create(ReviewsApi::class.java)
    }
    
    /**
     * 推荐相关 API 服务
     */
    val recommendationsApi: RecommendationsApi by lazy {
        retrofit.create(RecommendationsApi::class.java)
    }
    
    /**
     * 观看相关 API 服务
     */
    val watchApi: WatchApi by lazy {
        retrofit.create(WatchApi::class.java)
    }
    
    /**
     * 随机相关 API 服务
     */
    val randomApi: RandomApi by lazy {
        retrofit.create(RandomApi::class.java)
    }
}

