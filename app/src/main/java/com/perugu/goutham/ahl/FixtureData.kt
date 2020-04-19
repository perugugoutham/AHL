package com.perugu.goutham.ahl

import org.bson.types.ObjectId

class FixtureData : ArrayList<FixtureDataItem>()

data class FixtureDataItem(
    val buddingPlayer: Player,
    val id: ObjectId,
    val matchDateTime: Long,
    val mom: Player,
    val result: Int,
    val status: Status,
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
    val teamTag: TeamTag,
    val tournamentId: ObjectId
)

enum class Position(val value: String){
    FORWARD("Forward"),
    MIDFIELDER("Mid Fielder"),
    DEFENCE("Defence"),
    GOALKEEPER("Goal Keeper");
}

enum class Status(val value: String){
    COMPLETED("COMPLETED"),
    UPCOMING("UPCOMING")
}

enum class TeamTag (val value: String){
    M_RED("RR"),
    M_BLUE("SB"),
    M_WHITE("WW"),
    M_YELLOW("YY"),
    M_GREEN("GG"),
    M_VIOLET("VW"),
    W_RED("RR"),
    W_BLUE("SB"),
    W_WHITE("WW"),
    W_YELLOW("YY"),
    W_GREEN("GG"),
    W_VIOLET("VW"),
    M_BLACK("BH"),
    OTHER("OT")
}


