package nl.han.ica.examplatform.persistence.decryptioncode

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.DatabaseException
import nl.han.ica.examplatform.controllers.decryptioncode.DecryptionCodeNotFoundException
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * This class handles all the Database operations for decryption codes [String].
 */
@Repository
class DecryptionCodeDAO : IDecryptionCodeDAO {
    override fun getAllDecryptionCodes(): ArrayList<Pair<String, String>> {
        val sqlRetrieveExamDecryptKeyQuery = "SELECT DECRYPTKEY, EXAMNAME FROM EXAM"

        val conn: Connection? = MySQLConnection.getConnection()

        val preparedStatement: PreparedStatement? = conn?.prepareStatement(sqlRetrieveExamDecryptKeyQuery)

        return try {
            val resultSet = preparedStatement?.executeQuery()
                    ?: throw DatabaseException("Error while fetching decryption codes from the database.")

            val decryptionCodes = ArrayList<Pair<String, String>>()
            while(resultSet.next()) {
                decryptionCodes.add(Pair(resultSet.getString("EXAMNAME"), resultSet.getString("DECRYPTKEY") ?: "No decrypt key available"))
            }
            decryptionCodes
        } catch (e: SQLException) {
            logger.error("Error retrieving exam decryption keys", e)
            throw DatabaseException("Error while retrieving exam decryption keys")
        }
    }

    private val logger = loggerFor(javaClass)

    /**
     * This function gets a decryption code for a single exam from the database
     *
     * @return [String] decryption code
     */
    override fun getDecryptionCode(examId: Int): String {
        val sqlRetrieveExamDecryptKeyQuery = "SELECT DECRYPTKEY FROM EXAM WHERE EXAMID = ?"

        val conn: Connection? = MySQLConnection.getConnection()

        val preparedStatement: PreparedStatement? = conn?.prepareStatement(sqlRetrieveExamDecryptKeyQuery)
        preparedStatement?.setInt(1, examId)

        return try {
            val resultSet = preparedStatement?.executeQuery()
                    ?: throw DatabaseException("Error while fetching decryption codes from the database.")
            resultSet.next()
                "\"" + (resultSet.getString("DECRYPTKEY") ?: throw DecryptionCodeNotFoundException()) + "\""
        } catch (e: SQLException) {
            logger.error("Error retrieving exam decryption key $examId", e)
            throw DatabaseException("Error while retrieving exam decryption key $examId")
        }
    }
}
