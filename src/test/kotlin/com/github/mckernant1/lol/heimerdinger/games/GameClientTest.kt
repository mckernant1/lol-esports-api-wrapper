package com.github.mckernant1.lol.heimerdinger.games

import com.github.mckernant1.lolapi.ClientBaseTest
import com.github.mckernant1.lolapi.config.EsportsApiConfig
import com.github.mckernant1.lolapi.config.HostUrl
import org.junit.Test


internal class GameClientTest : ClientBaseTest() {

    override val noCacheEsportsApiConfig = EsportsApiConfig(
        cacheConfig = cacheConfig,
        endpointHost = HostUrl.LIVE_STATS_API
    )

    private val gameClient = GameClient(noCacheEsportsApiConfig)

    @Test
    fun getGameFrames() {
        val game = gameClient.getGameStats("102147201352179016")
        assert(game.frames.isNotEmpty())
    }

    @Test
    fun getPlayerStats() {
        val playerStats = gameClient.getPlayerStats(
            "102147201352179016",
            participantIds = listOf(1, 2)
        )
        assert(playerStats.isNotEmpty())
    }
}
