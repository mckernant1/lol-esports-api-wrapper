package com.github.mckernant1.lol.heimerdinger

import com.github.mckernant1.lol.heimerdinger.config.EsportsApiConfig
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

    protected open val noCacheEsportsApiConfig = EsportsApiConfig(
        logger = { println(it) }
    )
}
