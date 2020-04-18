package com.perugu.goutham.ahl

class TopScorersData : ArrayList<TopScorersDataItem>()

data class TopScorersDataItem(
    val goals: Int,
    val player: Player,
    val team: Team,
    var category: Category = Category.MEN
)