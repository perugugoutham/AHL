package com.perugu.goutham.ahl

class WomenHomeFragment: BaseHomeFragment() {

    override fun renderAhlDataState(newState: AHLDataState) {

        val fixtureDataWomen = newState.fixtureDataWomen

        if (oldState == null || oldState!!.fixtureDataWomen != fixtureDataWomen){
            renderPreviousMatchData(fixtureDataWomen)
        }

        val fixtureForWomenLoader = newState.loaderData.fixtureForWomen
        if (oldState == null || oldState!!.loaderData.fixtureForWomen != fixtureForWomenLoader){
            renderPreviousMatchLoaderUI(fixtureForWomenLoader)
        }

        val pointsTableDataWomen = newState.pointsTableDataWomen

        if (oldState == null || oldState!!.pointsTableDataWomen != pointsTableDataWomen){
            renderPointsTable(pointsTableDataWomen)
        }

        val pointsForWomenLoader = newState.loaderData.pointsTableForWomen
        if (oldState == null || oldState!!.loaderData.pointsTableForWomen != pointsForWomenLoader){
            renderPointsTableLoaderUI(pointsForWomenLoader)
        }

        val topScorersDataWomen = newState.topScorersDataWomen

        if (oldState == null || oldState!!.topScorersDataWomen != topScorersDataWomen){
            renderTopScorersData(topScorersDataWomen)
        }

        val tableTopperForWomenLoader = newState.loaderData.tableTopperForWomen
        if (oldState == null || oldState!!.loaderData.tableTopperForWomen != tableTopperForWomenLoader){
            renderTopScorersLoaderUI(tableTopperForWomenLoader)
        }

        oldState = newState
    }


}