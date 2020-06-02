package com.github.mckernant1.lolapi.leagues

import org.junit.Test


internal class LeagueClientTest {

    @Test
    fun getLeagues() {
        val leagueClient = LeagueClient()
        val leagues = leagueClient.getLeagues()
        assert(leagues.isNotEmpty())
        leagueClient.close()
    }

    @Test
    fun getLeagueByName() {
        val leagueClient = LeagueClient()
        assert(leagueClient.getLeagueByName("LCS").id == "98767991299243165")
        leagueClient.close()
    }
}
