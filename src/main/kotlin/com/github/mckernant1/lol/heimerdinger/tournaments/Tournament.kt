package com.github.mckernant1.lol.heimerdinger.tournaments

import java.io.Serializable


data class Standing(
    val teamName: String,
    val wins: Int,
    val losses: Int
) : Serializable

data class Tournament(
    val id: String,
    val slug: String,
    val startDate: String,
    val endDate: String
) : Serializable
