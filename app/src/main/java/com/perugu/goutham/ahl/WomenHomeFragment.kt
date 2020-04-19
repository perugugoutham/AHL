package com.perugu.goutham.ahl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class WomenHomeFragment: BaseAhlFragment() {

    private val oldState: AHLDataState = AHLDataState()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_layout, container, false)
    }


    override fun renderAhlDataState(newState: AHLDataState) {
        val fixtureDataWomen = newState.fixtureDataWomen
        if (oldState.fixtureDataWomen != fixtureDataWomen){

            fixtureDataWomen.sortByDescending {
                it.matchDateTime
            }

            val previousMatchFixtureData = fixtureDataWomen.find {
                it.status == Status.COMPLETED
            }

            val date = Date(previousMatchFixtureData!!.matchDateTime)
            val simpleDateFormat = SimpleDateFormat("dd.MMM.yyyy HH:mm a", Locale.US)

            requireView().findViewById<TextView>(R.id.pervious_match_date).text = simpleDateFormat.format(date)
            requireView().findViewById<TextView>(R.id.team1_name).text = previousMatchFixtureData.team1.name
            requireView().findViewById<TextView>(R.id.team2_name).text = previousMatchFixtureData.team2.name
            requireView().findViewById<TextView>(R.id.budding_player).text = previousMatchFixtureData.buddingPlayer.name
            requireView().findViewById<TextView>(R.id.man_of_match).text = previousMatchFixtureData.mom.name

            val team1Scorers = previousMatchFixtureData.team1Scorers
            requireView().findViewById<TextView>(R.id.team1_goals).text = team1Scorers.values.sum().toString()

            val team2Scorers = previousMatchFixtureData.team2Scorers
            requireView().findViewById<TextView>(R.id.team2_goals).text = team2Scorers.values.sum().toString()

            requireView().findViewById<ImageView>(R.id.team1_logo).setImageResource(getTeamLogo(previousMatchFixtureData.team1.teamTag))
            requireView().findViewById<ImageView>(R.id.team2_logo).setImageResource(getTeamLogo(previousMatchFixtureData.team2.teamTag))

        }
    }

}