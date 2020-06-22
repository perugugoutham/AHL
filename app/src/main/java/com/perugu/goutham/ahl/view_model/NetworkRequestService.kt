package com.perugu.goutham.ahl.view_model

import com.perugu.goutham.ahl.data.FixtureData
import com.perugu.goutham.ahl.data.PointsTableData
import com.perugu.goutham.ahl.data.TopScorersData
import com.perugu.goutham.ahl.data.TournamentData
import org.bson.types.ObjectId
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface NetworkRequestService {

    @GET("api/tournament")
    fun fetchTournamentDetails(@Query("season") season: String, @Query("type") type: String): Call<TournamentData>

    @GET("/api/matches")
    fun fetchFixtureData(@Query("category") category: String, @Query("tournament") id: ObjectId): Call<FixtureData>

    @GET("api/points")
    fun fetchPointsTable(@Query("category") category: String, @Query("tournament") id: ObjectId): Call<PointsTableData>

    @GET("api/topscorers/{id}")
    fun fetchTopScorers(@Path("id") id: ObjectId, @Query("category") category: String, @Query("count") count: String): Call<TopScorersData>
}