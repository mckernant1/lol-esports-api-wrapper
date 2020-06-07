package com.github.mckernant1.lolapi.games

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.github.mckernant1.lolapi.EsportsApiHttpClient
import com.github.mckernant1.lolapi.config.EsportsApiConfig
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.*

class GameClient(
    esportsApiConfig: EsportsApiConfig = EsportsApiConfig(endpointHost = "feed.lolesports.com/livestats/v1/")
) : EsportsApiHttpClient(esportsApiConfig) {

    /**
     * The GameId can be gotten from the Match data structure in the ScheduleClient
     * @param gameId The Id of the game you want
     */
    fun getGameStats(gameId: String): Game {
        val gameString = super.get("window/$gameId")

        val gameJson = parser.parseJsonObject(StringReader(gameString))
        val gameData = parser.parseFromJsonObject<GameData>(gameJson.obj("gameMetadata")!!)!!
        val frames = parser.parseFromJsonArray<Frame>(gameJson.array<JsonObject>("frames")!!)!!

        return Game(gameData, frames)
    }

    /**
     * The GameId can be gotten from the Match data structure in the ScheduleClient
     * @param gameId The Id of the game you want
     * @param startingTime The starting time of the data. Your seconds need to be divisible 10
     * @param participantIds The list of participant Ids. can be gotten from the Game Object
     * @return The list of DetailFrame will only return 100 results. Adjust the time to get different results
     */
    fun getPlayerStats(gameId: String, startingTime: Date? = null, participantIds: List<Int> = listOf()): List<DetailFrame> {
        val params = mutableListOf<Pair<String, String>>()
        if (startingTime != null) {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            params.add(Pair("startingTime", formatter.format(startingTime)))
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

    companion object {
        private val parser = Klaxon()
    }

}


