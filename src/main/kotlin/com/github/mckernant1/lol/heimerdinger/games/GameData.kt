package com.github.mckernant1.lol.heimerdinger.games

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PlayerData(
    @SerialName("participantId") val playerId: Int,
    val summonerName: String,
    val championId: String,
    val role: String
)

@Serializable
data class TeamData(
    @SerialName("esportsTeamId") val teamId: String,
    @SerialName("participantMetadata") val players: List<PlayerData>
)

@Serializable
data class GameData(
    val patchVersion: String,
    @SerialName("blueTeamMetadata") val blueTeamData: TeamData,
    @SerialName("redTeamMetadata") val redTeamData: TeamData
)

@Serializable
data class Game(
    val data: GameData,
    val frames: List<Frame>
)
