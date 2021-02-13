package com.github.mckernant1.lol.heimerdinger.tournaments

import com.github.mckernant1.lol.heimerdinger.ClientBaseTest
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
    fun getMostRecentTournament() {
        val tournament = tournamentClient.getMostRecentTournament("98767991299243165")
        println(tournament)
        assert(tournament.slug.contains("lcs", ignoreCase = true))
    }

    @Test
    fun getStandingsForLeagueByName() {
        val standings = tournamentClient.getStandingsForLeagueByName(
            "LCS",
            2020
        )
        assert(standings.isNotEmpty())
    }

    @Test
    fun getWorlds() {
        val worlds = tournamentClient.getTournamentsForLeague("98767975604431411")
        assert(worlds.isNotEmpty())
    }

    @Test
    fun getLCSStandings() {
        val lcs = tournamentClient.getStandingsForMostRecentTournamentInLeague("98767991299243165")
        println(lcs)
        assert(lcs.isNotEmpty())
    }
}
