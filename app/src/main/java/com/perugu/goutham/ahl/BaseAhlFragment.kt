package com.perugu.goutham.ahl

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor
import android.os.Looper



abstract class BaseAhlFragment: Fragment() {

    private val ahlViewModel by viewModels<AHLViewModel>()

    lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ahlViewModel.fetchTournamentId()
    }

    abstract override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(ahlViewModel.ahlDataStateStream
            /*.observeOn(AndroidSchedulers.mainThread())*/ //Unable to use this, as the package of Schedulers is different in rxRelay and rxAndroid
            .observeOn(Schedulers.from(UIThreadExecutor()))
            .subscribe(this::renderAhlDataState))
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable = CompositeDisposable()
    }

    override fun onStop() {
        super.onStop()
        if (!compositeDisposable.isDisposed){
            compositeDisposable.dispose()
        }
    }

    abstract fun renderAhlDataState(newState: AHLDataState)

    fun getTeamLogo(tag : TeamTag): Int{
        return when(tag){
            TeamTag.M_RED, TeamTag.W_RED -> R.drawable.ruffianz
            TeamTag.M_BLUE, TeamTag.W_BLUE -> R.drawable.bluz
            TeamTag.M_WHITE, TeamTag.W_WHITE -> R.drawable.griffinz
            TeamTag.M_YELLOW, TeamTag.W_YELLOW -> R.drawable.white
            TeamTag.M_GREEN, TeamTag.W_GREEN -> R.drawable.driblerz
            TeamTag.M_VIOLET, TeamTag.W_VIOLET -> R.drawable.yakz
            TeamTag.M_BLACK -> R.drawable.black
            TeamTag.OTHER -> R.drawable.black
        }
    }

}

internal class UIThreadExecutor : Executor {
    private val handler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
        handler.post(command)
    }
}