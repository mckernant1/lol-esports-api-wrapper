package com.lolapi.schedule

import java.util.*

/**
 * Match Type ENUM
 */
enum class MatchType {
    BO1,
    BO3,
    BO5
}

/**
 * Simple Object to represent a match.
 * Does not give info about games in a BO3/BO5
 */
data class Match(
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


