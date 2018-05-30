package nl.han.ica.examplatform.config.logger

import org.slf4j.Logger
import org.slf4j.LoggerFactory

//inline fun <reified T:Any> loggerFor(): Logger = LoggerFactory.getLogger(T::class.java.name)
fun <T> loggerFor(clazz: Class<T>): Logger = LoggerFactory.getLogger(clazz)