package com.perugu.goutham.ahl

import com.google.gson.Gson
import com.orhanobut.logger.Logger
import okhttp3.*
import org.bson.types.ObjectId
import java.io.IOException

class NetworkRequestRepo(val okHttpClient: OkHttpClient, val gson: Gson) {

    private val baseUrl = "https://young-coast-02878.herokuapp.com/api"

    fun fetchTournamentId(){
        val url = "${baseUrl}/tournament?season=2020&type=AHL"

        Logger.d("Tournament data fetch")

        val requestBuilder = Request.Builder()
        requestBuilder.url(url)
        okHttpClient.newCall(requestBuilder.build()).enqueue(object : Callback{

            override fun onFailure(call: Call, e: IOException) {
                Logger.e("TournamentId fetch failed")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200){
                    val responseString = response.body?.string()
                    val tournamentData = gson.fromJson<TournamentData>(responseString, TournamentData::class.java)
                    Logger.wtf("TournamentId  ${tournamentData.id}")

                    fetchFixtureData(tournamentData.id, Category.MEN)
                    fetchFixtureData(tournamentData.id, Category.WOMEN)

                    fetchPointsTableData(tournamentData.id, Category.MEN)
                    fetchPointsTableData(tournamentData.id, Category.WOMEN)

                    fetchTopScorers(tournamentData.id, Category.MEN)
                    fetchTopScorers(tournamentData.id, Category.WOMEN)

                }else {
                    Logger.e("TournamentId response code ${response.code}}")
                }

            }

        })
    }

    private fun fetchFixtureData(id: ObjectId, category: Category) {

        Logger.d("Fixture data fetch ${category.value}")

        val url = "$baseUrl/matches?category=${category.value}&tournament=$id"

        val requestBuilder = Request.Builder()
        requestBuilder.url(url)

        val response = okHttpClient.newCall(requestBuilder.build()).execute()

        if (response.isSuccessful){
            val fixtureData = gson.fromJson<FixtureData>(response.body?.string(), FixtureData::class.java)
            Logger.wtf("Fixture data of ${category.value} is ${fixtureData[0]}")
        }else {
            Logger.e("Fixture data of ${category.value} fetch failed")
        }
    }

    private fun fetchPointsTableData(id: ObjectId, category: Category) {
        val url = "$baseUrl/points?category=${category.value}&tournament=$id"

        Logger.d("PointsTable data fetch ${category.value}")

        val requestBuilder = Request.Builder()
        requestBuilder.url(url)

        val response = okHttpClient.newCall(requestBuilder.build()).execute()

        if (response.isSuccessful){
            val pointsTableData = gson.fromJson<PointsTableData>(response.body?.string(), PointsTableData::class.java)
            Logger.wtf("PointsTable data of ${category.value} is ${pointsTableData[0]}")
        }else {
            Logger.e("PointsTable data of ${category.value} fetch failed")
        }
    }

    private fun fetchTopScorers(id: ObjectId, category: Category){
        val url = "$baseUrl/topscorers/$id?category=${category.value}&count=3"

        Logger.d("TopScorers data fetch ${category.value}")

        val requestBuilder = Request.Builder()
        requestBuilder.url(url)

        val response = okHttpClient.newCall(requestBuilder.build()).execute()

        if (response.isSuccessful){
            val topScorersData = gson.fromJson<TopScorersData>(response.body?.string(), TopScorersData::class.java)
            Logger.wtf("TopScorers data of ${category.value} is ${topScorersData[0]}")
        }else {
            Logger.e("TopScorers data of ${category.value} fetch failed")
        }
    }

}

enum class Category(val value: String){
    MEN ("men"),
    WOMEN("women")
}