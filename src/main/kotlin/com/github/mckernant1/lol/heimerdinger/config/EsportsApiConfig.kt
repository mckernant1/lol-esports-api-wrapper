package com.github.mckernant1.lol.heimerdinger.config

/**
 * @param endpointHost the host of the API to reach out to
 * @param languageCode the language code ENUM of the language you want
 * @param logger Specify a print function
 * @param cacheConfig Specify Caching properties for the httpClient
 */
data class EsportsApiConfig(
    val endpointHost: HostUrl = HostUrl.ESPORTS_API_1,
    val languageCode: LanguageCode = LanguageCode.EN_US,
    val logger: (String) -> Unit = {}
) {
    init {
        this.println("Instantiating Config object with Options: host: '$endpointHost', language: '${languageCode.code}', logger: '$logger'")
    }

    internal fun println(s: String) {
        logger(s)
    }
}

