package com.perugu.goutham.ahl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.orhanobut.logger.Logger
import io.reactivex.disposables.CompositeDisposable

class AHLFragment: Fragment() {

    private val viewModel: AHLViewModel by viewModels()

    lateinit var compositeDisposable: CompositeDisposable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_ahl_layout, container, false)
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(viewModel.ahlDataStateStream.subscribe(this::DataReceivePlace))
    }

    override fun onStop() {
        super.onStop()
        if (!compositeDisposable.isDisposed){
            compositeDisposable.dispose()
        }
    }

    fun DataReceivePlace(ahlDataState: AHLDataState){
       Logger.wtf(ahlDataState.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchTournamentId()
    }
}