package com.github.mckernant1.lolapi.config

/**
 * The different host Urls that can be used
 */
enum class HostUrl(val hostname: String) {
    ESPORTS_API_1("prod-relapi.ewp.gg/persisted/gw/"),
    ESPORTS_API_2("esports-api.lolesports.com/persisted/gw/"),
    LIVE_STATS_API("feed.lolesports.com/livestats/v1/")
}
