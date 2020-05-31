# Lol Esports API Wrapper
![Publish package to the Maven Central Repository](https://github.com/mckernant1/LolEsportsApiWrapper/workflows/Publish%20package%20to%20the%20Maven%20Central%20Repository/badge.svg)

This is a work in progress!

The current Lol Esports API is confusing and weird

Esports API Docs
https://vickz84259.github.io/lolesports-api-docs/


This is a wrapper around the API calls to make it easier for people to develop against it.

### Install via Gradle

```kotlin
implementation("com.github.mckernant1:lol-esports-api-wrapper:0.0.6")
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
val esportsApiHttpClient = EsportsApiHttpClient()
val split = esportsApiHttpClient.get(
    "getSchedule",
    listOf(
        Pair("leagueId", leagueId)
    )
)
```

You can give any client a Config Object to change the Language. It defaults to `LanguageCode.EN_US`

```kotlin
val esportsApiConfig = EsportsApiConfig(LanguageCode.EN_GB)
val esportsApiHttpClient = EsportsApiHttpClient(esportsApiConfig)
```
