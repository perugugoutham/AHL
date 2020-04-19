package com.perugu.goutham.ahl

import androidx.recyclerview.widget.RecyclerView

class MenHomeFragment: BaseHomeFragment() {

    override fun renderAhlDataState(newState: AHLDataState) {
        val fixtureDataMen = newState.fixtureDataMen
        if (oldState.fixtureDataMen != fixtureDataMen){
            renderPreviousMatchData(fixtureDataMen)
        }

        val pointsTableDataMen = newState.pointsTableDataMen

        if (oldState.pointsTableDataMen != pointsTableDataMen){
            renderPointsTable(pointsTableDataMen)
        }

        val topScorersDataMen = newState.topScorersDataMen

        if (oldState.topScorersDataMen != topScorersDataMen){
            renderTopScorersData(topScorersDataMen)
        }
    }

}