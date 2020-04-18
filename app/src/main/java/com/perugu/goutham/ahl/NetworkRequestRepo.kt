package com.perugu.goutham.ahl

import okhttp3.*
import timber.log.Timber
import java.io.IOException

class NetworkRequestRepo(val okHttpClient: OkHttpClient) {

    fun fetchTournamentId(){
        val url: String = "https://young-coast-02878.herokuapp.com/api/tournament?season=2020&type=AHL"

        val requestBuilder = Request.Builder()
        requestBuilder.url(url)
        okHttpClient.newCall(requestBuilder.build()).enqueue(object : Callback{

            override fun onFailure(call: Call, e: IOException) {
                Timber.e("TournamentId fetch failed")
            }

            override fun onResponse(call: Call, response: Response) {
                Timber.d("TournamnetId response ${response.body}")
            }

        })
    }

}