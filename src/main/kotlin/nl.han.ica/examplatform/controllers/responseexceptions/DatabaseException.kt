package nl.han.ica.examplatform.controllers.responseexceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection

/**
 * Exception thrown when there's an error when interacting with the database in [MySQLConnection].
 * It is bound to a HTTP status [HttpStatus.INTERNAL_SERVER_ERROR].
 *
 * @param message [String] an additional message to further specify the error
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class DatabaseException(message: String) : RuntimeException(message)