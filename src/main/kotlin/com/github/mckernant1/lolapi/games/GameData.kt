package com.github.mckernant1.lolapi.games

import com.beust.klaxon.Json
import java.io.Serializable


data class PlayerData(
    @Json("participantId") val playerId: Int,
    val summonerName: String,
    val championId: String,
    val role: String
) : Serializable

data class TeamData(
    @Json("esportsTeamId") val teamId: String,
    @Json("participantMetadata") val players: List<PlayerData>
) : Serializable

data class GameData(
    val patchVersion: String,
    @Json("blueTeamMetadata") val blueTeamData: TeamData,
    @Json("redTeamMetadata") val redTeamData: TeamData
) : Serializable

data class Game(
    val data: GameData,
    val frames: List<Frame>
) : Serializable
