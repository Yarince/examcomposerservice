package poc

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.FileReader
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

fun addQuestionsToExam(questions: Array<Question>, exam: ArrayList<Question>, possibleSubjects: Map<String, List<Question>>, possibleSubjectsKeysArray: List<String>, iterator: Int = 0, iteratorForward: Boolean = true) {
    // If the exam contains 50% of the questions, exit this function
    if (exam.size > 0) if (exam.size % (questions.size / 1) == 0) return println("Exit recursive function")

    // Gets the list of questions in the current subject
    val currentSubjectList = possibleSubjects[possibleSubjectsKeysArray[iterator]]

    // If it's not null, add a random question to the exam
    currentSubjectList?.let {
        // todo: check if question hasnt been added yet
        val randomNumber = ThreadLocalRandom.current().nextInt(0, it.size)
        exam.add(it[randomNumber])
        // remove question from list of possible questions
        // val mutableSubjectList = possibleSubjects[possibleSubjectsKeysArray[iterator]]?.toMutableList()
        // mutableSubjectList?.remove(0)
    }

    // This makes it so the questions are cycled between subjects
    val newIt = if (iteratorForward) iterator + 1 else iterator - 1

    var newItForward: Boolean = iteratorForward
    if (newIt >= possibleSubjectsKeysArray.size - 1)
        newItForward = false
    else if (newIt < 1)
        newItForward = true

    // Recursively add more questions
    addQuestionsToExam(questions, exam, possibleSubjects, possibleSubjectsKeysArray, newIt, newItForward)
}


private fun loadQuestions(): Array<Question> {
    val reader = JsonReader(FileReader("src/main/kotlin/poc/resources/datasetQuestions.json"))
    return Gson().fromJson(reader, Array<Question>::class.java)
}
