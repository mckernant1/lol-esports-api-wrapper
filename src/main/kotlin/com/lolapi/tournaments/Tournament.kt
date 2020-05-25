package com.lolapi.tournaments

data class Tournament(
    val id: String,
    val slug: String,
    val startDate: String,
    val endDate: String
)

data class LeagueTournamentsWrapper(
    val tournaments: List<Tournament>
)

data class TournamentLeagueWrapper(
    val leagues: List<LeagueTournamentsWrapper>
)

data class TournamentDataWrapper(
    val data: TournamentLeagueWrapper
)
