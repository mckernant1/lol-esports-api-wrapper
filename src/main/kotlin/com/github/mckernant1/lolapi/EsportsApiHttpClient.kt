package com.github.mckernant1.lolapi

import com.github.mckernant1.lolapi.config.EsportsApiConfig
import org.apache.http.HttpException
import org.apache.http.HttpRequest
import org.apache.http.HttpStatus
import org.apache.http.client.cache.CacheResponseStatus
import org.apache.http.client.cache.HttpCacheContext
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.cache.CachingHttpClients
import org.apache.http.protocol.HttpContext
import org.apache.http.util.EntityUtils

/**
 * Internal HttpClient class
 * @constructor Takes in an optional esportsApiConfig object
 */
open class EsportsApiHttpClient(
    private val esportsApiConfig: EsportsApiConfig = EsportsApiConfig()
) {
    private val httpClient = CachingHttpClients
        .custom()
        .setCacheConfig(esportsApiConfig.cacheConfig)
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
        val context = HttpCacheContext.create()
        val someURI = URIBuilder()
            .setScheme("https")
            .setHost(esportsApiConfig.endpointHost)
            .setParameter("hl", esportsApiConfig.languageCode.code)
        someURI.path = path
        params.forEach { (key, value) ->
            someURI.setParameter(key, value)
        }
        val fullURI = someURI.build()
        esportsApiConfig.println("Executing get request on URL: ${fullURI.toASCIIString()}")
        val req = HttpGet(fullURI)
        val res = httpClient.execute(req, context)
        esportsApiConfig.println(
            when (context.cacheResponseStatus) {
                CacheResponseStatus.CACHE_HIT -> "Hit the Cache"
                CacheResponseStatus.CACHE_MODULE_RESPONSE -> "The response was generated directly by the Cache_module"
                CacheResponseStatus.CACHE_MISS -> "The response came from an upstream server"
                CacheResponseStatus.VALIDATED -> "The response was generated from the cache after validating the entry with the origin server"
                else -> error("Caching error")
            }
        )

        val body = EntityUtils.toString(res.entity)
            ?: throw Exception("Request Failed with URI ${fullURI.toASCIIString()}")

        if (res.statusLine.statusCode != HttpStatus.SC_OK) {
            throw HttpException("Request failed with message: $body")
        }

        res.close()
        return body
    }

    protected fun get(
        path: String
    ): String {
        return get(path, listOf())
    }

    fun close() {
        esportsApiConfig.println("Closing the httpClient")
        httpClient.close()
    }
}
