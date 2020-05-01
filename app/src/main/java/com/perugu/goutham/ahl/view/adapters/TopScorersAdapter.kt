package com.perugu.goutham.ahl.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perugu.goutham.ahl.R
import com.perugu.goutham.ahl.data.TopScorersData
import com.perugu.goutham.ahl.view.fragments.getTeamLogo

class TopScorersAdapter: RecyclerView.Adapter<TopScorersAdapter.TopScorersViewHolder>() {

    private var topScorersData: TopScorersData? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopScorersViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.top_scorers_layout, parent, false)
        return TopScorersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return topScorersData?.count() ?: 4
    }

    fun updateTopScorersData(topScorersData: TopScorersData){
        this.topScorersData = topScorersData
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TopScorersViewHolder, position: Int) {
        if (topScorersData != null){
            val topScorersDataItem = topScorersData!![position]
            holder.teamLogo.setImageResource(
                getTeamLogo(
                    topScorersDataItem.team.teamTag
                )
            )
            holder.playerName.text = topScorersDataItem.player.name
            holder.teamName.text = topScorersDataItem.team.name
            holder.goals.text = topScorersDataItem.goals.toString()
        }
    }

    inner class TopScorersViewHolder(view: View): RecyclerView.ViewHolder(view){
        val teamLogo: ImageView = view.findViewById(R.id.team_logo)
        val playerName: TextView = view.findViewById(R.id.player_name)
        val teamName: TextView = view.findViewById(R.id.team_name)
        val goals: TextView = view.findViewById(R.id.goals)
    }
}