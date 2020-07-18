package com.github.mckernant1.lolapi

import com.github.mckernant1.lolapi.config.EsportsApiConfig
import org.apache.http.impl.client.cache.CacheConfig
import org.junit.Rule
import org.junit.rules.Stopwatch
import org.junit.runner.Description
import java.util.concurrent.TimeUnit

internal open class ClientBaseTest {

    @get:Rule
    val stopwatch = object : Stopwatch() {
        override fun succeeded(nanos: Long, description: Description?) =
            println("${description?.className}: ${description?.methodName} succeeded taking ${TimeUnit.NANOSECONDS.toMillis(nanos)} milliseconds")
    }

    protected val cacheConfig: CacheConfig = CacheConfig.custom()
        .setMaxCacheEntries(1000)
        .setMaxObjectSize(8192)
        .build()
    protected open val noCacheEsportsApiConfig = EsportsApiConfig(
        cacheConfig = cacheConfig,
        logger = System.out
    )
}
