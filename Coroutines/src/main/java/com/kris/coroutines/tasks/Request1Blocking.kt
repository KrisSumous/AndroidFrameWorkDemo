package com.kris.coroutines.tasks

import com.kris.coroutines.api.GithubService
import com.kris.coroutines.data.RequestData
import com.kris.coroutines.data.User
import com.kris.coroutines.util.Logger
import retrofit2.Response

fun <T> Response<List<T>>.bodyList(): List<T> {
    return body() ?: listOf()
}

fun loadContributorsBlocking(service: GithubService, req: RequestData) : List<User> {
    val repos = service
        .getOrgReposCall(req.org)
        .execute() // Executes request and blocks the current thread
        .also { Logger.logRepos(req, it) }
        .body() ?: listOf()

    return repos.flatMap { repo ->
        service
            .getRepoContributorsCall(req.org, repo.name)
            .execute() // Executes request and blocks the current thread
            .also { Logger.logUsers(repo, it) }
            .bodyList()
    }
}