package com.indexed.server.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class LibraryListResponse(
    val root: String,
    val entries: List<LibraryEntry>
)

@Serializable
data class LibraryEntry(
    val name: String,
    val isDirectory: Boolean
)

@Serializable
data class ErrorBody(
    val error: String,
    val path: String
)
