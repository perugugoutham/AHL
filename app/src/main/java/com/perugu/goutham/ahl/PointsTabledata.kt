package com.perugu.goutham.ahl

class PointsTableData : ArrayList<PointsTableDataItem>()

data class PointsTableDataItem(
    val draw: Int,
    val goalAgainst: Int,
    val goalDifference: Int,
    val goalScored: Int,
    val lost: Int,
    val matchesPlayed: Int,
    val points: Int,
    val position: Int,
    val team: Team,
    val won: Int
)

