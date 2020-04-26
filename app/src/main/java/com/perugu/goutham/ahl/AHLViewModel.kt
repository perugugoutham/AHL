package com.perugu.goutham.ahl

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.readystatesoftware.chuck.ChuckInterceptor
import io.reactivex.disposables.Disposable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient

class AHLViewModel(application: Application): AndroidViewModel(application) {

    val ahlDataStateStream = BehaviorRelay.createDefault(AHLDataState())

    private val networkRequestStateStream = PublishRelay.create<NetworkRequestState>()

    private var disposable: Disposable? = null

    private var networkRequestRepo = NetworkRequestRepo(
        OkHttpClient.Builder().addInterceptor(ChuckInterceptor(application)).build(),
        Gson(),
        networkRequestStateStream
    )

    init {
        fetchTournamentId()
        subscribeToNetworkRequestState()
    }

    private fun fetchTournamentId(){
        networkRequestRepo.fetchTournamentId()
    }

    private fun subscribeToNetworkRequestState(){
        disposable = networkRequestStateStream.subscribe {
            when (it) {

                is Loading -> {
                    val updatedAhlDataState: AHLDataState = when (it.networkRequestRequests) {

                        NetworkRequestAction.FIXTURE_FOR_MEN -> {
                            val updatedLoaderData =
                                ahlDataStateStream.value!!.loaderData.copy(fixtureForMen = UIDataState.SHOW_LOADER)
                            ahlDataStateStream.value!!.copy(loaderData = updatedLoaderData)
                        }

                        NetworkRequestAction.FIXTURE_FOR_WOMEN -> {
                            val updatedLoaderData =
                                ahlDataStateStream.value!!.loaderData.copy(fixtureForWomen = UIDataState.SHOW_LOADER)
                            ahlDataStateStream.value!!.copy(loaderData = updatedLoaderData)
                        }

                        NetworkRequestAction.POINTS_TABLE_FOR_MEN -> {
                            val updatedLoaderData =
                                ahlDataStateStream.value!!.loaderData.copy(pointsForMen = UIDataState.SHOW_LOADER)
                            ahlDataStateStream.value!!.copy(loaderData = updatedLoaderData)
                        }

                        NetworkRequestAction.POINTS_TABLE_FOR_WOMEN -> {
                            val updatedLoaderData =
                                ahlDataStateStream.value!!.loaderData.copy(pointsTableForWomen = UIDataState.SHOW_LOADER)
                            ahlDataStateStream.value!!.copy(loaderData = updatedLoaderData)
                        }

                        NetworkRequestAction.TOP_SCORER_FOR_MEN -> {
                            val updatedLoaderData =
                                ahlDataStateStream.value!!.loaderData.copy(tableTopperForMen = UIDataState.SHOW_LOADER)
                            ahlDataStateStream.value!!.copy(loaderData = updatedLoaderData)
                        }

                        NetworkRequestAction.TOP_SCORER_FOR_WOMEN -> {
                            val updatedLoaderData =
                                ahlDataStateStream.value!!.loaderData.copy(tableTopperForWomen = UIDataState.SHOW_LOADER)
                            ahlDataStateStream.value!!.copy(loaderData = updatedLoaderData)
                        }

                        NetworkRequestAction.TOURNAMENT_ID -> {
                            val updatedLoaderData =
                                ahlDataStateStream.value!!.loaderData.copy(tournamentData = UIDataState.SHOW_LOADER)
                            ahlDataStateStream.value!!.copy(loaderData = updatedLoaderData)
                        }
                    }
                    ahlDataStateStream.accept(updatedAhlDataState)
                }

                is Success -> {
                    val data = it.data
                    val updatedAhlDataState: AHLDataState = when (data) {

                        is FixtureData -> {
                            if (data[0].category == Category.MEN) {
                                val updatedLoaderData =
                                    ahlDataStateStream.value!!.loaderData.copy(fixtureForMen = UIDataState.SHOW_DATA)
                                ahlDataStateStream.value!!.copy(
                                    loaderData = updatedLoaderData,
                                    fixtureDataMen = data
                                )
                            } else {
                                val updatedLoaderData =
                                    ahlDataStateStream.value!!.loaderData.copy(fixtureForWomen = UIDataState.SHOW_DATA)
                                ahlDataStateStream.value!!.copy(
                                    loaderData = updatedLoaderData,
                                    fixtureDataWomen = data
                                )
                            }
                        }

                        is PointsTableData -> {
                            if (data[0].category == Category.MEN) {
                                val updatedLoaderData =
                                    ahlDataStateStream.value!!.loaderData.copy(pointsForMen = UIDataState.SHOW_DATA)
                                ahlDataStateStream.value!!.copy(
                                    loaderData = updatedLoaderData,
                                    pointsTableDataMen = data
                                )
                            } else {
                                val updatedLoaderData =
                                    ahlDataStateStream.value!!.loaderData.copy(pointsTableForWomen = UIDataState.SHOW_DATA)
                                ahlDataStateStream.value!!.copy(
                                    loaderData = updatedLoaderData,
                                    pointsTableDataWomen = data
                                )
                            }
                        }

                        is TopScorersData -> {
                            if (data[0].category == Category.MEN) {
                                val updatedLoaderData =
                                    ahlDataStateStream.value!!.loaderData.copy(tableTopperForMen = UIDataState.SHOW_DATA)
                                ahlDataStateStream.value!!.copy(
                                    loaderData = updatedLoaderData,
                                    topScorersDataMen = data
                                )
                            } else {
                                val updatedLoaderData =
                                    ahlDataStateStream.value!!.loaderData.copy(tableTopperForWomen = UIDataState.SHOW_DATA)
                                ahlDataStateStream.value!!.copy(
                                    loaderData = updatedLoaderData,
                                    topScorersDataWomen = data
                                )
                            }
                        }

                        is TournamentData -> {
                            fetchOtherDataInMultipleThreads(data)
                            val updatedLoaderData =
                                ahlDataStateStream.value!!.loaderData.copy(tournamentData = UIDataState.SHOW_DATA)
                            ahlDataStateStream.value!!.copy(
                                loaderData = updatedLoaderData,
                                tournamentData = data
                            )
                        }

                        else -> {
                            ahlDataStateStream.value!!
                        }
                    }

                    ahlDataStateStream.accept(updatedAhlDataState)
                }

                is Failed -> {
                    val updatedAhlDataState: AHLDataState =
                        when (it.networkRequestRequests) {
                            NetworkRequestAction.FIXTURE_FOR_MEN -> {
                                val updatedLoaderData =
                                    ahlDataStateStream.value!!.loaderData.copy(fixtureForMen = UIDataState.SHOW_ERROR)
                                ahlDataStateStream.value!!.copy(loaderData = updatedLoaderData)
                            }

                            NetworkRequestAction.FIXTURE_FOR_WOMEN -> {
                                val updatedLoaderData =
                                    ahlDataStateStream.value!!.loaderData.copy(fixtureForMen = UIDataState.SHOW_ERROR)
                                ahlDataStateStream.value!!.copy(loaderData = updatedLoaderData)
                            }

                            NetworkRequestAction.POINTS_TABLE_FOR_MEN -> {
                                val updatedLoaderData =
                                    ahlDataStateStream.value!!.loaderData.copy(fixtureForMen = UIDataState.SHOW_ERROR)
                                ahlDataStateStream.value!!.copy(loaderData = updatedLoaderData)
                            }

                            NetworkRequestAction.POINTS_TABLE_FOR_WOMEN -> {
                                val updatedLoaderData =
                                    ahlDataStateStream.value!!.loaderData.copy(fixtureForMen = UIDataState.SHOW_ERROR)
                                ahlDataStateStream.value!!.copy(loaderData = updatedLoaderData)
                            }

                            NetworkRequestAction.TOP_SCORER_FOR_MEN -> {
                                val updatedLoaderData =
                                    ahlDataStateStream.value!!.loaderData.copy(fixtureForMen = UIDataState.SHOW_ERROR)
                                ahlDataStateStream.value!!.copy(loaderData = updatedLoaderData)
                            }

                            NetworkRequestAction.TOP_SCORER_FOR_WOMEN -> {
                                val updatedLoaderData =
                                    ahlDataStateStream.value!!.loaderData.copy(fixtureForMen = UIDataState.SHOW_ERROR)
                                ahlDataStateStream.value!!.copy(loaderData = updatedLoaderData)
                            }

                            NetworkRequestAction.TOURNAMENT_ID -> {
                                val updatedLoaderData =
                                    ahlDataStateStream.value!!.loaderData.copy(fixtureForMen = UIDataState.SHOW_ERROR)
                                ahlDataStateStream.value!!.copy(loaderData = updatedLoaderData)
                            }
                        }
                    ahlDataStateStream.accept(updatedAhlDataState)
                }
            }
        }
    }

    private fun fetchOtherDataInMultipleThreads(
        tournamentData: TournamentData
    ) {
        val list = NetworkRequestAction.values().toMutableList()
        list.reverse()
        Flowable.just(list)
            .flatMap {
                Flowable.fromIterable(it)
            }
            .parallel()
            .runOn(Schedulers.computation())
            .map {
                when(it!!){
                    NetworkRequestAction.FIXTURE_FOR_WOMEN -> {
                        networkRequestRepo.fetchFixtureData(
                            tournamentData.id,
                            Category.WOMEN
                        )
                    }

                    NetworkRequestAction.FIXTURE_FOR_MEN -> {
                        networkRequestRepo.fetchFixtureData(
                            tournamentData.id,
                            Category.MEN
                        )
                    }

                    NetworkRequestAction.POINTS_TABLE_FOR_MEN -> {
                        networkRequestRepo.fetchPointsTableData(
                            tournamentData.id,
                            Category.MEN
                        )
                    }
                    NetworkRequestAction.POINTS_TABLE_FOR_WOMEN -> {
                        networkRequestRepo.fetchPointsTableData(
                            tournamentData.id,
                            Category.WOMEN
                        )
                    }

                    NetworkRequestAction.TOP_SCORER_FOR_MEN -> {
                        networkRequestRepo.fetchTopScorers(
                            tournamentData.id,
                            Category.MEN
                        )
                    }
                    NetworkRequestAction.TOP_SCORER_FOR_WOMEN -> {
                        networkRequestRepo.fetchTopScorers(
                            tournamentData.id,
                            Category.WOMEN
                        )
                    }

                    NetworkRequestAction.TOURNAMENT_ID -> {

                    }
                }
            }
            .sequential()
            .blockingLast()
    }

    override fun onCleared() {
        super.onCleared()
        if (disposable != null && !disposable!!.isDisposed){
            disposable!!.dispose()
        }
    }

}

data class AHLDataState(
    //Maintaining men and women data separately is temp until server changes are done

    val loaderData: LoaderData = LoaderData(),

    val fixtureDataMen: FixtureData = FixtureData(),
    val fixtureDataWomen: FixtureData = FixtureData(),

    val pointsTableDataMen: PointsTableData = PointsTableData(),
    val pointsTableDataWomen: PointsTableData = PointsTableData(),

    val topScorersDataMen: TopScorersData = TopScorersData(),
    val topScorersDataWomen: TopScorersData = TopScorersData(),

    val tournamentData: TournamentData = TournamentData()
)


data class LoaderData(
    val fixtureForMen: UIDataState = UIDataState.SHOW_LOADER,
    val fixtureForWomen: UIDataState = UIDataState.SHOW_LOADER,
    val pointsForMen: UIDataState = UIDataState.SHOW_LOADER,
    val pointsTableForWomen : UIDataState = UIDataState.SHOW_LOADER,
    val tableTopperForMen : UIDataState = UIDataState.SHOW_LOADER,
    val tableTopperForWomen : UIDataState = UIDataState.SHOW_LOADER,
    val tournamentData: UIDataState = UIDataState.SHOW_LOADER
)

enum class NetworkRequestAction {

    FIXTURE_FOR_MEN,
    FIXTURE_FOR_WOMEN,

    POINTS_TABLE_FOR_MEN,
    POINTS_TABLE_FOR_WOMEN,

    TOP_SCORER_FOR_MEN,
    TOP_SCORER_FOR_WOMEN,

    TOURNAMENT_ID
}

sealed class NetworkRequestState
class Loading(val networkRequestRequests: NetworkRequestAction): NetworkRequestState()
class Success(val data: Any): NetworkRequestState()
class Failed(val networkRequestRequests: NetworkRequestAction, val statusCode: Int) : NetworkRequestState()

enum class UIDataState{
    SHOW_LOADER,
    SHOW_DATA,
    SHOW_ERROR,
}
