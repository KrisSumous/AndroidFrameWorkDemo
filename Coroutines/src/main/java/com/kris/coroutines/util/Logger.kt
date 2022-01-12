package com.kris.coroutines.util

import android.util.Log
import com.kris.coroutines.data.Repo
import com.kris.coroutines.data.RequestData
import com.kris.coroutines.data.User
import retrofit2.Response

object Logger {

    fun logRepos(req: RequestData, response: Response<List<Repo>>) {
        val repos = response.body()
        if (!response.isSuccessful || repos == null) {
            Log.e(
                "kris",
                "Failed loading repos for ${req.org} with response: '${response.code()}: ${response.message()}'"
            )
        } else {
            Log.e("kris", "${req.org}: loaded ${repos.size} repos")
        }
    }

    fun logUsers(repo: Repo, response: Response<List<User>>) {
        val users = response.body()
        if (!response.isSuccessful || users == null) {
            Log.e(
                "kris",
                "Failed loading contributors for ${repo.name} with response '${response.code()}: ${response.message()}'"
            )
        } else {
            Log.e("kris", "${repo.name}: loaded ${users.size} contributors")
        }
    }

}