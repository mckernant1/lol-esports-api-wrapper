package com.github.mckernant1.lolapi.games

import com.github.mckernant1.lolapi.ClientBaseTest
import com.github.mckernant1.lolapi.config.EsportsApiConfig
import org.junit.Test


internal class GameClientTest : ClientBaseTest() {

    override val noCacheEsportsApiConfig = EsportsApiConfig(
        cacheConfig = cacheConfig,
        endpointHost = "feed.lolesports.com/livestats/v1/"
    )

    private val gameClient = GameClient(noCacheEsportsApiConfig)

    @Test
    fun getGameFrames() {
        val game = gameClient.getGameStats("102147201352179016")
        assert(game.frames == frames)
        assert(game.data == gameData)
    }

    @Test
    fun getPlayerStats() {
        val playerStats = gameClient.getPlayerStats(
            "102147201352179016",
            participantIds = listOf(1, 2)
        )
        assert(playerStats == playerStatsData)
    }
}
