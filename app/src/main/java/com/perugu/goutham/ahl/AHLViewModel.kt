package com.perugu.goutham.ahl

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient

class AHLViewModel(application: Application): AndroidViewModel(application) {

    val ahlDataStateStream = BehaviorRelay.createDefault(AHLDataState())

    val uistateStream = PublishRelay.create<UIState>()

    private var networkRequestRepo = NetworkRequestRepo(OkHttpClient.Builder().addInterceptor(ChuckInterceptor(application)).build(), Gson())

    init {
        fetchTournamentId()
    }

    fun fetchTournamentId(){
        networkRequestRepo.fetchTournamentId(CallBackStreams(ahlDataStateStream, uistateStream))
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

sealed class UIState
object NETWORK_NOT_AVAILABLE : UIState()
object NETWORK_AVAILABLE : UIState()
data class NETWORK_REQUEST_FAILED(val value: String): UIState()

data class CallBackStreams(
    val ahlDataStateStream : BehaviorRelay<AHLDataState>,
    val errorState : PublishRelay<UIState>
)