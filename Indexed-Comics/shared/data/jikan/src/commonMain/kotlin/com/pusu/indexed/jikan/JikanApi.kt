package com.pusu.indexed.jikan

import com.pusu.indexed.jikan.api.*
import com.pusu.indexed.jikan.impl.*

/**
 * Jikan API 主接口。
 * 
 * 这是 Jikan API 的统一入口，聚合了所有资源类型的 API：
 * - [anime]：动漫相关接口
 * - [manga]：漫画相关接口
 * - [characters]：角色相关接口
 * - [people]：人物（声优/制作人员）相关接口
 * - [seasons]：季度相关接口
 * - [producers]：制作公司相关接口
 * - [magazines]：杂志/出版社相关接口
 * - [clubs]：俱乐部相关接口
 * - [users]：用户相关接口
 * - [watch]：观看推荐相关接口
 * - [genres]：类型/题材相关接口
 * - [random]：随机获取相关接口
 * - [recommendations]：全局推荐相关接口
 * - [reviews]：全局评论相关接口
 * - [schedules]：播放时间表相关接口
 * - [top]：排行榜相关接口
 * 
 * 使用示例：
 * ```kotlin
 * val jikanApi = createJikanApi()
 * 
 * // 获取动漫信息
 * val animeResult = jikanApi.anime.getAnimeById(1)
 * 
 * // 搜索漫画
 * val mangaResult = jikanApi.manga.searchManga(query = "One Piece")
 * 
 * // 获取随机动漫
 * val randomAnime = jikanApi.random.getRandomAnime()
 * 
 * // 获取排行榜
 * val topAnime = jikanApi.top.getTopAnime()
 * ```
 * 
 * @property anime 动漫 API
 * @property manga 漫画 API
 * @property characters 角色 API
 * @property people 人物 API
 * @property seasons 季度 API
 * @property producers 制作公司 API
 * @property magazines 杂志 API
 * @property clubs 俱乐部 API
 * @property users 用户 API
 * @property watch 观看推荐 API
 * @property genres 类型/题材 API
 * @property random 随机获取 API
 * @property recommendations 全局推荐 API
 * @property reviews 全局评论 API
 * @property schedules 播放时间表 API
 * @property top 排行榜 API
 * 
 * @see AnimeApi
 * @see MangaApi
 * @see CharacterApi
 * @see PeopleApi
 * @see SeasonApi
 * @see ProducerApi
 * @see MagazineApi
 * @see ClubApi
 * @see UserApi
 * @see WatchApi
 * @see GenresApi
 * @see RandomApi
 * @see RecommendationsApi
 * @see ReviewsApi
 * @see SchedulesApi
 * @see TopApi
 */
interface JikanApi {
    /** 动漫相关接口 */
    val anime: AnimeApi
    
    /** 漫画相关接口 */
    val manga: MangaApi
    
    /** 角色相关接口 */
    val characters: CharacterApi
    
    /** 人物（声优/制作人员）相关接口 */
    val people: PeopleApi
    
    /** 季度相关接口 */
    val seasons: SeasonApi
    
    /** 制作公司相关接口 */
    val producers: ProducerApi
    
    /** 杂志/出版社相关接口 */
    val magazines: MagazineApi
    
    /** 俱乐部相关接口 */
    val clubs: ClubApi
    
    /** 用户相关接口 */
    val users: UserApi
    
    /** 观看推荐相关接口 */
    val watch: WatchApi
    
    /** 类型/题材相关接口 */
    val genres: GenresApi
    
    /** 随机获取相关接口 */
    val random: RandomApi
    
    /** 全局推荐相关接口 */
    val recommendations: RecommendationsApi
    
    /** 全局评论相关接口 */
    val reviews: ReviewsApi
    
    /** 播放时间表相关接口 */
    val schedules: SchedulesApi
    
    /** 排行榜相关接口 */
    val top: TopApi
}

/**
 * JikanApi 的默认实现。
 * 
 * 使用 lazy 初始化所有子 API 实例，只在首次访问时创建，提高性能并节省资源。
 * 
 * @property client HTTP 客户端，负责底层网络通信
 */
internal class JikanApiImpl(
    private val client: JikanClient
) : JikanApi {
    override val anime: AnimeApi by lazy { AnimeApiImpl(client) }
    override val manga: MangaApi by lazy { MangaApiImpl(client) }
    override val characters: CharacterApi by lazy { CharacterApiImpl(client) }
    override val people: PeopleApi by lazy { PeopleApiImpl(client) }
    override val seasons: SeasonApi by lazy { SeasonApiImpl(client) }
    override val producers: ProducerApi by lazy { ProducerApiImpl(client) }
    override val magazines: MagazineApi by lazy { MagazineApiImpl(client) }
    override val clubs: ClubApi by lazy { ClubApiImpl(client) }
    override val users: UserApi by lazy { UserApiImpl(client) }
    override val watch: WatchApi by lazy { WatchApiImpl(client) }
    override val genres: GenresApi by lazy { GenresApiImpl(client) }
    override val random: RandomApi by lazy { RandomApiImpl(client) }
    override val recommendations: RecommendationsApi by lazy { RecommendationsApiImpl(client) }
    override val reviews: ReviewsApi by lazy { ReviewsApiImpl(client) }
    override val schedules: SchedulesApi by lazy { SchedulesApiImpl(client) }
    override val top: TopApi by lazy { TopApiImpl(client) }
}

/**
 * 创建 Jikan API 实例的工厂方法。
 * 
 * 使用默认配置创建一个 JikanApi 实例，baseUrl 为官方 API 地址。
 * 如果需要自定义配置，可以传入自定义的 [JikanClient]。
 * 
 * @param client 可选的自定义 HTTP 客户端，默认使用官方 API
 * @return JikanApi 实例
 * 
 * 使用示例：
 * ```kotlin
 * // 使用默认配置
 * val api = createJikanApi()
 * 
 * // 使用自定义客户端
 * val customClient = JikanClient(baseUrl = "https://custom-api.example.com")
 * val customApi = createJikanApi(customClient)
 * ```
 */
fun createJikanApi(
    client: JikanClient = JikanClient()
): JikanApi = JikanApiImpl(client)

