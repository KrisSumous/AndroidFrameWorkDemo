package com.kris.coroutines.data

import kotlinx.serialization.Serializable

@Serializable
data class Repo(
    val id: Long,
    val name: String
)

@Serializable
data class User(
    val login: String,
    val contributions: Int
)

@Serializable
data class RequestData(
    val username: String = "github username",
    val password: String = "github password",
    val org: String = "kotlin"
)


data class Params(
    val username: String,
    val password: String,
    val org: String,
    val variant: Variant
)

enum class LoadingStatus {
    COMPLETED,
    CANCELED,
    IN_PROGRESS }

enum class Variant {
    BLOCKING,         // Request1Blocking
    BACKGROUND,       // Request2Background
    CALLBACKS,        // Request3Callbacks
    SUSPEND,          // Request4Coroutine
    CONCURRENT,       // Request5Concurrent
    NOT_CANCELLABLE,  // Request6NotCancellable
    PROGRESS,         // Request6Progress
    CHANNELS          // Request7Channels
}


