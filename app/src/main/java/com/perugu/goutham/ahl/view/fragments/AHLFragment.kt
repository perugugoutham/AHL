package com.perugu.goutham.ahl.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.PagerAdapter
import com.perugu.goutham.ahl.view_model.AHLDataState
import com.perugu.goutham.ahl.view_model.AHLViewModel
import com.perugu.goutham.ahl.R
import com.perugu.goutham.ahl.view_model.UIDataState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_ahl_layout.*

class AHLFragment : Fragment() {

    private val ahlViewModel by activityViewModels<AHLViewModel>()

    private lateinit var compositeDisposable: CompositeDisposable

    private var oldState: AHLDataState? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_ahl_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(ahlViewModel.ahlDataStateStream
            .observeOn(Schedulers.from(UIThreadExecutor()))
            .subscribe {

                if (oldState == null || oldState!!.loaderData.tournamentData != it.loaderData.tournamentData) {

                    when (it.loaderData.tournamentData) {

                        UIDataState.SHOW_LOADER -> {

                        }

                        UIDataState.SHOW_DATA -> {

                        }

                        UIDataState.SHOW_ERROR -> {

                        }
                    }
                }
                oldState = it
            })

        ahl_tab_layout.setupWithViewPager(ahl_view_pager)
        val ahlTabAdapter = AhlTabAdapter(parentFragmentManager)

        val menHomeFragment = MenHomeFragment()
        ahlTabAdapter.addFragment(menHomeFragment)

        val womenHomeFragment = WomenHomeFragment()
        ahlTabAdapter.addFragment(womenHomeFragment)

        ahl_view_pager.adapter = ahlTabAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    inner class AhlTabAdapter internal constructor(fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val fragmentList = java.util.ArrayList<Fragment>()

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }

        internal fun addFragment(fragment: Fragment) {
            fragmentList.add(fragment)
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "MEN"
                1 -> "WOMEN"
                else -> null
            }
        }

    }
}