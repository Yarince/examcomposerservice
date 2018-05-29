package nl.han.ica.examplatform.persistence.question

import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

@Repository
class QuestionTypeDAO {
    /**
     * This function gets a list of all questionTypes from the database.
     *
     * @return [ArrayList]<[String]> List of questionTypes
     */
    fun getAllQuestionTypes(): ArrayList<String> {
        val dbConnection: Connection? = MySQLConnection.getConnection()
        val pluginNameQuery = "SELECT PluginName FROM `PLUGIN`"
        val preparedStatement: PreparedStatement? = dbConnection?.prepareStatement(pluginNameQuery)

        val result = arrayListOf<String>()
        try {
            val rs = preparedStatement?.executeQuery()
            while (rs!!.next()) {
                result.add(rs.getString("PluginName"))
            }

        } catch (e: SQLException) {
            e.printStackTrace()
            print(e)
        } finally {
            MySQLConnection.closeStatement(preparedStatement)
            MySQLConnection.closeConnection(dbConnection)
        }
        return result
    }
}