package com.github.mckernant1.lolapi.games

import com.beust.klaxon.Json


data class TeamPlayerFrame(
    @Json("participantId") val playerId: Int,
    val level: Int,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val creepScore: Int,
    val totalGold: Int,
    val currentHealth: Int,
    val maxHealth: Int
)


data class TeamFrame(
    val totalGold: Int,
    val inhibitors: Int,
    val towers: Int,
    val barons: Int,
    val totalKills: Int,
    val dragons: List<String>,
    @Json("participants") val teamPlayers: List<TeamPlayerFrame>
)

data class Frame(
    @Json("rfc460Timestamp") val timestamp: String,
    val gameState: String,
    val blueTeam: TeamFrame,
    val redTeam: TeamFrame
)