package com.github.mckernant1.lolapi

import com.github.mckernant1.lolapi.config.EsportsApiConfig
import org.apache.http.HttpException
import org.apache.http.HttpRequest
import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.protocol.HttpContext
import org.apache.http.util.EntityUtils

/**
 * Internal HttpClient class
 * @constructor Takes in an optional esportsApiConfig object
 */
open class EsportsApiHttpClient(
    private val esportsApiConfig: EsportsApiConfig = EsportsApiConfig()
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


    protected fun get(
        path: String,
        params: List<Pair<String, String>> = listOf()
    ): String {
        val someURI = URIBuilder()
            .setScheme("https")
            .setHost(esportsApiConfig.endpointHost)
            .setParameter("hl", esportsApiConfig.languageCode.code)
        someURI.path = path
        params.forEach { (key, value) ->
            someURI.setParameter(key, value)
        }
        val fullURI = someURI.build()
        val req = HttpGet(fullURI)
        val res = httpClient.execute(req)

        if (res.statusLine.statusCode != HttpStatus.SC_OK) {
            throw HttpException("Request failed with message ${res.entity}")
        }

        return EntityUtils.toString(res.entity)
            ?: throw Exception("Request Failed with URI ${fullURI.toASCIIString()}")
    }

    protected fun get(
        path: String
    ): String {
        return get(path, listOf())
    }

    fun close() {
        httpClient.close()
    }
}
