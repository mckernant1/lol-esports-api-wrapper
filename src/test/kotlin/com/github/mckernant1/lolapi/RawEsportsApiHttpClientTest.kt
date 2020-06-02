package com.github.mckernant1.lolapi

import org.junit.Test

class RawEsportsApiHttpClientTest {

    @Test
    fun rawGet() {
        val rawEsportsApiHttpClient = RawEsportsApiHttpClient()
        val resString = rawEsportsApiHttpClient.rawGet("getTeams")
        assert(resString.isNotBlank())
        rawEsportsApiHttpClient.close()
    }
}
