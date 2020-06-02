package com.github.mckernant1.lolapi.team

import org.junit.Test

internal class TeamClientTest {

    @Test
    fun getAllTeams() {
        val teamClient = TeamClient()
        val allTeams = teamClient.getAllTeams()
        assert(allTeams.isNotEmpty())
        teamClient.close()
    }

    @Test
    fun getTeamBySlug() {
        val teamClient = TeamClient()
        val cloud9 = teamClient.getTeamBySlug("cloud9")
        assert(cloud9.slug == "cloud9")
        teamClient.close()
    }

    @Test
    fun getTeamByCode() {
        val teamClient = TeamClient()
        val tsm = teamClient.getTeamByCode("tsm")
        assert(tsm.code.toLowerCase() == "tsm")
        teamClient.close()
    }
}
