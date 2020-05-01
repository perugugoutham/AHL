package com.perugu.goutham.ahl.view.fragments

import com.perugu.goutham.ahl.view_model.AHLDataState

class MenFixtureFragment: BaseFixtureFragment() {
    override fun renderData(newState: AHLDataState) {
        val fixtureDataMen = newState.fixtureDataMen
        if (oldState == null || oldState!!.fixtureDataMen != fixtureDataMen){
            renderFixtureData(fixtureDataMen)
        }

        val fixtureForMenLoader = newState.loaderData.fixtureForMen
        if (oldState == null || oldState!!.loaderData.fixtureForMen != fixtureForMenLoader){
            renderFixtureLoaderUI(fixtureForMenLoader)
        }
    }
}