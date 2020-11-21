package com.github.mckernant1.lol.heimerdinger.games

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.github.mckernant1.lol.heimerdinger.EsportsApiHttpClient
import com.github.mckernant1.lolapi.config.EsportsApiConfig
import com.github.mckernant1.lolapi.config.HostUrl
import java.io.StringReader
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class GameClient(
    esportsApiConfig: EsportsApiConfig = EsportsApiConfig(endpointHost = HostUrl.LIVE_STATS_API)
) : _root_ide_package_.com.github.mckernant1.lol.heimerdinger.EsportsApiHttpClient(esportsApiConfig) {

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

        val gameJson = parser.parseJsonObject(StringReader(gameString))
        val gameData = parser.parseFromJsonObject<GameData>(gameJson.obj("gameMetadata")!!)!!
        val frames = parser.parseFromJsonArray<Frame>(gameJson.array<JsonObject>("frames")!!)!!

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
        val detailJson = parser.parseJsonObject(StringReader(detailString))
        return parser.parseFromJsonArray<DetailFrame>(detailJson.array<JsonObject>("frames")!!)!!
    }

    private fun formatTimeToString(time: LocalDateTime): String =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneId.of("UTC")).format(time)


    companion object {
        private val parser = Klaxon()
    }

}


