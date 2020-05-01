package com.perugu.goutham.ahl.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perugu.goutham.ahl.R
import com.perugu.goutham.ahl.data.FixtureData
import com.perugu.goutham.ahl.view.fragments.getTeamLogo
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class FixtureDataAdapter : RecyclerView.Adapter<FixtureDataAdapter.FixtureDataHolder>() {

    private var fixtureData: FixtureData? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixtureDataHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fixture_holder_layout, parent, false)
        return FixtureDataHolder(view)
    }

    override fun getItemCount(): Int {
        return fixtureData?.count() ?: 7
    }

    fun updateFixtureData(fixtureData: FixtureData) {
        this.fixtureData = fixtureData
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FixtureDataHolder, position: Int) {
        if (fixtureData != null) {
            val fixtureData = fixtureData!![position]
            val date = Date(fixtureData.matchDateTime)
            val simpleDateFormat = SimpleDateFormat("dd MMM yyyy - hh:mm a", Locale.US)

            holder.date_text_view.text =
                simpleDateFormat.format(date)
            holder.team1_name.text = fixtureData.team1.name
            holder.team2_name.text = fixtureData.team2.name
            holder.team1_goals.text = fixtureData.team1Scorers.values.sum().toString()
            holder.team2_goals.text = fixtureData.team2Scorers.values.sum().toString()

            Picasso.get()
                .load(getTeamLogo(fixtureData.team1.teamTag))
                .into(holder.team1_logo)

            Picasso.get()
                .load(getTeamLogo(fixtureData.team2.teamTag))
                .into(holder.team2_logo)
        }
    }

    inner class FixtureDataHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date_text_view = view.findViewById<TextView>(R.id.match_date)
        val team1_name = view.findViewById<TextView>(R.id.team1_name)
        val team2_name = view.findViewById<TextView>(R.id.team2_name)
        val team1_goals = view.findViewById<TextView>(R.id.team1_goals)
        val team2_goals = view.findViewById<TextView>(R.id.team2_goals)
        val team1_logo = view.findViewById<ImageView>(R.id.team1_logo)
        val team2_logo = view.findViewById<ImageView>(R.id.team2_logo)
    }
}