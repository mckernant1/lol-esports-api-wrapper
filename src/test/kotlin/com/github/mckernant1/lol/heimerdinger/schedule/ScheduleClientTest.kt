package com.github.mckernant1.lol.heimerdinger.schedule

import com.github.mckernant1.lol.heimerdinger.ClientBaseTest
import com.github.mckernant1.lol.heimerdinger.tournaments.TournamentClient
import org.junit.Test

internal class ScheduleClientTest : ClientBaseTest() {

    private val scheduleClient = ScheduleClient(noCacheEsportsApiConfig)
    private val tournamentClient = TournamentClient(noCacheEsportsApiConfig)

    @Test
    fun getSplit() {
        val split = scheduleClient.getSplitByYearAndNumber(
            "98767991299243165",
            2020,
            2
        ).also { println(it) }

        assert(split.matches.isNotEmpty())

        assert(split.matches.fold(true) {acc: Boolean, match: Match -> acc && match.gameIds.size == match.type.numberOfGames })
    }

    @Test
    fun getSplitForWorlds() {
        val worlds = scheduleClient.getSplitByYearAndNumber("98767975604431411", 2020)
        assert(worlds.matches.isNotEmpty())
    }

    @Test
    fun getMostRecentSplit() {
        val tournament = tournamentClient.getMostRecentTournament("98767991299243165")
        val split = scheduleClient.getSplitByTournament("98767991299243165", tournament)
        println(split)
    }
}
