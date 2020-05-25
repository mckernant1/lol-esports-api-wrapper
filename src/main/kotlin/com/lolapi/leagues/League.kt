package com.lolapi.leagues

data class League(
    val name: String,
    val slug: String,
    val id: String,
    val image: String,
    val priority: Int,
    val region: String
)

data class LeagueList(
    val leagues: List<League>
)

data class LeagueDataWrapper(
    val data: LeagueList
)
