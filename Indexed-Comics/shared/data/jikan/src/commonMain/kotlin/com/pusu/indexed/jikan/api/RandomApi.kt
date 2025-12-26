package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.anime.Anime
import com.pusu.indexed.jikan.models.manga.Manga
import com.pusu.indexed.jikan.models.character.Character
import com.pusu.indexed.jikan.models.people.Person
import com.pusu.indexed.jikan.models.user.User
import com.pusu.indexed.jikan.models.common.*

/**
 * Random 随机 API。
 * 
 * 提供随机获取资源的接口，包括：
 * - 随机动漫
 * - 随机漫画
 * - 随机角色
 * - 随机人物
 * - 随机用户
 * 
 * 所有路径对齐官方文档：https://docs.api.jikan.moe/#tag/random
 */
interface RandomApi {
    /**
     * 获取随机动漫。
     * 
     * 路径：`GET /random/anime`
     * 
     * @return 随机动漫的响应结果
     */
    suspend fun getRandomAnime(): Result<JikanResponse<Anime>>
    
    /**
     * 获取随机漫画。
     * 
     * 路径：`GET /random/manga`
     * 
     * @return 随机漫画的响应结果
     */
    suspend fun getRandomManga(): Result<JikanResponse<Manga>>
    
    /**
     * 获取随机角色。
     * 
     * 路径：`GET /random/characters`
     * 
     * @return 随机角色的响应结果
     */
    suspend fun getRandomCharacter(): Result<JikanResponse<Character>>
    
    /**
     * 获取随机人物。
     * 
     * 路径：`GET /random/people`
     * 
     * @return 随机人物的响应结果
     */
    suspend fun getRandomPerson(): Result<JikanResponse<Person>>
    
    /**
     * 获取随机用户。
     * 
     * 路径：`GET /random/users`
     * 
     * @return 随机用户的响应结果
     */
    suspend fun getRandomUser(): Result<JikanResponse<User>>
}

