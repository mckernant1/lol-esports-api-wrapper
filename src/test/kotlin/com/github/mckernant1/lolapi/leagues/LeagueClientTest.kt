package com.github.mckernant1.lolapi.leagues

import com.github.mckernant1.lolapi.ClientBaseTest
import org.junit.Test


internal class LeagueClientTest : ClientBaseTest() {

    private val leagueClient = LeagueClient(noCacheEsportsApiConfig)

    @Test
    fun getLeagues() {
        val leagues = leagueClient.getLeagues()
        assert(leagues == leaguesData)
    }

    @Test
    fun getLeagueByName() {
        val lcs = leagueClient.getLeagueByName("LCS")
        assert(lcs == lcsData)
    }
}
