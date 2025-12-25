package com.pusu.indexed.jikan.impl

import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.api.RandomApi
import com.pusu.indexed.jikan.models.anime.Anime
import com.pusu.indexed.jikan.models.manga.Manga
import com.pusu.indexed.jikan.models.character.Character
import com.pusu.indexed.jikan.models.people.Person
import com.pusu.indexed.jikan.models.user.User
import com.pusu.indexed.jikan.models.common.*

/**
 * RandomApi 的默认实现。
 * 
 * @property client HTTP 客户端，负责底层网络通信
 */
internal class RandomApiImpl(
    private val client: JikanClient
) : RandomApi {
    
    override suspend fun getRandomAnime(): Result<JikanResponse<Anime>> =
        client.get(path = "random/anime")
    
    override suspend fun getRandomManga(): Result<JikanResponse<Manga>> =
        client.get(path = "random/manga")
    
    override suspend fun getRandomCharacter(): Result<JikanResponse<Character>> =
        client.get(path = "random/characters")
    
    override suspend fun getRandomPerson(): Result<JikanResponse<Person>> =
        client.get(path = "random/people")
    
    override suspend fun getRandomUser(): Result<JikanResponse<User>> =
        client.get(path = "random/users")
}
