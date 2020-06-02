package com.github.mckernant1.lolapi.schedule

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.github.mckernant1.lolapi.EsportsApiHttpClient
import com.github.mckernant1.lolapi.config.EsportsApiConfig
import com.github.mckernant1.lolapi.tournaments.Tournament
import com.github.mckernant1.lolapi.tournaments.TournamentClient
import java.io.StringReader
import java.sql.Date

class ScheduleClient(
    val esportsApiConfig: EsportsApiConfig = EsportsApiConfig()
) : EsportsApiHttpClient(esportsApiConfig) {
    /**
     * @param leagueId The Id of the league gotten from the leagueClient
     * @param splitYear The year of the split
     * @param splitNumber The number of the split (1 Spring, 2 Summer)
     */
    fun getSplit(leagueId: String, splitYear: Int, splitNumber: Int): Split {
        val split = super.get(
            "getSchedule",
            listOf(
                Pair("leagueId", leagueId)
            )
        )
        val json: JsonObject = Klaxon().parseJsonObject(StringReader(split))
        val tourney = getTourneyForSplit(leagueId, splitYear, splitNumber)
        val startDate = Date.valueOf(tourney.startDate)
        val endDate = Date.valueOf(tourney.endDate)
        var matches = parseMatches(json).filter {
            it.date > startDate &&
                    it.date < endDate
        }.toMutableList()
        var prevJson = json
        while (matches.find { it.date < startDate } == null) {
            val prevPageToken = prevJson.obj("data")
                ?.obj("schedule")
                ?.obj("pages")
                ?.string("older") ?: break
            val prevPage = super.get(
                "getSchedule",
                listOf(
                    Pair("leagueId", leagueId),
                    Pair("pageToken", prevPageToken)
                )
            )
            prevJson = Klaxon().parseJsonObject(StringReader(prevPage))
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
    private fun getTourneyForSplit(leagueId: String, splitYear: Int, splitNumber: Int): Tournament {
        val tournamentClient = TournamentClient()
        val slugTest = mutableListOf<String>()
        if (splitNumber == 1) {
            slugTest.add("spring")
            slugTest.add("split1")
        } else if (splitNumber == 2) {
            slugTest.add("summer")
            slugTest.add("split2")
        }

        return tournamentClient.getTournamentsForLeague(leagueId).find {
            return@find it.slug.contains(splitYear.toString())
                    && slugTest.fold(false) { acc, s ->
                acc || it.slug.contains(s)
            }
        } ?: throw NoSuchFieldError("No tournament found for $leagueId, $splitYear, $splitNumber")
    }

    /*
     * Helper method to parse JSON
     */
    private fun parseMatches(json: JsonObject): List<Match> {
        return json
            .obj("data")
            ?.obj("schedule")
            ?.array<JsonObject>("events")
            ?.mapChildrenObjectsOnly { event ->
                val date = Date.valueOf(event.string("startTime")
                    ?.takeWhile { it != 'T' })
                val matches = event.obj("match")
                val teams = matches
                    ?.array<JsonObject>("teams")
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
                val matchType: MatchType = when (matches
                    ?.obj("strategy")
                    ?.int("count")) {
                    1 -> MatchType.BO1
                    3 -> MatchType.BO3
                    5 -> MatchType.BO5
                    else -> throw NumberFormatException("Best of must be 1, 3, or 5")
                }
                return@mapChildrenObjectsOnly Match(
                    team1 = team1!!,
                    team2 = team2!!,
                    type = matchType,
                    winner = winner ?: "TBD",
                    date = date
                )
            }!!.toList()
    }
}
