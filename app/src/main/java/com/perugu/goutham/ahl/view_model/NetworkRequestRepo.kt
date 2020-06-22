package com.perugu.goutham.ahl.view_model

import com.jakewharton.rxrelay2.PublishRelay
import com.orhanobut.logger.Logger
import com.perugu.goutham.ahl.data.TournamentData
import org.bson.types.ObjectId

class NetworkRequestRepo(
    private val networkRequestService: NetworkRequestService,
    val networkRequestStateStream: PublishRelay<NetworkRequestState>
) {

    fun fetchTournamentId() {
        networkRequestStateStream.accept(Loading(NetworkRequestAction.TOURNAMENT_ID))
        val call: retrofit2.Call<TournamentData> = networkRequestService.fetchTournamentDetails("2020", "AHL")
        call.enqueue(object : retrofit2.Callback<TournamentData> {
            override fun onFailure(call: retrofit2.Call<TournamentData>, t: Throwable) {
                networkRequestStateStream.accept(
                    Failed(
                        NetworkRequestAction.TOURNAMENT_ID,
                        -1
                    )
                )
            }

            override fun onResponse(
                call: retrofit2.Call<TournamentData>,
                response: retrofit2.Response<TournamentData>
            ) {
                if (response.code() == 200){
                    networkRequestStateStream.accept(
                        Success(
                            response.body()!!
                        )
                    )
                }else {
                    networkRequestStateStream.accept(
                        Failed(
                            NetworkRequestAction.TOURNAMENT_ID,
                            response.code()
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

        val response = networkRequestService.fetchFixtureData(category = category.value, id = id).execute()

        if (response.isSuccessful){

            response.body()!!.forEach {
                it.category = category
            }

            networkRequestStateStream.accept(Success(response.body()!!))
        }else {
            Logger.e("Fixture data of ${category.value} fetch failed")
            if (category == Category.MEN){
                networkRequestStateStream.accept(
                    Failed(
                        NetworkRequestAction.FIXTURE_FOR_MEN,
                        response.code()
                    )
                )
            }else{
                networkRequestStateStream.accept(
                    Failed(
                        NetworkRequestAction.FIXTURE_FOR_WOMEN,
                        response.code()
                    )
                )
            }
        }
    }

    fun fetchPointsTableData(
        id: ObjectId,
        category: Category
    ) {

        Logger.d("PointsTable data fetch ${category.value}")

        if (category == Category.MEN){
            networkRequestStateStream.accept(Loading(NetworkRequestAction.POINTS_TABLE_FOR_MEN))
        }else{
            networkRequestStateStream.accept(Loading(NetworkRequestAction.POINTS_TABLE_FOR_WOMEN))
        }

        val response = networkRequestService.fetchPointsTable(category = category.value, id = id).execute()

        if (response.isSuccessful){

            response.body()!!.forEach {
                it.category = category
            }

            networkRequestStateStream.accept(
                Success(
                    response.body()!!
                )
            )

        }else {
            Logger.e("PointsTable data of ${category.value} fetch failed")
            if (category == Category.MEN){
                networkRequestStateStream.accept(
                    Failed(
                        NetworkRequestAction.POINTS_TABLE_FOR_MEN,
                        response.code()
                    )
                )
            }else{
                networkRequestStateStream.accept(
                    Failed(
                        NetworkRequestAction.POINTS_TABLE_FOR_WOMEN,
                        response.code()
                    )
                )
            }
        }
    }

    fun fetchTopScorers(
        id: ObjectId,
        category: Category
    ){

        Logger.d("TopScorers data fetch ${category.value}")

        if (category == Category.MEN){
            networkRequestStateStream.accept(Loading(NetworkRequestAction.TOP_SCORER_FOR_MEN))
        }else{
            networkRequestStateStream.accept(Loading(NetworkRequestAction.TOP_SCORER_FOR_WOMEN))
        }

        val response = networkRequestService.fetchTopScorers(
            id = id,
            category = category.value,
            count = "3"
        ).execute()

        if (response.isSuccessful){

            response.body()!!.forEach {
                it.category = category
            }

            networkRequestStateStream.accept(
                Success(
                    response.body()!!
                )
            )

        }else {
            Logger.e("TopScorers data of ${category.value} fetch failed")
            if (category == Category.MEN){
                networkRequestStateStream.accept(
                    Failed(
                        NetworkRequestAction.TOP_SCORER_FOR_MEN,
                        response.code()
                    )
                )
            }else {
                networkRequestStateStream.accept(
                    Failed(
                        NetworkRequestAction.TOP_SCORER_FOR_WOMEN,
                        response.code()
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