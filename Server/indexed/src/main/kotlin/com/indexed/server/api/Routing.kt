package com.indexed.server.api

import com.indexed.server.api.dto.ErrorBody
import com.indexed.server.api.dto.LibraryEntry
import com.indexed.server.api.dto.LibraryListResponse
import com.indexed.server.config.SourceConfig
import com.indexed.server.storage.ListSourceResult
import com.indexed.server.storage.listSourceRoot
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val sourceConfig = SourceConfig.from(environment.config)
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/config/source") {
            when (val result = listSourceRoot(sourceConfig.root)) {
                is ListSourceResult.Ok -> call.respond(
                    LibraryListResponse(
                        root = result.root,
                        entries = result.entries.map { LibraryEntry(it.name, it.isDirectory) }
                    )
                )
                is ListSourceResult.NotFound -> call.respond(
                    HttpStatusCode.NotFound,
                    ErrorBody(error = "资源来源目录不存在", path = result.path)
                )
                is ListSourceResult.NotDirectory -> call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorBody(error = "资源来源路径不是目录", path = result.path)
                )
            }
        }
    }
}
