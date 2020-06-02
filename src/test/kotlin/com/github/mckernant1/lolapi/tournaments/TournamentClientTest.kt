package com.github.mckernant1.lolapi.tournaments

import org.junit.Test


internal class TournamentClientTest {

    @Test
    fun getTournamentsForLeague() {
        val tournamentClient = TournamentClient()
        assert(
            tournamentClient
                .getTournamentsForLeague("98767991299243165")
                .isNotEmpty()
        )
        tournamentClient.close()
    }
}
