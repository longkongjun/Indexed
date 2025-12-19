package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.anime.Anime
import com.pusu.indexed.jikan.models.character.Character
import com.pusu.indexed.jikan.models.common.*
import com.pusu.indexed.jikan.models.manga.Manga
import com.pusu.indexed.jikan.models.people.Person
import com.pusu.indexed.jikan.models.user.User
import retrofit2.http.GET

/**
 * 随机相关 API 接口
 * 提供随机获取各类信息的功能
 */
interface RandomApi {
    
    /**
     * 获取随机动漫
     * 
     * @return 动漫信息响应
     */
    @GET("random/anime")
    suspend fun getRandomAnime(): Result<JikanResponse<Anime>>
    
    /**
     * 获取随机漫画
     * 
     * @return 漫画信息响应
     */
    @GET("random/manga")
    suspend fun getRandomManga(): Result<JikanResponse<Manga>>
    
    /**
     * 获取随机角色
     * 
     * @return 角色信息响应
     */
    @GET("random/characters")
    suspend fun getRandomCharacter(): Result<JikanResponse<Character>>
    
    /**
     * 获取随机人物
     * 
     * @return 人物信息响应
     */
    @GET("random/people")
    suspend fun getRandomPerson(): Result<JikanResponse<Person>>
    
    /**
     * 获取随机用户
     * 
     * @return 用户信息响应
     */
    @GET("random/users")
    suspend fun getRandomUser(): Result<JikanResponse<User>>
}

