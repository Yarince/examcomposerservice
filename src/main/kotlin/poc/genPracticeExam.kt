package poc

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.FileReader

fun main(args: Array<String>) {
    generateExam()
}

data class Question(val questionId: Int, val questionText: String, val categories: Array<String>, val type: String)

fun generateExam() {
    val questions = loadQuestions()
    questions.forEach { println(it) }
}

private fun loadQuestions(): Array<Question> {
    val reader = JsonReader(FileReader("src/main/kotlin/poc/resources/questionBankNotAnswered.json"))
    return Gson().fromJson(reader, Array<Question>::class.java)
}
