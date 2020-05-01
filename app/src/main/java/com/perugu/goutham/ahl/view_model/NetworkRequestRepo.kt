package com.perugu.goutham.ahl.view_model

import com.google.gson.Gson
import com.jakewharton.rxrelay2.PublishRelay
import com.orhanobut.logger.Logger
import com.perugu.goutham.ahl.data.FixtureData
import com.perugu.goutham.ahl.data.PointsTableData
import com.perugu.goutham.ahl.data.TopScorersData
import com.perugu.goutham.ahl.data.TournamentData
import okhttp3.*
import org.bson.types.ObjectId
import java.io.IOException

class NetworkRequestRepo(
    private val okHttpClient: OkHttpClient,
    val gson: Gson,
    val networkRequestStateStream: PublishRelay<NetworkRequestState>
) {

    private val baseUrl = "https://young-coast-02878.herokuapp.com/api"

    fun fetchTournamentId() {
        networkRequestStateStream.accept(Loading(NetworkRequestAction.TOURNAMENT_ID))
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

                    networkRequestStateStream.accept(
                        Success(
                            tournamentData
                        )
                    )

                }else {
                    Logger.e("TournamentId response code ${response.code}}")
                    networkRequestStateStream.accept(
                        Failed(
                            NetworkRequestAction.TOURNAMENT_ID,
                            response.code
                        )
                    )
                }

            }

        })
    }

    fun fetchFixtureData(
        id: ObjectId,
        category: Category
    ) {

        Logger.d("Fixture data fetch ${category.value}")

        if (category == Category.MEN){
            networkRequestStateStream.accept(Loading(NetworkRequestAction.FIXTURE_FOR_MEN))
        }else{
            networkRequestStateStream.accept(Loading(NetworkRequestAction.FIXTURE_FOR_WOMEN))
        }

        val url = "$baseUrl/matches?category=${category.value}&tournament=$id"

        val requestBuilder = Request.Builder()
        requestBuilder.url(url)

        val response = okHttpClient.newCall(requestBuilder.build()).execute()

        if (response.isSuccessful){
            val fixtureData = gson.fromJson<FixtureData>(response.body?.string(), FixtureData::class.java)

            //Todo this is temp until changes are made in API
            fixtureData.forEach {
                it.category = category
            }

            networkRequestStateStream.accept(Success(fixtureData))

        }else {
            Logger.e("Fixture data of ${category.value} fetch failed")
            if (category == Category.MEN){
                networkRequestStateStream.accept(
                    Failed(
                        NetworkRequestAction.FIXTURE_FOR_MEN,
                        response.code
                    )
                )
            }else{
                networkRequestStateStream.accept(
                    Failed(
                        NetworkRequestAction.FIXTURE_FOR_WOMEN,
                        response.code
                    )
                )
            }
        }
    }

    fun fetchPointsTableData(
        id: ObjectId,
        category: Category
    ) {
        val url = "$baseUrl/points?category=${category.value}&tournament=$id"

        Logger.d("PointsTable data fetch ${category.value}")

        if (category == Category.MEN){
            networkRequestStateStream.accept(Loading(NetworkRequestAction.POINTS_TABLE_FOR_MEN))
        }else{
            networkRequestStateStream.accept(Loading(NetworkRequestAction.POINTS_TABLE_FOR_WOMEN))
        }

        val requestBuilder = Request.Builder()
        requestBuilder.url(url)

        val response = okHttpClient.newCall(requestBuilder.build()).execute()

        if (response.isSuccessful){
            val pointsTableData = gson.fromJson<PointsTableData>(response.body?.string(), PointsTableData::class.java)

            pointsTableData.forEach {
                it.category = category
            }

            networkRequestStateStream.accept(
                Success(
                    pointsTableData
                )
            )

        }else {
            Logger.e("PointsTable data of ${category.value} fetch failed")
            if (category == Category.MEN){
                networkRequestStateStream.accept(
                    Failed(
                        NetworkRequestAction.POINTS_TABLE_FOR_MEN,
                        response.code
                    )
                )
            }else{
                networkRequestStateStream.accept(
                    Failed(
                        NetworkRequestAction.POINTS_TABLE_FOR_WOMEN,
                        response.code
                    )
                )
            }
        }
    }

    fun fetchTopScorers(
        id: ObjectId,
        category: Category
    ){
        val url = "$baseUrl/topscorers/$id?category=${category.value}&count=3"

        Logger.d("TopScorers data fetch ${category.value}")

        if (category == Category.MEN){
            networkRequestStateStream.accept(Loading(NetworkRequestAction.TOP_SCORER_FOR_MEN))
        }else{
            networkRequestStateStream.accept(Loading(NetworkRequestAction.TOP_SCORER_FOR_WOMEN))
        }

        val requestBuilder = Request.Builder()
        requestBuilder.url(url)

        val response = okHttpClient.newCall(requestBuilder.build()).execute()

        if (response.isSuccessful){
            val topScorersData = gson.fromJson<TopScorersData>(response.body?.string(), TopScorersData::class.java)

            topScorersData.forEach {
                it.category = category
            }

            networkRequestStateStream.accept(
                Success(
                    topScorersData
                )
            )

        }else {
            Logger.e("TopScorers data of ${category.value} fetch failed")
            if (category == Category.MEN){
                networkRequestStateStream.accept(
                    Failed(
                        NetworkRequestAction.TOP_SCORER_FOR_MEN,
                        response.code
                    )
                )
            }else {
                networkRequestStateStream.accept(
                    Failed(
                        NetworkRequestAction.TOP_SCORER_FOR_WOMEN,
                        response.code
                    )
                )
            }
        }
    }

}

enum class Category(val value: String){
    DEFAULT("DEFAULT"),
    MEN ("men"),
    WOMEN("women")
}