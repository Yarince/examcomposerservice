package poc

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.FileReader
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList

fun main(args: Array<String>) {
    generateExam()
}

data class Question(val questionId: Int, val tags: String, val actualAnswer: String)

fun generateExam() {
    val questions = loadQuestions()

    // Group questions by tag
    val possibleSubjects = questions.groupBy { it.tags }

    // The list of which the questions should be added to
    val practiceExam = ArrayList<Question>()

    possibleSubjects.forEach {
        println(practiceExam.size % (questions.size / 2))
        if (practiceExam.size > 0) if (practiceExam.size % (questions.size / 2) == 0) println("this")
        practiceExam.add(it.value[ThreadLocalRandom.current().nextInt(0, it.value.size)])
    }

    practiceExam.forEach {
        println(it)
    }


}

fun addQuestionsToExam(questions: Array<Question>, exam: ArrayList<Question>) {

}


fun loadQuestions(): Array<Question> {
    val gson = Gson()
    val reader = JsonReader(FileReader("src/main/kotlin/poc/resources/datasetQuestions.json"))
    return gson.fromJson(reader, Array<Question>::class.java)
}

