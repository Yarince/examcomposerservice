package poc

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import poc.models.Question
import poc.models.QuestionResultStats
import java.io.FileReader

internal fun loadQuestions(courseId: Int, studentNr: Int, type: String): Array<Question> {
    // Here the DB should get questions for courseId and studentNr
    val reader = JsonReader(FileReader("src/main/kotlin/pocmilo/resources/$type.json"))
    return Gson().fromJson(reader, Array<Question>::class.java)
}

internal fun loadResultsOfOthers(category: String): Array<QuestionResultStats> {
    val reader = JsonReader(FileReader("src/main/kotlin/pocmilo/resources/questionsMadeByOtherStudents.json"))
    return Gson().fromJson(reader, Array<QuestionResultStats>::class.java)
}