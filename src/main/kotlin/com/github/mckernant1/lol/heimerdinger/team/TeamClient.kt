package com.github.mckernant1.lol.heimerdinger.team

import com.github.mckernant1.lol.heimerdinger.EsportsApiHttpClient
import com.github.mckernant1.lol.heimerdinger.config.EsportsApiConfig
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*

class TeamClient(
    esportsApiConfig: EsportsApiConfig = EsportsApiConfig()
) : EsportsApiHttpClient(esportsApiConfig) {

    /**
     * @return This list of all teams from the API
     */
    fun getAllTeams(): List<Team> {
        val teamsString = super.get("getTeams")
        val teamsJson = parser.decodeFromString<JsonObject>(teamsString)
        return parseTeams(teamsJson)
    }

    /**
     * WARNING Slugs are inconsistent, but this will pull less from the API
     * if you know the exact slug you want
     * @param slug The slug of the desired team
     */
    fun getTeamBySlug(slug: String): Team {
        val teamString = super.get(
            "getTeams",
            listOf(Pair("id", slug))
        )
        val teamJson = parser.decodeFromString<JsonObject>(teamString)
        return parseTeams(teamJson).find { it.slug == slug }
            ?: throw IllegalArgumentException("Team with this slug does not exist")
    }

    /**
     * Get team by the 2 or 3 Character code
     * Slower then {@link #getTeamBySlug(String) getTeamBySlug} because it pulls all teams and then filters
     * @param code The caseless team code
     * @return The team
     */
    fun getTeamByCode(code: String): Team {
        return getAllTeams().find { it.code.equals(code, ignoreCase = true) }
            ?: throw IllegalArgumentException("there is no team with code ${code.toLowerCase()}")
    }

    /**
     * Parses teams from json. Filters out TBD Teams
     */
    private fun parseTeams(teamsJson: JsonObject): List<Team> {
        return teamsJson["data"]
            ?.jsonObject?.get("teams")
            ?.jsonArray
            ?.mapNotNull {
                if (it.jsonObject["id"]?.jsonPrimitive?.content == "0") {
                    return@mapNotNull null
                }
                val homeLeagueCode = try {
                    it.jsonObject["homeLeague"]?.jsonObject?.get("name")?.jsonPrimitive?.content ?: "N/A"
                } catch (e: java.lang.IllegalArgumentException) {
                    "N/A"
                }
                return@mapNotNull Team(
                    teamId = it.jsonObject["id"]?.jsonPrimitive?.content!!,
                    slug = it.jsonObject["slug"]?.jsonPrimitive?.content!!,
                    name = it.jsonObject["name"]?.jsonPrimitive?.content!!,
                    code = it.jsonObject["code"]?.jsonPrimitive?.content!!,
                    homeLeagueCode = homeLeagueCode,
                    players = parsePlayers(it.jsonObject)
                )
            }?.filter { it.slug.toLowerCase() != "tbd" }
            ?: throw SerializationException("Error parsing Json $teamsJson")
    }

    private fun parsePlayers(teamJson: JsonObject): List<Player> {
        return teamJson["players"]
            ?.jsonArray
            ?.map {
                return@map parser.decodeFromJsonElement<Player>(it)
            }?.toList()
            ?: throw SerializationException("Error parsing team with json $teamJson")
    }

    companion object {
        private val parser = Json
    }
}
