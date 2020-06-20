package com.github.mckernant1.lolapi.schedule

import com.github.mckernant1.lolapi.ClientBaseTest
import org.junit.Test

internal class ScheduleClientTest : ClientBaseTest() {

    private val scheduleClient = ScheduleClient(noCacheEsportsApiConfig)

    @Test
    fun getSplit() {
        val split = scheduleClient.getSplit(
            "98767991299243165",
            2020,
            2
        )
        assert(split.matches.isNotEmpty())

        assert(split.matches.fold(true) {acc: Boolean, match: Match -> acc && match.gameIds.size == match.type.numberOfGames })

        val worlds = scheduleClient.getSplit("98767975604431411", 2019)

        assert(worlds.matches.isNotEmpty())

        scheduleClient.close()
    }

}
