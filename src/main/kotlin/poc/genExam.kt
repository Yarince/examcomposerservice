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
    // Put all subject keys in a list
    val possibleSubjectsKeysArray = questions.groupBy { it.tags }.keys.toList()

    // The list of which the questions should be added to
    val practiceExam = ArrayList<Question>()

    // Recursively add questions to exam
    addQuestionsToExam(questions, practiceExam, possibleSubjects, possibleSubjectsKeysArray)

    // Print exam contents
    practiceExam.forEach {
        println(it)
    }
}

fun addQuestionsToExam(questions: Array<Question>, exam: ArrayList<Question>, possibleSubjects: Map<String, List<Question>>, possibleSubjectsKeysArray: List<String>, iterator: Int = 0) {
    // If the exam contains 50% of the questions, exit this function
    if (exam.size > 0) if (exam.size % (questions.size / 2) == 0) return println("List should be full")

    println("1")
    val currentSubjectNN = possibleSubjects[possibleSubjectsKeysArray[iterator]]

    currentSubjectNN?.let {
        // add Random question if not null
        // todo: only add if half of these questions are not added yet
        // val examGroupedByTags = exam.groupBy { it.tags }
        // examGroupedByTags[possibleSubjectsKeysArray[iterator]]

        exam.add(it[ThreadLocalRandom.current().nextInt(0, it.size)])
    }
    //val iteration = if (possibleSubjects.values
    val newIterator = if (iterator == possibleSubjectsKeysArray.size - 1) iterator - 1 else iterator + 1
    addQuestionsToExam(questions, exam, possibleSubjects, possibleSubjectsKeysArray, newIterator)
}


private fun loadQuestions(): Array<Question> {
    val gson = Gson()
    val reader = JsonReader(FileReader("src/main/kotlin/poc/resources/datasetQuestions.json"))
    return gson.fromJson(reader, Array<Question>::class.java)
}

