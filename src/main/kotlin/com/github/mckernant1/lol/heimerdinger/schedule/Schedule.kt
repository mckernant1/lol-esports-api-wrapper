package com.github.mckernant1.lol.heimerdinger.schedule

import java.io.Serializable
import java.time.ZonedDateTime

/**
 * Match Type ENUM
 */
enum class MatchType(val numberOfGames: Int) {
    BO1(1),
    BO3(3),
    BO5(5),
    UNKNOWN(0)
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
    val team1NumWins: Int,
    val team2NumWins: Int,
    val team1: String,
    val team2: String,
    val date: ZonedDateTime
) : Serializable

/**
 * Has simple data on an entire split
 */
data class Split(
    val startDate: ZonedDateTime,
    val endDate: ZonedDateTime,
    val matches: List<Match>
) : Serializable


