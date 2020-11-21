package com.github.mckernant1.lol.heimerdinger.tournaments

import kotlinx.serialization.Serializable

@Serializable
data class Standing(
    val teamName: String,
    val wins: Int,
    val losses: Int
)

@Serializable
data class Tournament(
    val id: String,
    val slug: String,
    val startDate: String,
    val endDate: String
)
