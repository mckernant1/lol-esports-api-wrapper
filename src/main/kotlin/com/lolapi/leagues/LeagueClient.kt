package com.lolapi.leagues

import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import com.lolapi.EsportsApiHttpClient
import com.lolapi.config.EsportsApiConfig

class LeagueClient(
    val esportsApiConfig: EsportsApiConfig = EsportsApiConfig()
) {
    val esportsApiHttpClient = EsportsApiHttpClient(esportsApiConfig)

    fun getLeagues(): List<League> {
        val res = esportsApiHttpClient.get("getLeagues")
        val leagues = Klaxon().parse<LeagueDataWrapper>(res)
            ?: throw KlaxonException("Parsing Failed")
        return leagues.data.leagues
    }

    fun getLeagueByName(name: String): League {
        return getLeagues().find { it.name == name }
            ?: throw NoSuchFieldException("There is no league with name '$name'")
    }

    fun getLeagueBySlug(slug: String): League {
        return getLeagues().find { it.slug == slug }
            ?: throw NoSuchFieldException("There is no league with name '$slug'")
    }
}
