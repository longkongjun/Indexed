package com.pusu.indexed.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess

suspend inline fun <reified T> HttpClient.getJson(
    url: String,
    query: Map<String, Any?> = emptyMap(),
): Result<T> = runCatching {
    val response = get(url) {
        query.forEach { (key, value) ->
            if (value != null) parameter(key, value)
        }
    }
    if (!response.status.isSuccess()) throw ResponseException(response, "HTTP ${'$'}{response.status}")
    response.body<T>()
}

inline fun <T> Result<T>.onHttpError(action: (ResponseException) -> Unit): Result<T> {
    exceptionOrNull()?.let { if (it is ResponseException) action(it) }
    return this
}

inline fun <T> Result<T>.onNetworkError(action: (Throwable) -> Unit): Result<T> {
    exceptionOrNull()?.let { if (it !is ResponseException) action(it) }
    return this
}

inline fun <T, R> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> =
    fold(transform) { Result.failure(it) }

inline fun <T1, T2, R> Result<T1>.zip(other: Result<T2>, transform: (T1, T2) -> R): Result<R> =
    flatMap { t1 -> other.map { t2 -> transform(t1, t2) } }

fun <T> Iterable<Result<T>>.combine(): Result<List<T>> = runCatching {
    buildList {
        for (result in this@combine) add(result.getOrThrow())
    }
}
