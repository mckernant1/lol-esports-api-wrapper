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
        scheduleClient.close()
    }

}
