package com.perugu.goutham.ahl.data

import com.perugu.goutham.ahl.view_model.Category

class TopScorersData : ArrayList<TopScorersDataItem>()

data class TopScorersDataItem(
    val goals: Int = 0,
    val player: Player = Player(),
    val team: Team = Team(),
    var category: Category = Category.DEFAULT
)