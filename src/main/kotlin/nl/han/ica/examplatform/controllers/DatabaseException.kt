package nl.han.ica.examplatform.controllers

import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import java.sql.SQLException

/**
 * Exception thrown when there's an error when interacting with the database in [MySQLConnection].
 *
 * @param message [String] an additional message to further specify the error
 */
class DatabaseException(
        message: String,
        override val cause: SQLException? = null
) : RuntimeException(message, cause)
