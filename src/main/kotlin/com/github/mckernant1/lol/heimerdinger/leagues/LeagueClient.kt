package com.github.mckernant1.lol.heimerdinger.leagues

import com.github.mckernant1.lol.heimerdinger.EsportsApiHttpClient
import com.github.mckernant1.lol.heimerdinger.config.EsportsApiConfig
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*

class LeagueClient(
    esportsApiConfig: EsportsApiConfig = EsportsApiConfig()
) : EsportsApiHttpClient(esportsApiConfig) {

    /**
     * @return The list of all leagues pulled from the API
     */
    fun getLeagues(): List<League> {
        val res = super.get("getLeagues")
        println(res)
        return parser.decodeFromString<JsonObject>(res)["data"]?.jsonObject?.get("leagues")?.jsonArray
            ?.map { parser.decodeFromJsonElement(it.jsonObject) }
            ?: throw SerializationException()
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
        private val parser = Json
    }
}
