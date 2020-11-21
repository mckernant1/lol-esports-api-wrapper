package com.github.mckernant1.lol.heimerdinger.leagues

import com.github.mckernant1.lol.heimerdinger.ClientBaseTest
import org.junit.Test


internal class LeagueClientTest : ClientBaseTest() {

    private val leagueClient = LeagueClient(noCacheEsportsApiConfig)

    @Test
    fun getLeagues() {
        val leagues = leagueClient.getLeagues()
        assert(leagues.isNotEmpty())
    }

    @Test
    fun getLeagueByName() {
        assert(leagueClient.getLeagueByName("LCS").id == "98767991299243165")
    }

    @Test
    fun getLeagueBySlug() {
        assert(leagueClient.getLeagueBySlug("LCS").id == "98767991299243165")
    }
}
