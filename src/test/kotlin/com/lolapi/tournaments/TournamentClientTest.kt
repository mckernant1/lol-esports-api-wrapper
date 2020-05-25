package com.lolapi.tournaments

import com.lolapi.leagues.LeagueClient
import org.junit.Test


internal class TournamentClientTest {

    @Test
    fun getTournamentsForLeague() {
        val leagueClient = LeagueClient()
        val leagueId = leagueClient.getLeagues()[5].id
        val tournamentClient = TournamentClient()
        println(tournamentClient.getTournamentsForLeague(leagueId))
    }
}
