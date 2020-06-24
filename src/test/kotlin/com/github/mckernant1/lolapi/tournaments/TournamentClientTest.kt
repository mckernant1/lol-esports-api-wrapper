package com.github.mckernant1.lolapi.tournaments

import com.github.mckernant1.lolapi.ClientBaseTest
import org.junit.Test


internal class TournamentClientTest : ClientBaseTest() {

    private val tournamentClient = TournamentClient(noCacheEsportsApiConfig)

    @Test
    fun getTournamentsForLeague() {

        assert(
            tournamentClient
                .getTournamentsForLeague("98767991299243165")
                .isNotEmpty()
        )
        tournamentClient.close()
    }

    @Test
    fun getStandingsForLeague() {
        val standings = tournamentClient.getStandingsForLeague("98767991299243165",
            2020,
            2
        )
        assert(standings.isNotEmpty())
    }

    @Test
    fun getStandingsForLeagueByName() {
        val standings = tournamentClient.getStandingsForLeagueByName(
            "LCS",
            2020,
            2
        )
        assert(standings.isNotEmpty())
    }
}
