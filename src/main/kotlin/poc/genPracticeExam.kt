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
    val ratedCategories = questionsToCategoryRating(questions)
    ratedCategories.forEach { println(it) }
}

private fun loadQuestions(): Array<Question> {
    val reader = JsonReader(FileReader("src/main/kotlin/poc/resources/questionBankNotAnswered.json"))
    return Gson().fromJson(reader, Array<Question>::class.java)
}

private fun questionsToCategoryRating(questions: Array<Question>): HashMap<String, Double> {
    // This will be implemented by another team member, so this is a stub that returns the categories and ratings
    val categories = ArrayList<String>()
    for (question in questions) {
        for (category in question.categories) {
            if (!categories.contains(category))
                categories.add(category)
        }
    }

    val ratedCategories = HashMap<String, Double>()
    categories.forEachIndexed { index, category ->
        ratedCategories[category] = (index + 1) * 25.0
    }

    return ratedCategories
}
