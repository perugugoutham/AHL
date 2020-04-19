package com.perugu.goutham.ahl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class PointsTableAdapter : RecyclerView.Adapter<PointsTableAdapter.PointsTableHolder>() {

    private var pointsTableData: PointsTableData? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointsTableHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.points_table_layout, parent, false)
        return PointsTableHolder(view)
    }

    override fun getItemCount(): Int {
        return if (pointsTableData != null ) pointsTableData!!.count() else 7
    }

    override fun onBindViewHolder(holder: PointsTableHolder, position: Int) {
        if (pointsTableData != null){
            val pointsTableDataItem = pointsTableData!![position]
            holder.position.text = pointsTableDataItem.position.toString()

            val teamName = pointsTableDataItem.team.name
            val name = teamName
                .split(' ')
                .mapNotNull {
                    it.firstOrNull()?.toString()
                }
                .reduce { first, second ->
                    first.toUpperCase(Locale.US) + second.toUpperCase(Locale.US)
                }.take(2)

            holder.teamName.text = name
            holder.played.text = pointsTableDataItem.matchesPlayed.toString()
            holder.won.text = pointsTableDataItem.won.toString()
            holder.lost.text = pointsTableDataItem.lost.toString()
            holder.draw.text = pointsTableDataItem.draw.toString()
            holder.goalDifference.text = pointsTableDataItem.goalDifference.toString()
            holder.points.text = pointsTableDataItem.points.toString()
            holder.teamLogo.setImageResource(getTeamLogo(pointsTableDataItem.team.teamTag))
        }
    }

    fun updatePointsTabledata(pointsTableDataMen: PointsTableData) {
        this.pointsTableData = pointsTableDataMen
        notifyDataSetChanged()
    }

    inner class PointsTableHolder(layoutHolder: View) : RecyclerView.ViewHolder(layoutHolder) {
        val position: TextView = layoutHolder.findViewById(R.id.position)
        val teamName: TextView = layoutHolder.findViewById(R.id.team_name)
        val played: TextView = layoutHolder.findViewById(R.id.played)
        val won: TextView = layoutHolder.findViewById(R.id.won)
        val lost: TextView = layoutHolder.findViewById(R.id.lost)
        val draw: TextView = layoutHolder.findViewById(R.id.draw)
        val goalDifference: TextView = layoutHolder.findViewById(R.id.goal_difference)
        val points: TextView = layoutHolder.findViewById(R.id.points)
        val teamLogo: ImageView = layoutHolder.findViewById(R.id.team_logo)
    }
}

