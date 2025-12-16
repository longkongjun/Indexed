# runCatching 迁移记录

本文档记录了将项目中的 try-catch 块迁移到 Kotlin `runCatching` 的过程。

## 迁移原因

使用 Kotlin 的 `runCatching` 相比传统 try-catch 有以下优势：

1. **更简洁的代码**：函数式风格，链式调用
2. **更好的可组合性**：返回 Result 类型，可以与其他 Result 操作组合
3. **更明确的错误处理**：通过 `fold`、`getOrElse` 等方法强制处理错误
4. **Kotlin 风格**：符合 Kotlin 的函数式编程理念

## 迁移内容

### 1. ResultExtensions.kt - flatMap 方法

**修改前（使用 try-catch）：**
```kotlin
inline fun <T, R> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> {
    return try {
        if (isSuccess) {
            transform(getOrThrow())
        } else {
            Result.failure(exceptionOrNull()!!)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

**修改后（使用 runCatching）：**
```kotlin
inline fun <T, R> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> {
    return if (isSuccess) {
        runCatching { transform(getOrThrow()) }
            .getOrElse { exception -> Result.failure(exception) }
    } else {
        Result.failure(exceptionOrNull()!!)
    }
}
```

**改进点：**
- 只对可能抛出异常的 `transform` 调用使用 `runCatching`
- 使用 `getOrElse` 优雅地处理异常情况
- 避免了 `Result<Result<R>>` 的嵌套问题

### 2. ResultCallAdapterFactory.kt - execute 方法

**修改前（使用 try-catch）：**
```kotlin
override fun execute(): Response<Result<R>> {
    return try {
        val response = delegate.execute()
        val result = if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.success(body)
            } else {
                Result.failure(NullPointerException("响应体为空"))
            }
        } else {
            Result.failure(
                HttpException(
                    code = response.code(),
                    message = response.message(),
                    errorBody = response.errorBody()?.string()
                )
            )
        }
        Response.success(result)
    } catch (e: Exception) {
        Response.success(Result.failure(e))
    }
}
```

**修改后（使用 runCatching）：**
```kotlin
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
            Result.failure(
                HttpException(
                    code = response.code(),
                    message = response.message(),
                    errorBody = response.errorBody()?.string()
                )
            )
        }
    }.fold(
        onSuccess = { result -> Response.success(result) },
        onFailure = { exception -> Response.success(Result.failure(exception)) }
    )
}
```

**改进点：**
- 使用 `runCatching` 包裹整个操作逻辑
- 使用 `fold` 优雅地处理成功和失败两种情况
- 代码更加简洁，逻辑更加清晰

## 迁移要点

### 1. 避免类型嵌套

当函数返回 `Result<T>` 时，如果直接用 `runCatching` 包裹整个逻辑，会导致 `Result<Result<T>>` 嵌套。

**错误示例：**
```kotlin
fun foo(): Result<String> {
    return runCatching {  // 返回 Result<Result<String>>
        Result.success("value")
    }
}
```

**正确做法：**
```kotlin
fun foo(): Result<String> {
    return runCatching {  // 返回 Result<String>
        "value"  // 直接返回值，不要包装在 Result 中
    }
}
```

### 2. 使用 fold 处理复杂返回类型

当返回类型不是 `Result<T>` 时，使用 `fold` 可以更优雅地处理成功和失败情况。

```kotlin
runCatching {
    // 可能抛出异常的操作
}.fold(
    onSuccess = { value -> /* 返回成功结果 */ },
    onFailure = { exception -> /* 返回失败结果 */ }
)
```

### 3. 使用 getOrElse 提供默认值

当需要在失败时提供默认值或转换时，`getOrElse` 比 catch 块更简洁。

```kotlin
runCatching {
    riskyOperation()
}.getOrElse { exception ->
    defaultValue
}
```

### 4. inline 函数与 runCatching

`runCatching` 本身不是 inline 函数，但可以在 inline 函数中使用。注意性能影响。

## 迁移验证

### 编译验证
```bash
./gradlew :jikan:compileDebugKotlin
# ✅ BUILD SUCCESSFUL
```

### Linter 检查
```bash
# ✅ No linter errors found
```

### 检查结果
- ✅ 所有 try-catch 已迁移为 runCatching
- ✅ 编译通过
- ✅ 无 linter 错误
- ✅ 逻辑等价性保持

## 统计信息

- **迁移文件数量**: 2 个
- **迁移方法数量**: 2 个
- **移除 try-catch 数量**: 2 个
- **新增 runCatching 数量**: 2 个

## 相关文件

- `network/ResultExtensions.kt` - Result 扩展方法
- `network/ResultCallAdapterFactory.kt` - Retrofit Result 适配器

## 最佳实践建议

1. **优先使用 runCatching**：新代码应该优先使用 `runCatching` 而不是 try-catch
2. **注意返回类型**：避免 `Result<Result<T>>` 嵌套
3. **使用 fold/getOrElse**：根据场景选择合适的结果处理方法
4. **保持简洁**：不要过度使用，简单的情况可以直接用 try-catch

## 参考资料

- [Kotlin Result 官方文档](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/)
- [runCatching 官方文档](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/run-catching.html)

