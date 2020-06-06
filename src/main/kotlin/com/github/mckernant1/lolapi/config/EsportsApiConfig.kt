package com.github.mckernant1.lolapi.config

/**
 * @param endpointHost the host of the API to reach out to
 * @param languageCode the language code ENUM of the language you want
 */
data class EsportsApiConfig(
    val endpointHost: String = "esports-api.lolesports.com/persisted/gw/",
    val languageCode: LanguageCode = LanguageCode.EN_US
)
