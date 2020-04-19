package com.perugu.goutham.ahl

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*


abstract class BaseHomeFragment : Fragment() {

    private val ahlViewModel by viewModels<AHLViewModel>()

    lateinit var compositeDisposable: CompositeDisposable

    val oldState: AHLDataState = AHLDataState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ahlViewModel.fetchTournamentId()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            ahlViewModel.ahlDataStateStream
                /*.observeOn(AndroidSchedulers.mainThread())*/ //Unable to use this, as the package of Schedulers is different in rxRelay and rxAndroid
                .observeOn(Schedulers.from(UIThreadExecutor()))
                .subscribe(this::renderAhlDataState)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    abstract fun renderAhlDataState(newState: AHLDataState)

    fun renderPreviousMatchData(fixtureData: FixtureData) {

        fixtureData.sortByDescending {
            it.matchDateTime
        }

        val previousMatchFixtureData = fixtureData.find {
            it.status == Status.COMPLETED
        }

        val date = Date(previousMatchFixtureData!!.matchDateTime)
        val simpleDateFormat = SimpleDateFormat("dd.MMM.yyyy HH:mm a", Locale.US)

        requireView().findViewById<TextView>(R.id.pervious_match_date).text =
            simpleDateFormat.format(date)
        requireView().findViewById<TextView>(R.id.team1_name).text = previousMatchFixtureData.team1.name
        requireView().findViewById<TextView>(R.id.team2_name).text =
            previousMatchFixtureData.team2.name
        requireView().findViewById<TextView>(R.id.budding_player).text =
            previousMatchFixtureData.buddingPlayer.name
        requireView().findViewById<TextView>(R.id.man_of_match).text =
            previousMatchFixtureData.mom.name

        val team1Scorers = previousMatchFixtureData.team1Scorers
        requireView().findViewById<TextView>(R.id.team1_goals).text =
            team1Scorers.values.sum().toString()

        val team2Scorers = previousMatchFixtureData.team2Scorers
        requireView().findViewById<TextView>(R.id.team2_goals).text =
            team2Scorers.values.sum().toString()

        requireView().findViewById<ImageView>(R.id.team1_logo).setImageResource(
            getTeamLogo(
                previousMatchFixtureData.team1.teamTag
            )
        )
        requireView().findViewById<ImageView>(R.id.team2_logo).setImageResource(
            getTeamLogo(
                previousMatchFixtureData.team2.teamTag
            )
        )
    }

    fun renderPointsTable(pointsTableData: PointsTableData) {
        val pointsTableAdapter = PointsTableAdapter()
        pointsTableAdapter.updatePointsTabledata(pointsTableData)
        requireView().findViewById<RecyclerView>(R.id.points_table_recycler_view).adapter =
            pointsTableAdapter
    }
}

internal class UIThreadExecutor : Executor {
    private val handler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
        handler.post(command)
    }
}


//Extension function
fun getTeamLogo(tag: TeamTag): Int {
    return when (tag) {
        TeamTag.M_RED, TeamTag.W_RED -> R.drawable.ruffianz
        TeamTag.M_BLUE, TeamTag.W_BLUE -> R.drawable.bluz
        TeamTag.M_WHITE, TeamTag.W_WHITE -> R.drawable.griffinz
        TeamTag.M_YELLOW, TeamTag.W_YELLOW -> R.drawable.white
        TeamTag.M_GREEN, TeamTag.W_GREEN -> R.drawable.driblerz
        TeamTag.M_VIOLET, TeamTag.W_VIOLET -> R.drawable.yakz
        TeamTag.M_BLACK -> R.drawable.black
        TeamTag.OTHER -> R.drawable.black
    }
}