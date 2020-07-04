package com.github.mckernant1.lolapi.schedule

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.github.mckernant1.lolapi.EsportsApiHttpClient
import com.github.mckernant1.lolapi.config.EsportsApiConfig
import com.github.mckernant1.lolapi.tournaments.Tournament
import com.github.mckernant1.lolapi.tournaments.TournamentClient
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.StringReader
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ScheduleClient(
        val esportsApiConfig: EsportsApiConfig = EsportsApiConfig()
) : EsportsApiHttpClient(esportsApiConfig) {

    /**
     * @param leagueId The Id of the league gotten from the leagueClient
     * @param splitYear The year of the split
     * @param splitNumber The number of the split (1 Spring, 2 Summer)
     */
    fun getSplitByYearAndNumber(leagueId: String, splitYear: Int, splitNumber: Int? = null): Split {
        return getSplitByTournament(leagueId, getTourneyForSplit(leagueId, splitYear, splitNumber))
    }

    /**
     * @param leagueId The Id of the league gotten from the leagueClient
     * @param tourney The tournament gotten from the TournamentClient
     * @return The split
     */
    fun getSplitByTournament(leagueId: String, tourney: Tournament): Split {
        val split = super.get(
                "getSchedule",
                listOf(
                        Pair("leagueId", leagueId)
                )
        )
        val json: JsonObject = parser.parseJsonObject(StringReader(split))
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val startDate = formatter.parse(tourney.startDate)
        val endDate = formatter.parse(tourney.endDate)
        var matches = parseMatches(json).filter {
            it.date > startDate &&
                    it.date < endDate
        }.toMutableList()
        var prevJson = json
        while (matches.find { it.date < startDate } == null) {
            val prevPageToken = (prevJson.obj("data")
                    ?.obj("schedule")
                    ?.obj("pages")
                    ?.string("older") ?: break)
            val prevPage = super.get(
                    "getSchedule",
                    listOf(
                            Pair("leagueId", leagueId),
                            Pair("pageToken", prevPageToken)
                    )
            )
            prevJson = parser.parseJsonObject(StringReader(prevPage))
            val newMatches = parseMatches(prevJson).filter {
                it.date > startDate &&
                        it.date < endDate
            }
            matches.addAll(newMatches)
        }
        matches = matches.filter {
            it.date > startDate && it.date < endDate
        }.toMutableList()
        return Split(startDate, endDate, matches)
    }

    /*
     * Helper method to get a tournament for a specific split
     */
    private fun getTourneyForSplit(leagueId: String, splitYear: Int, splitNumber: Int? = null): Tournament {
        val tournamentClient = TournamentClient()
        val splitNumberTmp = splitNumber ?: getCurrentSplitNumber()
        val slugTest = mutableListOf<String>()
        when (splitNumberTmp) {
            1 -> {
                slugTest.add("spring")
                slugTest.add("split1")
            }
            2 -> {
                slugTest.add("summer")
                slugTest.add("split2")
            }
        }
        val tourneys = tournamentClient.getTournamentsForLeague(leagueId)
        return tourneys.find {
            return@find it.slug.contains(splitYear.toString())
                    && slugTest.fold(false) { acc, s ->
                acc || it.slug.contains(s)
            }
        } ?: tourneys.find {
            return@find it.slug.contains(splitYear.toString())
        } ?: throw NoSuchFieldError("No tournament found for $leagueId, $splitYear, $splitNumber")
    }

    private fun getCurrentSplitNumber(): Int {
        val month = Calendar.getInstance().get(Calendar.MONTH) + 1
        return if (month in 1..6) 1 else 2
    }

    /*
     * Helper method to parse JSON
     */
    private fun parseMatches(json: JsonObject): List<Match> {
        return runBlocking {
            return@runBlocking json
                .obj("data")
                ?.obj("schedule")
                ?.array<JsonObject>("events")
                ?.mapChildrenObjectsOnly { event: JsonObject ->
                    async { eventToMatch(event) }
                }!!.mapNotNull { it.await() }.toList()
        }
    }

    /*
     * Async method to parse an event to a match. Also retrieves the gameIds
     */
    private suspend fun eventToMatch(event: JsonObject): Match? {
        lateinit var date: Date
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            date = formatter.parse(event.string("startTime"))
        } catch (e: ParseException) {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            date = formatter.parse(event.string("startTime"))
        }
        val matches = event.obj("match") ?: return null

        val matchId = matches.string("id")
        val teams = matches
            .array<JsonObject>("teams")
        val team1 = teams?.get(0)?.string("name")
        val team2 = teams?.get(1)?.string("name")

        var winner: String? = null
        if (teams?.get(0)
                        ?.obj("result")
                        ?.string("outcome") == "win"
        ) {
            winner = team1
        } else if (teams?.get(1)
                        ?.obj("result")
                        ?.string("outcome") == "win"
        ) {
            winner = team2
        }

        val bestOfVar = matches
            .obj("strategy")
                ?.int("count")

        val matchType: MatchType = when (bestOfVar) {
            1 -> MatchType.BO1
            3 -> MatchType.BO3
            5 -> MatchType.BO5
            else -> throw NumberFormatException("Best of must be 1, 3, or 5. Instead it was $bestOfVar")
        }
        return Match(
                id = matchId!!,
                gameIds = getGameIdsForMatchId(matchId),
                team1 = team1!!,
                team2 = team2!!,
                type = matchType,
                winner = winner ?: "TBD",
                date = date
        )
    }


    private suspend fun getGameIdsForMatchId(matchId: String): List<String> {
        val eventDetails = super.get("getEventDetails", listOf(Pair("id", matchId)))
        val eventDetailsJson = parser.parseJsonObject(StringReader(eventDetails))

        return eventDetailsJson.obj("data")
                ?.obj("event")
                ?.obj("match")
                ?.array<JsonObject>("games")
                ?.mapChildrenObjectsOnly {
                    it.string("id")!!
                }!!.toList()
    }

    companion object {
        private val parser = Klaxon()
    }
}
