package com.github.mckernant1.lolapi.config

import org.apache.http.impl.client.cache.CacheConfig
import java.io.OutputStream
import java.io.PrintStream

/**
 * @param endpointHost the host of the API to reach out to
 * @param languageCode the language code ENUM of the language you want
 * @param logger Specify a PrintStream for the config. Default is null
 * @param cacheConfig Specify Caching properties for the httpClient
 */
data class EsportsApiConfig(
    val endpointHost: String = "esports-api.lolesports.com/persisted/gw/",
    val languageCode: LanguageCode = LanguageCode.EN_US,
    val logger: PrintStream = PrintStream(NullOutputStream()),
    val cacheConfig: CacheConfig? = CacheConfig.DEFAULT
) {
    init {
        this.println("Instantiating Config object with Options: host: '$endpointHost', language: '${languageCode.code}', logger: '$logger', cacheConfig: '$cacheConfig'")
    }

    internal fun println(s: String) {
        logger.println("[ESPORTS_API_WRAPPER]: $s")
    }
}


private class NullOutputStream : OutputStream() {
    override fun write(p0: Int) = Unit
}
