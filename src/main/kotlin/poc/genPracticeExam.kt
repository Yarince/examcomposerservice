package poc

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.FileReader
import java.util.concurrent.ThreadLocalRandom

data class Question(val questionId: Int, val questionText: String, val categories: Array<String>, val type: String)

fun main(args: Array<String>) {
    generateExam(1, 1)
}

fun generateExam(courseId: Int, studentNr: Int) {
    val questions = loadQuestions(courseId, studentNr)
    val ratedCategories = questionsToCategoryRating(questions)

    ratedCategories.forEach { println(it) }
}

private fun loadQuestions(courseId: Int, studentNr: Int): Array<Question> {
    // Here the DB should get questions for courseId and studentNr
    val reader = JsonReader(FileReader("src/main/kotlin/poc/resources/questionBankNotAnswered.json"))
    return Gson().fromJson(reader, Array<Question>::class.java)
}

private fun addQuestionToExam(studentNr: Int, allQuestions: Array<Question>, ratedCategories: HashMap<String, Double>, currentCategory: String) {
    if (!ratedCategories.containsKey(currentCategory)) return

    if (determineIfQuestionOfCategoryWillBeAdded(ratedCategories[currentCategory]!!)) {

        val questionToAdd = getMostRelevantNotAssessedQuestionOfCategory(currentCategory)
                ?: getFirstAskedQuestion(currentCategory, studentNr)

        //todo: add question to exam
    } else {
        val nextCategory = "Todo" // todo: determine next category
        addQuestionToExam(studentNr, allQuestions, ratedCategories, nextCategory)
    }
}

private fun determineIfQuestionOfCategoryWillBeAdded(chanceToGetAdded: Double): Boolean {
    val randomNumber = ThreadLocalRandom.current().nextDouble(0.0, 99.99)
    return randomNumber < chanceToGetAdded
}