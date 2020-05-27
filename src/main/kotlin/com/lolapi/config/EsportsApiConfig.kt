package com.lolapi.config

/**
 * @constructor takes in a language code ENUM. Default is English-US
 */
data class EsportsApiConfig(
    val languageCode: LanguageCode = LanguageCode.EN_US
)
