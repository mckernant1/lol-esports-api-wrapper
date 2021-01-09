package com.github.mckernant1.lol.heimerdinger.games

import com.github.mckernant1.lol.heimerdinger.EsportsApiHttpClient
import com.github.mckernant1.lol.heimerdinger.config.EsportsApiConfig
import com.github.mckernant1.lol.heimerdinger.config.HostUrl
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class GameClient(
    esportsApiConfig: EsportsApiConfig = EsportsApiConfig(endpointHost = HostUrl.LIVE_STATS_API)
) : EsportsApiHttpClient(esportsApiConfig) {

    /**
     * The GameId can be gotten from the Match data structure in the ScheduleClient
     * @param gameId The Id of the game you want
     */
    fun getGameStats(
        gameId: String,
        startTime: LocalDateTime? = LocalDateTime.now().withSecond(0)
    ): Game {

        val params = mutableListOf<Pair<String, String>>()

        if (startTime != null) {
            params.add(Pair("startingTime", formatTimeToString(startTime)))
        }

        val gameString = super.get(
            "window/$gameId",
            params
        )

        val gameJson = parser.decodeFromString<JsonObject>(gameString)
        val gameData = parser.decodeFromJsonElement<GameData>(gameJson["gameMetadata"]!!)
        val frames = parser.decodeFromJsonElement<List<Frame>>(gameJson["frames"]!!)

        return Game(gameData, frames)
    }

    /**
     * The GameId can be gotten from the Match data structure in the ScheduleClient
     * @param gameId The Id of the game you want
     * @param startingTime The starting time of the data. Your seconds need to be divisible 10. In order to get endGame stats we default to LocalDateTime.now()
     * @param participantIds The list of participant Ids. can be gotten from the Game Object
     * @return The list of DetailFrame will only return 100 results. Adjust the time to get different results
     */
    fun getPlayerStats(
        gameId: String,
        startingTime: LocalDateTime? = LocalDateTime.now().withSecond(0),
        participantIds: List<Int> = listOf()
    ): List<DetailFrame> {
        val params = mutableListOf<Pair<String, String>>()
        if (startingTime != null) {
            params.add(Pair("startingTime", formatTimeToString(startingTime)))
        }

        var participantIdString = ""
        if (participantIds.isNotEmpty()) {
            participantIdString = participantIds.fold("") { acc: String, i: Int ->
                "${acc}_${i}"
            }.substring(1)
        }

        if (participantIdString != "") {
            params.add(Pair("participantIds", participantIdString))
        }

        val detailString = super.get("details/$gameId", params)
        val detailJson = parser.decodeFromString<JsonObject>(detailString)
        return parser.decodeFromJsonElement<List<DetailFrame>>(detailJson["frames"]!!)
    }

    private fun formatTimeToString(time: LocalDateTime): String =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneId.of("UTC")).format(time)


    companion object {
        private val parser = Json {
            ignoreUnknownKeys = true
        }
    }

}


