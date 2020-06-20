package com.github.mckernant1.lolapi.team

import com.github.mckernant1.lolapi.ClientBaseTest
import org.junit.Test

internal class TeamClientTest : ClientBaseTest() {

    private val teamClient: TeamClient = TeamClient(noCacheEsportsApiConfig)

    @Test
    fun getAllTeams() {
        val allTeams = teamClient.getAllTeams()
        assert(allTeams.isNotEmpty())
    }

    @Test
    fun getTeamBySlug() {
        val cloud9 = teamClient.getTeamBySlug("cloud9")
        assert(cloud9 == cloud9Data)
    }

    @Test
    fun getTeamByCode() {
        val tsm = teamClient.getTeamByCode("tsm")
        assert(tsm.code.toLowerCase() == "tsm")
    }
}
