package com.github.mckernant1.lol.heimerdinger.tournaments

import com.github.mckernant1.lol.heimerdinger.EsportsApiHttpClient
import com.github.mckernant1.lol.heimerdinger.config.EsportsApiConfig
import com.github.mckernant1.lol.heimerdinger.leagues.LeagueClient
import com.github.mckernant1.lol.heimerdinger.schedule.ScheduleClient
import com.github.mckernant1.lol.heimerdinger.schedule.Split
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*

class TournamentClient(
    esportsApiConfig: EsportsApiConfig = EsportsApiConfig()
) : EsportsApiHttpClient(esportsApiConfig) {


    /**
     * @param leagueId league Id gotten from leagueClient
     * @return the most recent tournament. Includes ongoing tournaments
     */
    fun getMostRecentTournament(leagueId: String): Tournament {
        return getTournamentsForLeague(leagueId).maxByOrNull { it.startDate } ?:
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
        return parser.decodeFromString<JsonObject>(res)["data"]
            ?.jsonObject?.get("leagues")?.jsonArray?.get(0)
            ?.jsonObject?.get("tournaments")?.jsonArray
            ?.map {
                return@map parser.decodeFromJsonElement<Tournament>(it)
            }?.toList() ?: throw SerializationException("Json parsing failed for result: $res")
    }

    fun getStandingsForLeague(leagueId: String, splitYear: Int, splitNumber: Int? = null): List<Standing> {
        val split = scheduleClient.getSplitByYearAndNumber(leagueId, splitYear, splitNumber)
        return getStandingsBySplit(split)
    }
    fun getStandingsForMostRecentTournamentInLeague(leagueId: String): List<Standing> {
        val tournament = getMostRecentTournament(leagueId)
        val split = scheduleClient.getSplitByTournament(leagueId, tournament)

        return getStandingsBySplit(split)
    }

    fun getStandingsBySplit(split: Split): List<Standing> {
        val teams = mutableSetOf<String>()

        split.matches.forEach {
            teams.add(it.team1)
            teams.add(it.team2)
        }

        return teams.map {
            Standing(it,
                split.matches.count { match -> it == match.winner },
                split.matches.count { match -> (match.team1 == it || match.team2 == it) && it != match.winner && match.winner != "TBD"})
        }
    }


    fun getStandingsForLeagueByName(leagueName: String, splitYear: Int, splitNumber: Int? = null): List<Standing>  {
        val leagueClient = LeagueClient()
        val leagueId = leagueClient.getLeagueByName(leagueName).id
        return getStandingsForLeague(leagueId, splitYear, splitNumber)
    }

    companion object {
        private val parser = Json
        private val scheduleClient = ScheduleClient()
    }
}
