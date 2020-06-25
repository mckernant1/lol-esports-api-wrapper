package com.github.mckernant1.lolapi.tournaments

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import com.github.mckernant1.lolapi.EsportsApiHttpClient
import com.github.mckernant1.lolapi.config.EsportsApiConfig
import com.github.mckernant1.lolapi.leagues.LeagueClient
import com.github.mckernant1.lolapi.schedule.ScheduleClient
import java.io.StringReader

class TournamentClient(
    esportsApiConfig: EsportsApiConfig = EsportsApiConfig()
) : EsportsApiHttpClient(esportsApiConfig) {


    /**
     * @param leagueId league Id gotten from leagueClient
     * @return the most recent tournament. Includes ongoing tournaments
     */
    fun getMostRecentTournament(leagueId: String): Tournament {
        return getTournamentsForLeague(leagueId).maxBy { it.startDate } ?:
                throw NullPointerException("No tournaments in this league")
    }

    /**
     * TOURNAMENTS INCLUDE SPLITS + PLAYOFFS
     * @param leagueId league Id gotten from leagueClient
     * @return Returns a list of all the tournaments for a specific league
     */
    fun getTournamentsForLeague(leagueId: String): List<Tournament> {
        val res = super.get(
            "/getTournamentsForLeague",
            listOf(Pair("leagueId", leagueId))
        )
        return parser.parseJsonObject(StringReader(res)).obj("data")
            ?.array<JsonObject>("leagues")?.get(0)
            ?.array<JsonObject>("tournaments")
            ?.mapChildrenObjectsOnly {
                return@mapChildrenObjectsOnly parser.parse<Tournament>(it.toJsonString())
                    ?: throw KlaxonException("Tournament Parsing failed for tournament: $it")
            }?.toList() ?: throw KlaxonException("Json parsing failed for result: $res")
    }

    fun getStandingsForLeague(leagueId: String, splitYear: Int, splitNumber: Int? = null): List<Standing> {
        val split = scheduleClient.getSplitByYearAndNumber(leagueId, splitYear, splitNumber)
        val teams = mutableSetOf<String>()

        split.matches.forEach {
            teams.add(it.team1)
            teams.add(it.team2)
        }

        return teams.map {
            Standing(it,
                split.matches.count { match -> it == match.winner },
                split.matches.count { match -> (match.team1 == it || match.team2 == it) && it != match.winner && match.winner != "TBD"})
        }.also { println(it) }
    }

    fun getStandingsForLeagueByName(leagueName: String, splitYear: Int, splitNumber: Int? = null): List<Standing>  {
        val leagueClient = LeagueClient()
        val leagueId = leagueClient.getLeagueByName(leagueName).id
        return getStandingsForLeague(leagueId, splitYear, splitNumber)
    }

    companion object {
        private val parser = Klaxon()
        private val scheduleClient = ScheduleClient()
    }
}
