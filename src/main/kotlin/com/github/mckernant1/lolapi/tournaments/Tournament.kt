package com.github.mckernant1.lolapi.tournaments

data class Tournament(
    val id: String,
    val slug: String,
    val startDate: String,
    val endDate: String
)
