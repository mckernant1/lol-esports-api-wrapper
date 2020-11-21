package com.github.mckernant1.lol.heimerdinger.games

import com.beust.klaxon.Json
import kotlinx.serialization.Serializable

@Serializable
data class PerkMetaData(
    val styleId: Double,
    val subStyleId: Double,
    val perks: List<Double>
)

@Serializable
data class PlayerFrame(
    @Json("participantId") val playerId: Double,
    val level: Double,
    val kills: Double,
    val deaths: Double,
    val assists: Double,
    val creepScore: Double,
    val totalGoldEarned: Double,
    val killParticipation: Double,
    val championDamageShare: Double,
    val wardsPlaced: Double,
    val wardsDestroyed: Double,
    val attackDamage: Double,
    val abilityPower: Double,
    val criticalChance: Double,
    val attackSpeed: Double,
    val lifeSteal: Double,
    val armor: Double,
    val magicResistance: Double,
    val tenacity: Double,
    val items: List<Double>,
    val perkMetadata: PerkMetaData,
    val abilities: List<String>
)

@Serializable
data class DetailFrame(
    @Json("rfc460Timestamp") val timestamp: String,
    @Json("participants") val participantData: List<PlayerFrame>
)
