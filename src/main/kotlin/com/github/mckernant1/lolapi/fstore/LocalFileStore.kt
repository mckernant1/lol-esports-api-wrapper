package com.github.mckernant1.lolapi.fstore

import java.io.*
import java.time.Duration
import java.time.LocalDateTime

class LocalFileStore(val config: LocalFileStoreConfig = LocalFileStoreConfig()) {

    init {
        File(config.storageFolder).mkdir()
    }

    private val dir = File(config.storageFolder)

    private fun uriToFilename(uri: String) = uri
        .replace("https://", "")
        .replace("/", "-")
        .replace(".", "_")
        .replace("?", "+")

    fun store(uri: String, resultBody: String) {
        val url = uriToFilename(uri)
        val content = FileContent(
            url,
            LocalDateTime.now(),
            resultBody
        )
        val file = File(dir, "$url.json")

        ObjectOutputStream(FileOutputStream(file)).use { it.writeObject(content) }
    }


    fun retrieve(uri: String): String? {
        val url = uriToFilename(uri)

        val file = File(dir,"$url.json")
        if (!file.exists()) {
            return null
        }
        val content =
            when (val anyObj = ObjectInputStream(FileInputStream(file)).readObject()) {
                is FileContent -> anyObj
                else -> error("Could not decode file")
            }

        return if (Duration.between(content.dateTime, LocalDateTime.now()) < config.duration) {
            content.resultBody
        } else {
            file.delete()
            null
        }
    }
}


data class FileContent(
    val url: String,
    val dateTime: LocalDateTime,
    val resultBody: String
) : Serializable
