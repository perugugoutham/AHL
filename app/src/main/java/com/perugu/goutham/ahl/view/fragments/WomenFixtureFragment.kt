package com.perugu.goutham.ahl.view.fragments

import com.perugu.goutham.ahl.view_model.AHLDataState

class WomenFixtureFragment: BaseFixtureFragment() {
    override fun renderData(newState: AHLDataState) {
        val fixtureDataWomen = newState.fixtureDataWomen
        if (oldState == null || oldState!!.fixtureDataWomen != fixtureDataWomen){
            renderFixtureData(fixtureDataWomen)
        }

        val fixtureForWomenLoader = newState.loaderData.fixtureForWomen
        if (oldState == null || oldState!!.loaderData.fixtureForWomen != fixtureForWomenLoader){
            renderFixtureLoaderUI(fixtureForWomenLoader)
        }
    }
}