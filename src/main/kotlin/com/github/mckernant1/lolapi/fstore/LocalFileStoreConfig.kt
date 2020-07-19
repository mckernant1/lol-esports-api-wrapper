package com.github.mckernant1.lolapi.fstore

import java.time.Duration


data class LocalFileStoreConfig(
    val duration: Duration = Duration.ofHours(1),
    val storageFolder: String = "store/"
)
