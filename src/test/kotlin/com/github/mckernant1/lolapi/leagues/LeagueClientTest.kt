package com.github.mckernant1.lolapi.leagues

import com.github.mckernant1.lolapi.ClientBaseTest
import org.junit.Test


internal class LeagueClientTest : ClientBaseTest() {

    private val leagueClient = LeagueClient(noCacheEsportsApiConfig)

    @Test
    fun getLeagues() {

        val leagues = leagueClient.getLeagues()
        assert(leagues.isNotEmpty())
        leagueClient.close()
    }

    @Test
    fun getLeagueByName() {
        assert(leagueClient.getLeagueByName("LCS").id == "98767991299243165")
        leagueClient.close()
    }
}
