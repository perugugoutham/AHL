package com.perugu.goutham.ahl.view.fragments

import com.perugu.goutham.ahl.R
import com.perugu.goutham.ahl.view_model.AHLDataState

class MenHomeFragment: BaseHomeFragment() {

    override fun renderAhlDataState(newState: AHLDataState) {
        val fixtureDataMen = newState.fixtureDataMen
        if (oldState == null || oldState!!.fixtureDataMen != fixtureDataMen){
            renderPreviousMatchData(fixtureDataMen)
        }

        val fixtureForMenLoader = newState.loaderData.fixtureForMen
        if (oldState == null || oldState!!.loaderData.fixtureForMen != fixtureForMenLoader){
            renderPreviousMatchLoaderUI(fixtureForMenLoader)
        }

        val pointsTableDataMen = newState.pointsTableDataMen

        if (oldState == null || oldState!!.pointsTableDataMen != pointsTableDataMen){
            renderPointsTable(pointsTableDataMen)
        }

        val pointsForMenLoader = newState.loaderData.pointsForMen
        if (oldState == null || oldState!!.loaderData.pointsForMen != pointsForMenLoader){
            renderPointsTableLoaderUI(pointsForMenLoader)
        }

        val topScorersDataMen = newState.topScorersDataMen

        if (oldState == null || oldState!!.topScorersDataMen != topScorersDataMen){
            renderTopScorersData(topScorersDataMen)
        }

        val tableTopperForMenLoader = newState.loaderData.tableTopperForMen
        if (oldState == null || oldState!!.loaderData.tableTopperForMen != tableTopperForMenLoader){
            renderTopScorersLoaderUI(tableTopperForMenLoader)
        }

        oldState = newState
    }

    override fun getImageBasedOngender(): Int {
        return R.drawable.men_image
    }

}