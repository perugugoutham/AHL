package com.perugu.goutham.ahl

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import okhttp3.OkHttpClient

class AHLViewModel: ViewModel() {

    var networkRequestRepo: NetworkRequestRepo = NetworkRequestRepo(OkHttpClient(), Gson())

    fun fetchTournamentId(){
        networkRequestRepo.fetchTournamentId()
    }

}