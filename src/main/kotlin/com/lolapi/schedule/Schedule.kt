package com.lolapi.schedule

import java.util.*

enum class MatchType {
    BO1,
    BO3,
    BO5
}

data class Game(
    val blueTeam: String,
    val redTeam: String,
    val winner: String = "TBD"
)

data class Match(
    val games: List<Game>,
    val type: MatchType,
    val team1: String,
    val team2: String
)

data class Day(
    val matches: List<Match>,
    val date: Date
)

data class Split(
    val startDate: Date,
    val endDate: Date,
    val days: List<Day>
)


