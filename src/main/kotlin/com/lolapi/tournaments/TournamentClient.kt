package com.lolapi.tournaments

import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import com.lolapi.EsportsApiHttpClient
import com.lolapi.config.EsportsApiConfig

class TournamentClient(
    val esportsApiConfig: EsportsApiConfig = EsportsApiConfig()
) {
    val esportsApiHttpClient = EsportsApiHttpClient(esportsApiConfig)


    fun getTournamentsForLeague(leagueId: String): List<Tournament> {
        val res = esportsApiHttpClient.get(
            "/getTournamentsForLeague",
            listOf(Pair("leagueId", leagueId))
        )
        val tournament = Klaxon().parse<TournamentDataWrapper>(res) ?: throw KlaxonException("Parsing failed")
        return tournament.data.leagues[0].tournaments
    }
}
