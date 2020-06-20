package com.github.mckernant1.lolapi.leagues

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import com.github.mckernant1.lolapi.EsportsApiHttpClient
import com.github.mckernant1.lolapi.config.EsportsApiConfig
import java.io.StringReader

class LeagueClient(
    esportsApiConfig: EsportsApiConfig = EsportsApiConfig()
) : EsportsApiHttpClient(esportsApiConfig) {

    /**
     * @return The list of all leagues pulled from the API
     */
    fun getLeagues(): List<League> {
        val res = super.get("getLeagues")
        return parser.parseJsonObject(StringReader(res))
            .obj("data")
            ?.array<JsonObject>("leagues")
            ?.mapChildrenObjectsOnly {
                return@mapChildrenObjectsOnly parser.parse<League>(it.toJsonString())
                    ?: throw KlaxonException("League parsing failed")
            }?.toList() ?: throw KlaxonException("Parsing Failed")
    }

    /**
     * Simple algorithm to remove whitespace from the names
     * @param name The names can be inconsistant in terms of formatting
     * @return The league with the closest name
     */
    fun getLeagueByName(name: String): League {
        return getLeagues().find {
            it.name.replace(" ", "").contains(name.replace(" ", ""), ignoreCase = true)
        } ?: throw NoSuchFieldException("There is no league with name '$name'")
    }

    /**
     * This algorithm attempts to find the league that's closest to the inputted slug by removing delimiters
     * @param slug The slug for the league These are wildly inconsistent in terms of formatting
     * @return The league closest to the slug
     */
    fun getLeagueBySlug(slug: String): League {
        return getLeagues().find {
            it.slug.replace(Regex("[-_ ]"), "")
                .contains(slug.replace(Regex("[-_ ]"), ""), ignoreCase = true)
        } ?: throw NoSuchFieldException("There is no league with name '$slug'")
    }

    companion object {
        private val parser = Klaxon()
    }
}
