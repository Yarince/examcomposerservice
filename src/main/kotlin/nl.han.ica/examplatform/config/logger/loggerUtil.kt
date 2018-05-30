package nl.han.ica.examplatform.config.logger

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * This function returns a logger for the given class.
 *
 * @param clazz [Class] The class for the logger.
 * @return [Logger] Returns the logger for this class.
 */
fun <T> loggerFor(clazz: Class<T>): Logger = LoggerFactory.getLogger(clazz)
