package com.perugu.goutham.ahl

import org.bson.types.ObjectId

class FixtureData : ArrayList<FixtureDataItem>()

data class FixtureDataItem(
    val buddingPlayer: Player = Player(),
    val id: ObjectId = ObjectId(),
    val matchDateTime: Long = 0L,
    val mom: Player = Player(),
    val result: Int = 0,
    val status: Status = Status.DEFAULT,
    val team1: Team = Team(),
    val team1Scorers: Map<String, Int> = mapOf(), //Map of player and number of goals by player
    val team2: Team = Team(),
    val team2Scorers: Map<String, Int> = mapOf(), //Map of player and number of goals by player
    val tournamentId: ObjectId = ObjectId(),
    var category: Category = Category.MEN
)

data class Player(
    val id: ObjectId = ObjectId(),
    val name: String = "",
    val position: Position = Position.DEFAULT
)

data class Team(
    val id: ObjectId = ObjectId(),
    val name: String = "",
    val teamTag: TeamTag = TeamTag.OTHER,
    val tournamentId: ObjectId = ObjectId()
)

enum class Position(val value: String){
    DEFAULT("DEFAULT"),
    FORWARD("Forward"),
    MIDFIELDER("Mid Fielder"),
    DEFENCE("Defence"),
    GOALKEEPER("Goal Keeper");
}

enum class Status(val value: String){
    DEFAULT("DEFAULT"),
    COMPLETED("COMPLETED"),
    UPCOMING("UPCOMING")
}

enum class TeamTag (val value: String){
    OTHER("OT"),
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
    M_BLACK("BH")
}


