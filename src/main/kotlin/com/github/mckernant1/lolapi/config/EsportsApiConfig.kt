package com.github.mckernant1.lolapi.config

import org.apache.http.impl.client.cache.CacheConfig

/**
 * @param endpointHost the host of the API to reach out to
 * @param languageCode the language code ENUM of the language you want
 * @param logger Specify a print function
 * @param cacheConfig Specify Caching properties for the httpClient
 */
data class EsportsApiConfig(
    val endpointHost: HostUrl = HostUrl.ESPORTS_API_1,
    val languageCode: LanguageCode = LanguageCode.EN_US,
    val logger: (String) -> Unit = {},
    val cacheConfig: CacheConfig? = CacheConfig.DEFAULT
) {
    init {
        this.println("Instantiating Config object with Options: host: '$endpointHost', language: '${languageCode.code}', logger: '$logger', cacheConfig: '$cacheConfig'")
    }

    internal fun println(s: String) {
        logger("[ESPORTS_API_WRAPPER]: $s")
    }
}

