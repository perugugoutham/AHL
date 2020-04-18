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

    fun fetchTournamentId(){
        networkRequestRepo.fetchTournamentId(ahlDataStateStream)
    }

}

data class AHLDataState(
    val fixtureData: FixtureData = FixtureData(),
    val pointsTableData: PointsTableData = PointsTableData(),
    val topScorersData: TopScorersData = TopScorersData()
)