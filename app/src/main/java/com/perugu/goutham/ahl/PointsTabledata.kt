package com.perugu.goutham.ahl

class PointsTableData : ArrayList<PointsTableDataItem>()

data class PointsTableDataItem(
    val draw: Int = 0,
    val goalAgainst: Int = 0,
    val goalDifference: Int = 0,
    val goalScored: Int = 0,
    val lost: Int = 0,
    val matchesPlayed: Int = 0,
    val points: Int = 0,
    val position: Int = 0,
    val team: Team = Team(),
    val won: Int = 0,
    var category: Category = Category.DEFAULT
)

