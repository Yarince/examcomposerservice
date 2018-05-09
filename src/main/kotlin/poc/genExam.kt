package poc

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.FileReader

fun main(args: Array<String>) {
    generateExam()
}

data class Question(val questionId: Int, val tags: String, val actualAnswer: String)

fun generateExam() {
    val questions = loadQuestions()

    val possibleSubjects = questions.groupBy { it.tags }
    possibleSubjects.forEach { println(it) }

}


fun loadQuestions(): Array<Question> {
    val gson = Gson()
    val reader = JsonReader(FileReader("src/main/kotlin/poc/resources/datasetQuestions.json"))
    return gson.fromJson(reader, Array<Question>::class.java)
}

