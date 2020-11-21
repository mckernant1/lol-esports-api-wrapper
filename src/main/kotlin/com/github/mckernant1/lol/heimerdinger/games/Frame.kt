package com.github.mckernant1.lol.heimerdinger.games

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamPlayerFrame(
    @SerialName("participantId") val playerId: Int,
    val level: Int,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val creepScore: Int,
    val totalGold: Int,
    val currentHealth: Int,
    val maxHealth: Int
)

@Serializable
data class TeamFrame(
    val totalGold: Int,
    val inhibitors: Int,
    val towers: Int,
    val barons: Int,
    val totalKills: Int,
    val dragons: List<String>,
    @SerialName("participants") val teamPlayers: List<TeamPlayerFrame>
)

@Serializable
data class Frame(
    @SerialName("rfc460Timestamp") val timestamp: String,
    val gameState: String,
    val blueTeam: TeamFrame,
    val redTeam: TeamFrame
)
