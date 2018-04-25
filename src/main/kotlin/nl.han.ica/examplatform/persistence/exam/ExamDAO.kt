package nl.han.ica.examplatform.persistence.exam

import nl.han.ica.examplatform.models.exam.SimpleExam
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.lang.reflect.Array.setInt


@Repository
class ExamDAO {

    fun getExams(): ArrayList<SimpleExam> {
        val dbConnection: Connection? = MySQLConnection.getConnection()
        val sqlQueryStringInsertString = "SELECT EXAMID, EXAMNAME, COURSECODE FROM EXAM INNER JOIN COURSE ON EXAM.COURSEID = COURSE.COURSEID"
        val preparedStatement: PreparedStatement?
        preparedStatement = dbConnection?.prepareStatement(sqlQueryStringInsertString)

        val result = ArrayList<SimpleExam>()
        try {
            val resultSet = preparedStatement?.executeQuery()

            while (resultSet!!.next()) {
                result.add(SimpleExam(resultSet.getInt("ExamID"),
                        resultSet.getString("ExamName"),
                        resultSet.getString("CourseCode")))
            }

        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            preparedStatement?.close()
            dbConnection?.close()
        }

        return result
    }

}