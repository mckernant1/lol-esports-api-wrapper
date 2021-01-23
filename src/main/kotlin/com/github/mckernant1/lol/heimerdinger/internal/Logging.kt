package com.github.mckernant1.lol.heimerdinger.internal

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

internal fun <T : Any> getLogger(clazz: KClass<T>): Logger = LoggerFactory.getLogger(clazz.java)
