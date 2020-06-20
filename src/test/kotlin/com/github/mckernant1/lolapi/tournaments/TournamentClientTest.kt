package com.github.mckernant1.lolapi.tournaments

import com.github.mckernant1.lolapi.ClientBaseTest
import org.junit.Test


internal class TournamentClientTest : ClientBaseTest() {

    private val tournamentClient = TournamentClient(noCacheEsportsApiConfig)

    @Test
    fun getTournamentsForLeague() {
        val tournaments = tournamentClient.getTournamentsForLeague("98767991299243165")
        assert(tournaments == tournamentData)
        tournamentClient.close()
    }
}
