package com.github.mckernant1.lol.heimerdinger.games

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PlayerData(
    @SerialName("participantId") val playerId: Int,
    val summonerName: String,
    val championId: String,
    val role: String
) : java.io.Serializable

@Serializable
data class TeamData(
    @SerialName("esportsTeamId") val teamId: String,
    @SerialName("participantMetadata") val players: List<PlayerData>
) : java.io.Serializable

@Serializable
data class GameData(
    val patchVersion: String,
    @SerialName("blueTeamMetadata") val blueTeamData: TeamData,
    @SerialName("redTeamMetadata") val redTeamData: TeamData
) : java.io.Serializable

@Serializable
data class Game(
    val data: GameData,
    val frames: List<Frame>
) : java.io.Serializable
