package com.perugu.goutham.ahl

import org.bson.types.ObjectId

data class TournamentData(
    val id: ObjectId = ObjectId(),
    val live: Boolean = false,
    val season: String = "",
    val tagLine: String = "",
    val theme: String = "",
    val tournamentLogo: Any? = null,
    val tournamentName: String = "",
    val tournamentType: String = ""
)
