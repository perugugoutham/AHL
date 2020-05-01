package com.perugu.goutham.ahl.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.perugu.goutham.ahl.R
import com.perugu.goutham.ahl.view_model.AHLDataState
import com.perugu.goutham.ahl.view_model.AHLViewModel
import com.perugu.goutham.ahl.view_model.UIDataState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


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

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositeDisposable = CompositeDisposable()
        val swipeRefreshLayout = requireView().findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
        compositeDisposable.add(ahlViewModel.ahlDataStateStream
            .observeOn(Schedulers.from(UIThreadExecutor()))
            .subscribe {
                val textView = requireView().findViewById<TextView>(R.id.error_msg)
                if (oldState == null || oldState!!.loaderData.tournamentData != it.loaderData.tournamentData) {

                    when (it.loaderData.tournamentData) {

                        UIDataState.SHOW_LOADER -> {
                            textView.text = getString(R.string.loading)
                            textView.setBackgroundColor(ContextCompat.getColor(textView.context, R.color.orange))
                            textView.visibility = View.VISIBLE
                        }

                        UIDataState.SHOW_DATA -> {
                            swipeRefreshLayout.isRefreshing = false
                            textView.visibility = View.GONE
                        }

                        UIDataState.SHOW_ERROR -> {
                            textView.text = getString(R.string.something_went_wrong)
                            textView.setBackgroundColor(ContextCompat.getColor(textView.context, R.color.red))
                            textView.visibility = View.VISIBLE
                        }
                    }
                }
                oldState = it
            })

        val tabLayout = requireView().findViewById<TabLayout>(R.id.ahl_tab_layout)

        val viewPager = requireView().findViewById<ViewPager>(R.id.ahl_view_pager)

        viewPager.setOnTouchListener { v, event ->
            swipeRefreshLayout.isEnabled = false
            when (event?.action) {
                MotionEvent.ACTION_UP -> swipeRefreshLayout.isEnabled = true
            }
            false
        }

        tabLayout.setupWithViewPager(viewPager)

        val ahlTabAdapter = AhlTabAdapter(parentFragmentManager)

        val menHomeFragment = MenHomeFragment()
        ahlTabAdapter.addFragment(menHomeFragment)

        val womenHomeFragment = WomenHomeFragment()
        ahlTabAdapter.addFragment(womenHomeFragment)

        viewPager.adapter = ahlTabAdapter

        swipeRefreshLayout.setOnRefreshListener {
            ahlViewModel.fetchTournamentId()
        }

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