package nl.han.ica.examplatform.business.exam.practice

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import nl.han.ica.examplatform.business.exam.practice.models.Question
import nl.han.ica.examplatform.business.exam.practice.models.QuestionResultStats
import java.io.FileReader

internal fun loadQuestions(courseId: Int, studentNr: Int, type: String): Array<Question> {
    // Here the DB should get questions for courseId and studentNr
    val reader = JsonReader(FileReader("src/main/kotlin/nl.han.ica.examplatform.business.exam.practice/resources/$type.json"))
    return Gson().fromJson(reader, Array<Question>::class.java)
}

object QuestionLoader {
    private var results: Array<QuestionResultStats>? = null

    fun getResultsOfOthers(): Array<QuestionResultStats> {
        return if (this.results == null) {
            val reader = JsonReader(FileReader("src/main/kotlin/nl.han.ica.examplatform.business.exam.practice/resources/questionsMadeByOtherStudents.json"))
            results = Gson().fromJson(reader, Array<QuestionResultStats>::class.java)
            results!!
        } else {
            results!!
        }
    }
}