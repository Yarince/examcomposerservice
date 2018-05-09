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
    println(possibleSubjects)
    val possibleSubjectsKeysArray = questions.groupBy { it.tags }.keys


    // The list of which the questions should be added to
    val practiceExam = ArrayList<Question>()

    // Random:
    // ThreadLocalRandom.current().nextInt(0, max)

    addQuestionsToExam(questions, practiceExam, possibleSubjects, possibleSubjectsKeysArray.toList())
    practiceExam.forEach {
        println(it)
    }
}

fun addQuestionsToExam(questions: Array<Question>, exam: ArrayList<Question>, possibleSubjects: Map<String, List<Question>>, possibleSubjectsKeysArray: List<String>, currentSubject: String? = null) {
    // If the exam contains 50% of the questions, exit this function
    if (exam.size > 0) if (exam.size % (questions.size / 2) == 0) return println("List should be full")

    val currentSubjectNN = possibleSubjects[possibleSubjectsKeysArray[0]]

    currentSubjectNN?.let {
        // add Random question if not null
        // todo: only add if half of these questions are not added yet
        exam.add(it[ThreadLocalRandom.current().nextInt(0, it.size)])
    }

    //val iteration = if (possibleSubjects.values
    addQuestionsToExam(questions, exam, possibleSubjects, possibleSubjectsKeysArray)
}


private fun loadQuestions(): Array<Question> {
    val gson = Gson()
    val reader = JsonReader(FileReader("src/main/kotlin/poc/resources/datasetQuestions.json"))
    return gson.fromJson(reader, Array<Question>::class.java)
}

