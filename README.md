# Lol Esports API Wrapper

[![Code Coverage](https://codecov.io/gh/mckernant1/LolEsportsApiWrapper/branch/master/graph/badge.svg)](https://codecov.io/gh/mckernant1/LolEsportsApiWrapper)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mckernant1/lol-esports-api-wrapper/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mckernant1/lol-esports-api-wrapper)

This is a work in progress!

The current Lol Esports API is confusing and weird

Esports API Docs
https://vickz84259.github.io/lolesports-api-docs/


This is a wrapper around the API calls to make it easier for people to develop against it.

### Install via Gradle
Make sure you add maven central and jcenter
```kotlin

repositories {
    mavenCentral()
    jcenter()
}
...
dependencies {
    ...
    implementation("com.github.mckernant1:lol-esports-api-wrapper:0.1.14_1")
}
```


## How to use
This provides Clients that get data on various endpoints

```kotlin
val leagueClient = LeagueClient()
val lcs = LeagueClient.getLeagueByName("LCS")
val lcsId = lcs.id

val tournamentClient = TournamentClient()
val tournament = tournamentClient.getTournamentsForLeague(lcsId)

val split = ScheduleClient().getSplit(lcsId, 2020, 1) // Gets spring 2020 split including playoffs
```

You can also use the general perpose EsportsApiHttpClient to do general queries that return JSON strings

```kotlin
val rawEsportsApiHttpClient = RawEsportsApiHttpClient()
val split = rawEsportsApiHttpClient.get(
    "getSchedule",
    listOf(
        Pair("leagueId", leagueId)
    )
)
```

### Configuration
You can alter the settings of the httpClient via this config object

endpointHost: the host of the API to reach out to. Different clients use different endpoints

languageCode: the language code ENUM of the language you want. Enum is [here](https://github.com/mckernant1/LolEsportsApiWrapper/blob/master/src/main/kotlin/com/github/mckernant1/lolapi/config/LanguageCode.kt)

logger: Specify a PrintStream for the config. Default is null. Can input System.out or System.err

cacheConfig: Specify Caching properties for the httpClient

```kotlin

data class EsportsApiConfig(
    val endpointHost: String = "esports-api.lolesports.com/persisted/gw/",
    val languageCode: LanguageCode = LanguageCode.EN_US,
    val logger: PrintStream = {},
    val cacheConfig: CacheConfig? = CacheConfig.DEFAULT
)
```
