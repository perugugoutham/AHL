package com.perugu.goutham.ahl

import androidx.lifecycle.ViewModel
import okhttp3.OkHttpClient

class AHLViewModel: ViewModel() {

    var networkRequestRepo: NetworkRequestRepo = NetworkRequestRepo(OkHttpClient())

    fun fetchTournamentId(){
        networkRequestRepo.fetchTournamentId()
    }

}