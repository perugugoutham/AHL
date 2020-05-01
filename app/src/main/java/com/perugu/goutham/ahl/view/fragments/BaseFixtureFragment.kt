package com.perugu.goutham.ahl.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.perugu.goutham.ahl.R
import com.perugu.goutham.ahl.data.FixtureData
import com.perugu.goutham.ahl.view.adapters.FixtureDataAdapter
import com.perugu.goutham.ahl.view_model.AHLDataState
import com.perugu.goutham.ahl.view_model.AHLViewModel
import com.perugu.goutham.ahl.view_model.UIDataState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

abstract class BaseFixtureFragment: Fragment() {

    private val ahlViewModel by activityViewModels<AHLViewModel>()

    private lateinit var compositeDisposable: CompositeDisposable

    var oldState: AHLDataState? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fixture_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(ahlViewModel.ahlDataStateStream.observeOn(Schedulers.from(UIThreadExecutor())).subscribe(this::renderData))
    }

    abstract fun renderData(newState: AHLDataState)

    fun renderFixtureData(fixtureData: FixtureData) {
        val fixtureDataAdapter = FixtureDataAdapter()
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.fixture_data_recycler_view)
        recyclerView.adapter = fixtureDataAdapter
        fixtureDataAdapter.updateFixtureData(fixtureData)
    }

    fun renderFixtureLoaderUI(uidataState: UIDataState){
        when(uidataState){
            UIDataState.SHOW_LOADER -> {
                requireView().findViewById<View>(R.id.fixture_data_recycler_view).visibility = View.GONE
                requireView().findViewById<View>(R.id.match_shimmer).visibility = View.VISIBLE
            }

            UIDataState.SHOW_DATA -> {
                requireView().findViewById<View>(R.id.match_shimmer).visibility = View.GONE
                requireView().findViewById<View>(R.id.fixture_data_recycler_view).visibility = View.VISIBLE
            }

            UIDataState.SHOW_ERROR -> {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (!compositeDisposable.isDisposed){
            compositeDisposable.dispose()
        }
    }
}