package com.github.mckernant1.lolapi.games

import org.junit.Test
import java.text.SimpleDateFormat


internal class GameClientTest {

    @Test
    fun getGameFrames() {
        val gameClient = GameClient()
        val game = gameClient.getGameStats("102147201352179016")
        assert(game.frames.isNotEmpty())
    }

    @Test
    fun getPlayerStats() {
        val gameClient = GameClient()
        val playerStats = gameClient.getPlayerStats(
            "102147201352179016",
            participantIds = listOf(1, 2),
            startingTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse("2019-08-11T19:07:50Z")
        )
        assert(playerStats.isNotEmpty())
    }
}