package com.github.mckernant1.lolapi.games

import com.beust.klaxon.Json


data class PlayerData(
    @Json("participantId") val playerId: Int,
    val summonerName: String,
    val championId: String,
    val role: String
)

data class TeamData(
    @Json("esportsTeamId") val teamId: String,
    @Json("participantMetadata") val players: List<PlayerData>
)

data class GameData(
    val patchVersion: String,
    @Json("blueTeamMetadata") val blueTeamData: TeamData,
    @Json("redTeamMetadata") val redTeamData: TeamData
)

data class Game(
    val data: GameData,
    val frames: List<Frame>
)