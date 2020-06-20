package com.github.mckernant1.lolapi.tournaments

import com.beust.klaxon.Klaxon


private val parser = Klaxon()

val tournamentData = parser.parseArray<Tournament>("""
    [{"endDate" : "2018-09-16", "id" : "100205574960535474", "slug" : "na_2018_summer", "startDate" : "2018-06-16"}, {"endDate" : "2019-04-14", "id" : "101383794038323910", "slug" : "lcs_2019_spring", "startDate" : "2019-01-26"}, {"endDate" : "2019-09-20", "id" : "102147201296669914", "slug" : "lcs_2019_summer", "startDate" : "2019-06-01"}, {"endDate" : "2020-04-20", "id" : "103462439438682788", "slug" : "lcs_2020_split1", "startDate" : "2020-01-24"}, {"endDate" : "2020-09-14", "id" : "104174992692075107", "slug" : "lcs-summer-2020", "startDate" : "2020-06-11"}, {"endDate" : "2017-09-11", "id" : "98926509909121648", "slug" : "na_2017_summer", "startDate" : "2017-06-02"}, {"endDate" : "2018-04-08", "id" : "99294154009972000", "slug" : "na_2018_spring", "startDate" : "2018-01-20"}]
""".trimIndent())
