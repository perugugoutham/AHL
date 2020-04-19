package com.perugu.goutham.ahl

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.jakewharton.rxrelay2.BehaviorRelay
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient

class AHLViewModel(application: Application): AndroidViewModel(application) {

    val ahlDataStateStream = BehaviorRelay.createDefault(AHLDataState())

    private var networkRequestRepo = NetworkRequestRepo(OkHttpClient.Builder().addInterceptor(ChuckInterceptor(application)).build(), Gson())

    init {
        fetchTournamentId()
    }

    fun fetchTournamentId(){
        networkRequestRepo.fetchTournamentId(ahlDataStateStream)
    }

}

data class AHLDataState(
    //Maintaining men and women data separately is temp until server changes are done

    val fixtureDataMen: FixtureData = FixtureData(),
    val fixtureDataWomen: FixtureData = FixtureData(),

    val pointsTableDataMen: PointsTableData = PointsTableData(),
    val pointsTableDataWomen: PointsTableData = PointsTableData(),

    val topScorersDataMen: TopScorersData = TopScorersData(),
    val topScorersDataWomen: TopScorersData = TopScorersData()
)