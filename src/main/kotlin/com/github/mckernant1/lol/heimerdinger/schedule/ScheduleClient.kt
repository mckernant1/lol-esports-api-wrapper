package com.github.mckernant1.lol.heimerdinger.schedule

import com.github.mckernant1.lol.heimerdinger.EsportsApiHttpClient
import com.github.mckernant1.lol.heimerdinger.config.EsportsApiConfig
import com.github.mckernant1.lol.heimerdinger.tournaments.Tournament
import com.github.mckernant1.lol.heimerdinger.tournaments.TournamentClient
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
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
        val json = parser.decodeFromString<JsonObject>(split)
        val startDate = LocalDate.parse(tourney.startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(
            ZoneId.of("UTC")
        )
        val endDate = LocalDate.parse(tourney.endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(
            ZoneId.of("UTC")
        )
        var matches = parseMatches(json).toMutableList()
        var prevJson = json
        while (matches.find { it.date < startDate } == null) {
            val prevPageToken = prevJson["data"]
                ?.jsonObject?.get("schedule")
                ?.jsonObject?.get("pages")
                ?.jsonObject?.get("older")
                ?.jsonPrimitive?.content ?: "null"

            if (prevPageToken == "null") break

            val prevPage = super.get(
                "getSchedule",
                listOf(
                    Pair("leagueId", leagueId),
                    Pair("pageToken", prevPageToken)
                )
            )
            prevJson = parser.decodeFromString(prevPage)
            val newMatches = parseMatches(prevJson).filter {
                it.date > startDate &&
                        it.date < endDate
            }
            matches.addAll(newMatches)
        }
        matches = matches.filter {
            it.date >= startDate &&
                    it.date <= (endDate + Duration.ofDays(1))
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
            return@runBlocking json["data"]
                ?.jsonObject?.get("schedule")
                ?.jsonObject?.get("events")
                ?.jsonArray?.map { event: JsonElement ->
                    async { eventToMatch(event.jsonObject) }
                }?.mapNotNull { it.await() }?.toList()
                ?: throw SerializationException()
        }
    }

    /*
     * Async method to parse an event to a match. Also retrieves the gameIds
     */
    private suspend fun eventToMatch(event: JsonObject): Match? {
        val date: ZonedDateTime = try {
            LocalDateTime.parse(event["startTime"]!!.jsonPrimitive.content, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))
                .atZone(ZoneId.of("UTC"))
        } catch (e: DateTimeParseException) {
            LocalDateTime.parse(event["startTime"]!!.jsonPrimitive.content, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
                .atZone(ZoneId.of("UTC"))
        }
        val matches = event["match"]?.jsonObject ?: return null

        val matchId = matches["id"]?.jsonPrimitive?.content
            ?: return null
        val teams = matches["teams"]?.jsonArray
        val team1 = teams?.get(0)?.jsonObject?.get("name")?.jsonPrimitive?.content ?: return null
        val team2 = teams?.get(1)?.jsonObject?.get("name")?.jsonPrimitive?.content ?: return null

        val (team1Results, team2Results) = try {
            Pair(teams?.get(0)?.jsonObject?.get("result")?.jsonObject, teams?.get(1)?.jsonObject?.get("result")?.jsonObject)
        } catch (iae: IllegalArgumentException) {
            return null
        }
        val team1NumWins: Int = team1Results?.get("gameWins")?.jsonPrimitive?.int ?: 0

        val team2NumWins: Int = team2Results?.get("gameWins")?.jsonPrimitive?.int ?: 0

        var winner: String? = null

        if (team1Results?.jsonObject?.get("outcome")?.jsonPrimitive?.content == "win") {
            winner = team1
        } else if (team2Results?.jsonObject?.get("outcome")?.jsonPrimitive?.content == "win") {
            winner = team2
        }

        val bestOfVar = matches.jsonObject["strategy"]
            ?.jsonObject?.get("count")
            ?.jsonPrimitive
            ?.int ?: 1

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
            team1NumWins = team1NumWins,
            team2NumWins = team2NumWins,
            type = matchType,
            winner = winner ?: "TBD",
            date = date
        )
    }


    private suspend fun getGameIdsForMatchId(matchId: String): List<String> {
        val eventDetails = super.get("getEventDetails", listOf(Pair("id", matchId)))
        val eventDetailsJson = parser.decodeFromString<JsonObject>(eventDetails)

        return eventDetailsJson["data"]
            ?.jsonObject?.get("event")
            ?.jsonObject?.get("match")
            ?.jsonObject?.get("games")
            ?.jsonArray?.mapNotNull {
                it.jsonObject["id"]?.jsonPrimitive?.content
            }?.toList() ?: throw SerializationException()
    }

    companion object {
        private val parser = Json
    }
}
