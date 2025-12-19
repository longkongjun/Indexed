package com.pusu.indexed.jikan.network

import com.pusu.indexed.jikan.models.common.JikanErrorResponse
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlinx.serialization.decodeFromString
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Retrofit CallAdapter Factory，用于支持 Kotlin Result 类型
 * 
 * 使用示例：
 * ```
 * Retrofit.Builder()
 *     .addCallAdapterFactory(ResultCallAdapterFactory())
 *     .build()
 * ```
 */
class ResultCallAdapterFactory : CallAdapter.Factory() {
    
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // 检查返回类型是否为 Call
        if (getRawType(returnType) != Call::class.java) {
            return null
        }
        
        // 获取 Call 的泛型参数
        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        
        // 检查泛型参数是否为 Result
        if (getRawType(callType) != Result::class.java) {
            return null
        }
        
        // 获取 Result 的泛型参数（实际的响应类型）
        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
        
        return ResultCallAdapter<Any>(resultType)
    }
    
    /**
     * Result CallAdapter 实现
     * 将 Retrofit Call 适配为返回 Result 的 Call
     */
    private class ResultCallAdapter<R>(
        private val responseType: Type
    ) : CallAdapter<R, Call<Result<R>>> {
        
        override fun responseType(): Type = responseType
        
        override fun adapt(call: Call<R>): Call<Result<R>> {
            return ResultCall(call)
        }
    }
    
    /**
     * Result Call 实现
     * 包装原始的 Retrofit Call，将响应转换为 Result
     */
    private class ResultCall<R>(
        private val delegate: Call<R>
    ) : Call<Result<R>> {
        
        override fun enqueue(callback: Callback<Result<R>>) {
            delegate.enqueue(object : Callback<R> {
                override fun onResponse(call: Call<R>, response: Response<R>) {
                    val result = if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            Result.success(body)
                        } else {
                            Result.failure(NullPointerException("响应体为空"))
                        }
                    } else {
                        val errorBodyString = response.errorBody()?.string()
                        val errorResponse = errorBodyString
                            ?.takeIf { it.isNotBlank() }
                            ?.let { raw ->
                                runCatching { JikanJson.json.decodeFromString<JikanErrorResponse>(raw) }.getOrNull()
                            }
                        Result.failure(
                            HttpException(
                                code = response.code(),
                                message = response.message(),
                                errorBody = errorBodyString,
                                errorResponse = errorResponse,
                            )
                        )
                    }
                    callback.onResponse(this@ResultCall, Response.success(result))
                }
                
                override fun onFailure(call: Call<R>, t: Throwable) {
                    val result = Result.failure<R>(t)
                    callback.onResponse(this@ResultCall, Response.success(result))
                }
            })
        }
        
        override fun execute(): Response<Result<R>> {
            return runCatching {
                val response = delegate.execute()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(NullPointerException("响应体为空"))
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    val errorResponse = errorBodyString
                        ?.takeIf { it.isNotBlank() }
                        ?.let { raw ->
                            runCatching { JikanJson.json.decodeFromString<JikanErrorResponse>(raw) }.getOrNull()
                        }
                    Result.failure(
                        HttpException(
                            code = response.code(),
                            message = response.message(),
                            errorBody = errorBodyString,
                            errorResponse = errorResponse,
                        )
                    )
                }
            }.fold(
                onSuccess = { result -> Response.success(result) },
                onFailure = { exception -> Response.success(Result.failure(exception)) }
            )
        }
        
        override fun clone(): Call<Result<R>> = ResultCall(delegate.clone())
        
        override fun request(): Request = delegate.request()
        
        override fun timeout(): Timeout = delegate.timeout()
        
        override fun isExecuted(): Boolean = delegate.isExecuted
        
        override fun isCanceled(): Boolean = delegate.isCanceled
        
        override fun cancel() = delegate.cancel()
    }
}

/**
 * HTTP 异常
 * 用于封装 HTTP 错误响应
 * 
 * @property code HTTP 状态码
 * @property message 错误消息
 * @property errorBody 错误响应体
 */
data class HttpException(
    val code: Int,
    override val message: String,
    val errorBody: String?,
    val errorResponse: JikanErrorResponse? = null,
) : Exception(message) {
    
    override fun toString(): String {
        return "HttpException(code=$code, message='$message', errorBody='$errorBody', errorResponse=$errorResponse)"
    }
}

