package com.perugu.goutham.ahl

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient

class AHLViewModel(application: Application): AndroidViewModel(application) {

    var networkRequestRepo = NetworkRequestRepo(OkHttpClient.Builder().addInterceptor(ChuckInterceptor(application)).build(), Gson())

    fun fetchTournamentId(){
        networkRequestRepo.fetchTournamentId()
    }

}