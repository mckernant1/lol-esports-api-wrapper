package com.github.mckernant1.lolapi

import org.junit.Test

internal class RawEsportsApiHttpClientTest : ClientBaseTest() {

    private val rawEsportsApiHttpClient = RawEsportsApiHttpClient(noCacheEsportsApiConfig)

    @Test
    fun rawGet() {
        val resString = rawEsportsApiHttpClient.rawGet("getTeams")
        assert(resString.isNotBlank())
        rawEsportsApiHttpClient.close()
    }
}
