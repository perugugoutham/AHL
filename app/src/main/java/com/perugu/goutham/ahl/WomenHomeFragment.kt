package com.perugu.goutham.ahl

class WomenHomeFragment: BaseHomeFragment() {

    override fun renderAhlDataState(newState: AHLDataState) {

        val fixtureDataWomen = newState.fixtureDataWomen

        if (oldState.fixtureDataWomen != fixtureDataWomen){
            renderPreviousMatchData(fixtureDataWomen)
        }

        val pointsTableDataWomen = newState.pointsTableDataWomen

        if (oldState.pointsTableDataMen != pointsTableDataWomen){
            renderPointsTable(pointsTableDataWomen)
        }

        val topScorersDataWomen = newState.topScorersDataWomen

        if (oldState.topScorersDataWomen != topScorersDataWomen){
            renderTopScorersData(topScorersDataWomen)
        }
    }


}