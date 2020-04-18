package com.perugu.goutham.ahl

import org.bson.types.ObjectId

class FixtureData : ArrayList<FixtureDataItem>()

data class FixtureDataItem(
    val buddingPlayer: Player,
    val id: ObjectId,
    val matchDateTime: Long,
    val mom: Player,
    val result: Int,
    val status: String,
    val team1: Team,
    val team1Scorers: Map<String, Int>, //Map of player and number of goals by player
    val team2: Team,
    val team2Scorers: Map<String, Int>, //Map of player and number of goals by player
    val tournamentId: ObjectId,
    var category: Category
)

data class Player(
    val id: ObjectId,
    val name: String,
    val position: Position
)

data class Team(
    val id: ObjectId,
    val name: String,
    val teamTag: String,
    val tournamentId: ObjectId
)

enum class Position(val value: String){
    FORWARD("Forward"),
    MIDFIELDER("Mid Fielder"),
    DEFENCE("Defence"),
    GOALKEEPER("Goal Keeper");
}


