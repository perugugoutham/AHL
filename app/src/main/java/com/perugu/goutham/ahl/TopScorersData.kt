package com.perugu.goutham.ahl

class TopScorersData : ArrayList<TopScorersDataItem>()

data class TopScorersDataItem(
    val goals: Int = 0,
    val player: Player = Player(),
    val team: Team = Team(),
    var category: Category = Category.DEFAULT
)