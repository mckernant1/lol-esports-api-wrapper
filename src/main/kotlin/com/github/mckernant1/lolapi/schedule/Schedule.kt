package com.github.mckernant1.lolapi.schedule

import java.util.*

/**
 * Match Type ENUM
 */
enum class MatchType(val numberOfGames: Int) {
    BO1(1),
    BO3(3),
    BO5(5)
}

/**
 * Simple Object to represent a match.
 * Does not give info about games in a BO3/BO5
 */
data class Match(
    val id: String,
    val gameIds: List<String>,
    val winner: String,
    val type: MatchType,
    val team1: String,
    val team2: String,
    val date: Date
)

/**
 * Has simple data on an entire split
 */
data class Split(
    val startDate: Date,
    val endDate: Date,
    val matches: List<Match>
)


