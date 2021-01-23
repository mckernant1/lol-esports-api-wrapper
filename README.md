# Lol Esports API Wrapper

[![Code Coverage](https://codecov.io/gh/mckernant1/LolEsportsApiWrapper/branch/master/graph/badge.svg)](https://codecov.io/gh/mckernant1/LolEsportsApiWrapper)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mckernant1/lol-esports-api-wrapper/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mckernant1/lol-esports-api-wrapper)
[![Discord Chat](https://img.shields.io/discord/802610953396551720)](https://discord.gg/Dvq8f5KxZT)  

## Info
This is a wrapper around esports api calls in Kotlin.
It converts response data into Java objects that are much easier to deal with.

Enjoy!

### Esports API Docs
These are the docs That are available for the Esports API. 

https://vickz84259.github.io/lolesports-api-docs/


### Install via Gradle
Make sure you add maven central and jcenter
Maven Central Link: https://search.maven.org/artifact/com.github.mckernant1/lol-esports-api-wrapper/0.1.19/jar

```kotlin

repositories {
    mavenCentral()
    jcenter()
}
...
dependencies {
    ...
    implementation("com.github.mckernant1:lol-esports-api-wrapper:+")
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

logger: Specify a Print function for the config. 
```kotlin

data class EsportsApiConfig(
    val endpointHost: HostUrl = HostUrl.ESPORTS_API_1,
    val languageCode: LanguageCode = LanguageCode.EN_US,
)
```
