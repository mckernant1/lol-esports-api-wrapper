package com.github.mckernant1.lolapi

import com.github.mckernant1.lolapi.config.EsportsApiConfig

class RawEsportsApiHttpClient(
    esportsApiConfig: EsportsApiConfig = EsportsApiConfig()
) : EsportsApiHttpClient(esportsApiConfig) {

    /**
     * Does a get request against the lol-esports API
     * @param path the URI path
     * @param params The URI params
     * @return The body as a string. It will be JSON
     */
    fun rawGet(
        path: String,
        params: List<Pair<String, String>> = listOf()
    ): String {
       return super.get(path, params)
    }
}
