package com.lolapi.schedule

import org.junit.Test

internal class ScheduleClientTest {

    @Test
    fun getSplit() {
        val scheduleClient = ScheduleClient()
        println(
            scheduleClient.getSplit(
                "98767991299243165",
                2020,
                1
            )
        )
    }

}
