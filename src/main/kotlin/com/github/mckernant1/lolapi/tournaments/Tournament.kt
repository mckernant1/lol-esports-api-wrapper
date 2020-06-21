package com.github.mckernant1.lolapi.tournaments


data class Standing(
    val teamName: String,
    val wins: Int,
    val losses: Int
)

data class Tournament(
    val id: String,
    val slug: String,
    val startDate: String,
    val endDate: String
)
