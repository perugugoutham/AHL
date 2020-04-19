package com.perugu.goutham.ahl

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
    }

}