package com.github.mckernant1.lolapi

import com.github.mckernant1.lolapi.config.EsportsApiConfig
import org.apache.http.HttpRequest
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.protocol.HttpContext
import org.apache.http.util.EntityUtils

/**
 * @constructor Takes in an optional esportsApiConfig object
 */
class EsportsApiHttpClient(
    esportsApiConfig: EsportsApiConfig = EsportsApiConfig()
) {
    private val httpClient = HttpClients
        .custom()
        .addInterceptorFirst { httpRequest: HttpRequest, _: HttpContext ->
            httpRequest.addHeader(
                "x-api-key",
                "0TvQnueqKa5mxJntVWt0w4LpLfEkrV1Ta8rQBb9Z"
            )
        }
        .build()
    private val someURI = URIBuilder()
        .setScheme("https")
        .setHost("esports-api.lolesports.com/persisted/gw/")
        .setParameter("hl", esportsApiConfig.languageCode.code)

    /**
     * Does a get request against the lol-esports API
     * @param path the URI path
     * @param params The URI params
     * @return The body as a string. It will be JSON
     */
    fun get(
        path: String,
        params: List<Pair<String, String>> = listOf()
    ): String {
        someURI.path = path
        params.forEach { (key, value) ->
            someURI.setParameter(key, value)
        }
        val fullURI = someURI.build()
        val req = HttpGet(fullURI)
        val res = httpClient.execute(req)

        return EntityUtils.toString(res.entity)
            ?: throw Exception("Request Failed with URI ${fullURI.toASCIIString()}")
    }
}
