package com.lolapi.schedule

import com.beust.klaxon.Klaxon
import com.lolapi.EsportsApiHttpClient
import com.lolapi.config.EsportsApiConfig
import java.io.StringReader

class ScheduleClient(
    val esportsApiConfig: EsportsApiConfig = EsportsApiConfig()
) {
    val esportsApiHttpClient = EsportsApiHttpClient(esportsApiConfig)

    /**
     * @param leagueId The Id of the league gotten from the leagueClient
     * @param splitYear The year of the split
     * @param splitNumber The number of the split (1 Spring, 2 Summer)
     */
    fun getSplit(leagueId: String, splitYear: Int, splitNumber: Int) {
        val split = esportsApiHttpClient.get(
            "getSchedule",
            listOf(
                Pair("leagueId", leagueId)
            )
        )
        val json = Klaxon().parseJsonObject(StringReader(split))

        println(
            json
                .obj("data")
                ?.obj("schedule")
                ?.array<Any>("events")
        )
    }

}
