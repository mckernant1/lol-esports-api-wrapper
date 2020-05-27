package com.lolapi.tournaments

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import com.lolapi.EsportsApiHttpClient
import com.lolapi.config.EsportsApiConfig
import java.io.StringReader

class TournamentClient(
    val esportsApiConfig: EsportsApiConfig = EsportsApiConfig()
) {
    val esportsApiHttpClient = EsportsApiHttpClient(esportsApiConfig)

    /**
     * TOURNAMENTS INCLUDE SPLITS + PLAYOFFS
     * @return Returns a list of all the tournaments for a specific league
     */
    fun getTournamentsForLeague(leagueId: String): List<Tournament> {
        val res = esportsApiHttpClient.get(
            "/getTournamentsForLeague",
            listOf(Pair("leagueId", leagueId))
        )
        return Klaxon().parseJsonObject(StringReader(res)).obj("data")
            ?.array<JsonObject>("leagues")?.get(0)
            ?.array<JsonObject>("tournaments")
            ?.mapChildrenObjectsOnly {
                return@mapChildrenObjectsOnly Klaxon().parse<Tournament>(it.toJsonString())
                    ?: throw KlaxonException("Tournament Parsing failed")
            }?.toList() ?: throw KlaxonException("Json parsing failed")
    }
}
