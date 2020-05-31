package com.github.mckernant1.lolapi.team

import org.junit.Test

internal class TeamClientTest {

    @Test
    fun getAllTeams() {
        val teamClient = TeamClient()
        val allTeams = teamClient.getAllTeams()
        assert(allTeams.isNotEmpty())
    }

    @Test
    fun getTeamBySlug() {
        val teamClient = TeamClient()
        val cloud9 = teamClient.getTeamBySlug("cloud9")
        assert(cloud9.slug == "cloud9")
    }

    @Test
    fun getTeamByCode() {
        val teamClient = TeamClient()
        val tsm = teamClient.getTeamByCode("tsm")
        assert(tsm.code.toLowerCase() == "tsm")
    }
}
