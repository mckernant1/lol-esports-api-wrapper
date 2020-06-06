package com.github.mckernant1.lolapi.schedule

import org.junit.Test

internal class ScheduleClientTest {

    @Test
    fun getSplit() {
        val scheduleClient = ScheduleClient()
        val split = scheduleClient.getSplit(
            "98767991299243165",
            2020,
            2
        )
        assert(
            split.matches.isNotEmpty()
        )

        assert(
            split.matches.fold(true) {acc: Boolean, match: Match -> acc && match.gameIds.size == match.type.numberOfGames }
        )
        scheduleClient.close()
    }

}
