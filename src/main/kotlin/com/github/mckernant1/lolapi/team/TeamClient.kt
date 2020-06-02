package com.github.mckernant1.lolapi.team

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import com.github.mckernant1.lolapi.EsportsApiHttpClient
import com.github.mckernant1.lolapi.config.EsportsApiConfig
import java.io.StringReader

class TeamClient(
    val esportsApiConfig: EsportsApiConfig = EsportsApiConfig()
) : EsportsApiHttpClient(esportsApiConfig) {

    /**
     * @return This list of all teams from the API
     */
    fun getAllTeams(): List<Team> {
        val teamsString = super.get("getTeams")
        val teamsJson = Klaxon().parseJsonObject(StringReader(teamsString))
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
        val teamJson = Klaxon().parseJsonObject(StringReader(teamString))
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
        return getAllTeams().find { it.code.toLowerCase() == code.toLowerCase() }
            ?: throw IllegalArgumentException("there is no team with code ${code.toLowerCase()}")
    }

    /**
     * Parses teams from json. Filters out TBD Teams
     */
    private fun parseTeams(teamsJson: JsonObject): List<Team> {
        return teamsJson.obj("data")
            ?.array<JsonObject>("teams")
            ?.mapChildrenObjectsOnly {
                if (it.string("id") == "0") {
                    return@mapChildrenObjectsOnly null
                }
                try {
                    return@mapChildrenObjectsOnly Team(
                        teamId = it.string("id")!!,
                        slug = it.string("slug")!!,
                        name = it.string("name")!!,
                        code = it.string("code")!!,
                        homeLeagueCode = it.obj("homeLeague")?.string("name") ?: "N/A",
                        players = parsePlayers(it)
                    )
                } catch (e: NullPointerException) {
                    throw KlaxonException("Error parsing team with Json ${it.toJsonString()}")
                }
            }?.toList()?.filterNotNull()?.filter { it.slug.toLowerCase() != "tbd" }
            ?: throw KlaxonException("Error parsing Json $teamsJson")
    }

    private fun parsePlayers(teamJson: JsonObject): List<Player> {
        return teamJson.array<JsonObject>("players")
            ?.mapChildrenObjectsOnly {
                return@mapChildrenObjectsOnly Klaxon().parse<Player>(it.toJsonString())
                    ?: throw KlaxonException("Error parsing player with jsonString ${it.toJsonString()}")
            }?.toList()
            ?: throw KlaxonException("Error parsing team with json $teamJson")
    }
}
