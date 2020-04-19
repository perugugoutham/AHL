package com.perugu.goutham.ahl

import com.google.gson.Gson
import com.orhanobut.logger.Logger
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.*
import org.bson.types.ObjectId
import java.io.IOException

class NetworkRequestRepo(
    private val okHttpClient: OkHttpClient,
    val gson: Gson
) {

    private val baseUrl = "https://young-coast-02878.herokuapp.com/api"

    fun fetchTournamentId(callBackStreams: CallBackStreams) {
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

                    fetchOtherDataInMultipleThreads(tournamentData, callBackStreams)

                }else {
                    Logger.e("TournamentId response code ${response.code}}")
                    callBackStreams.errorState.accept(NETWORK_REQUEST_FAILED("TournamentId response code ${response.code}}"))
                }

            }

        })
    }

    private fun fetchOtherDataInMultipleThreads(
        tournamentData: TournamentData,
        callBackStreams: CallBackStreams
    ) {
        val list = NetworkRequests.values().toMutableList()
        list.reverse()
        Flowable.just(list)
            .flatMap {
                Flowable.fromIterable(it)
            }
            .parallel()
            .runOn(Schedulers.computation())
            .map {
                when(it!!){
                    NetworkRequests.FIXTURE_FOR_WOMEN -> fetchFixtureData(
                        tournamentData.id,
                        Category.WOMEN,
                        callBackStreams
                    )

                    NetworkRequests.FIXTURE_FOR_MEN -> fetchFixtureData(
                        tournamentData.id,
                        Category.MEN,
                        callBackStreams
                    )

                    NetworkRequests.POINTS_TABLE_FOR_MEN -> fetchPointsTableData(
                        tournamentData.id,
                        Category.MEN,
                        callBackStreams
                    )
                    NetworkRequests.POINTS_TABLE_FOR_WOMEN -> fetchPointsTableData(
                        tournamentData.id,
                        Category.WOMEN,
                        callBackStreams
                    )

                    NetworkRequests.TOP_SCORER_FOR_MEN -> fetchTopScorers(
                        tournamentData.id,
                        Category.MEN,
                        callBackStreams
                    )
                    NetworkRequests.TOP_SCORER_FOR_WOMEN -> fetchTopScorers(
                        tournamentData.id,
                        Category.WOMEN,
                        callBackStreams
                    )
                }
            }
            .sequential()
            .blockingLast()
    }

    private fun fetchFixtureData(
        id: ObjectId,
        category: Category,
        callBackStreams: CallBackStreams
    ) {

        Logger.d("Fixture data fetch ${category.value}")

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

            val ahlDataState: AHLDataState
            if (category == Category.MEN){
                ahlDataState = callBackStreams.ahlDataStateStream.value!!.copy(fixtureDataMen = fixtureData)
            }else {
                ahlDataState = callBackStreams.ahlDataStateStream.value!!.copy(fixtureDataWomen = fixtureData)
            }
            callBackStreams.ahlDataStateStream.accept(ahlDataState)

        }else {
            Logger.e("Fixture data of ${category.value} fetch failed")
            callBackStreams.errorState.accept(NETWORK_REQUEST_FAILED("Fixture data of ${category.value} fetch failed"))
        }
    }

    private fun fetchPointsTableData(
        id: ObjectId,
        category: Category,
        callBackStreams: CallBackStreams
    ) {
        val url = "$baseUrl/points?category=${category.value}&tournament=$id"

        Logger.d("PointsTable data fetch ${category.value}")

        val requestBuilder = Request.Builder()
        requestBuilder.url(url)

        val response = okHttpClient.newCall(requestBuilder.build()).execute()

        if (response.isSuccessful){
            val pointsTableData = gson.fromJson<PointsTableData>(response.body?.string(), PointsTableData::class.java)

            pointsTableData.forEach {
                it.category = category
            }

            val ahlDataState: AHLDataState
            if (category == Category.MEN){
                ahlDataState = callBackStreams.ahlDataStateStream.value!!.copy(pointsTableDataMen = pointsTableData)
            }else {
                ahlDataState = callBackStreams.ahlDataStateStream.value!!.copy(pointsTableDataWomen = pointsTableData)
            }
            callBackStreams.ahlDataStateStream.accept(ahlDataState)

        }else {
            Logger.e("PointsTable data of ${category.value} fetch failed")
            callBackStreams.errorState.accept(NETWORK_REQUEST_FAILED("PointsTable data of ${category.value} fetch failed"))
        }
    }

    private fun fetchTopScorers(
        id: ObjectId,
        category: Category,
        callBackStreams: CallBackStreams
    ){
        val url = "$baseUrl/topscorers/$id?category=${category.value}&count=3"

        Logger.d("TopScorers data fetch ${category.value}")

        val requestBuilder = Request.Builder()
        requestBuilder.url(url)

        val response = okHttpClient.newCall(requestBuilder.build()).execute()

        if (response.isSuccessful){
            val topScorersData = gson.fromJson<TopScorersData>(response.body?.string(), TopScorersData::class.java)

            topScorersData.forEach {
                it.category = category
            }

            val ahlDataState: AHLDataState
            if (category == Category.MEN){
                ahlDataState = callBackStreams.ahlDataStateStream.value!!.copy(topScorersDataMen = topScorersData)
            }else {
                ahlDataState = callBackStreams.ahlDataStateStream.value!!.copy(topScorersDataWomen = topScorersData)
            }
            callBackStreams.ahlDataStateStream.accept(ahlDataState)

        }else {
            Logger.e("TopScorers data of ${category.value} fetch failed")
            callBackStreams.errorState.accept(NETWORK_REQUEST_FAILED("TopScorers data of ${category.value} fetch failed"))
        }
    }

}

enum class NetworkRequests {

    FIXTURE_FOR_MEN,
    FIXTURE_FOR_WOMEN,

    POINTS_TABLE_FOR_MEN,
    POINTS_TABLE_FOR_WOMEN,

    TOP_SCORER_FOR_MEN,
    TOP_SCORER_FOR_WOMEN
}

enum class Category(val value: String){
    MEN ("men"),
    WOMEN("women")
}