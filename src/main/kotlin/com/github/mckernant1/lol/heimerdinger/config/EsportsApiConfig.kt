package com.github.mckernant1.lol.heimerdinger.config

import com.github.mckernant1.lol.heimerdinger.internal.getLogger
import org.slf4j.Logger

/**
 * @param endpointHost the host of the API to reach out to
 * @param languageCode the language code ENUM of the language you want
 */
data class EsportsApiConfig(
    val endpointHost: HostUrl = HostUrl.ESPORTS_API_1,
    val languageCode: LanguageCode = LanguageCode.EN_US,
) {
    init {
        logger.debug("Instantiating Config object with Options: host: '$endpointHost', language: '${languageCode.code}'")
    }

    companion object {
        val logger: Logger = getLogger(this::class)
    }
}

