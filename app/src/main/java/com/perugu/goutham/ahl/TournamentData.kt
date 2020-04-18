package com.perugu.goutham.ahl

import org.bson.types.ObjectId

data class TournamentData(
    val id: ObjectId,
    val live: Boolean,
    val season: String,
    val tagLine: String,
    val theme: String,
    val tournamentLogo: Any,
    val tournamentName: String,
    val tournamentType: String
)
