package com.github.mckernant1.lol.heimerdinger.team

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    @SerialName("id") val playerId: String,
    val summonerName: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    @SerialName("image") val imageURL: String
) : java.io.Serializable

@Serializable
data class Team(
    val teamId: String,
    val slug: String,
    val name: String,
    val code: String,
    val homeLeagueCode: String,
    val players: List<Player>
) : java.io.Serializable
