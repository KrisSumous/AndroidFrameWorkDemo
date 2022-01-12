package com.kris.coroutines

import android.os.Build
import androidx.annotation.RequiresApi
import com.kris.coroutines.api.createGithubService
import com.kris.coroutines.data.*
import com.kris.coroutines.tasks.loadContributorsBlocking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

interface Contributors : CoroutineScope {

    val job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    @RequiresApi(Build.VERSION_CODES.O)
    fun init() {
        loadContributors()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadContributors() {
        val req = RequestData()
        clearResults()
        val service = createGithubService(req.username, req.password)

        val startTime = System.currentTimeMillis()
        when (getSelectedVariant()) {
            Variant.BLOCKING -> { // Blocking UI thread
                val users = loadContributorsBlocking(service, req)
                updateResults(users, startTime)
            }
        }
    }

    private fun clearResults() {
        updateContributors(listOf())
        updateLoadingStatus(LoadingStatus.IN_PROGRESS)
        setActionsStatus(newLoadingEnabled = false)
    }

    private fun updateResults(
        users: List<User>,
        startTime: Long,
        completed: Boolean = true
    ) {
        updateContributors(users)
        updateLoadingStatus(
            if (completed) LoadingStatus.COMPLETED else LoadingStatus.IN_PROGRESS,
            startTime
        )
        if (completed) {
            setActionsStatus(newLoadingEnabled = true)
        }
    }

    private fun updateLoadingStatus(
        status: LoadingStatus,
        startTime: Long? = null
    ) {
        val time = if (startTime != null) {
            val time = System.currentTimeMillis() - startTime
            "${(time / 1000)}.${time % 1000 / 100} sec"
        } else ""

        val text = "Loading status: " +
                when (status) {
                    LoadingStatus.COMPLETED -> "completed in $time"
                    LoadingStatus.IN_PROGRESS -> "in progress $time"
                    LoadingStatus.CANCELED -> "canceled"
                }
        setLoadingStatus(text, status == LoadingStatus.IN_PROGRESS)
    }


    fun getSelectedVariant(): Variant

    fun updateContributors(users: List<User>)

    fun setLoadingStatus(text: String, iconRunning: Boolean)

    fun setActionsStatus(newLoadingEnabled: Boolean, cancellationEnabled: Boolean = false)

    fun setParams(params: Params)

    fun getParams(): Params

}