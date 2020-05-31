package com.github.mckernant1.lolapi.schedule

import org.junit.Test

internal class ScheduleClientTest {

    @Test
    fun getSplit() {
        val scheduleClient = ScheduleClient()
        assert(
            scheduleClient.getSplit(
                "98767991299243165",
                2020,
                2
            ).matches.isNotEmpty()
        )
    }

}