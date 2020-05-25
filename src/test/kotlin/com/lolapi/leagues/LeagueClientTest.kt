package com.lolapi.leagues

import org.junit.Test


internal class LeagueClientTest {

    @Test
    fun getLeagues() {
        val leagueClient = LeagueClient()
        println(leagueClient.getLeagues())
    }
}
