package nl.han.ica.examplatform.business.exam.practice

import nl.han.ica.examplatform.business.exam.practice.models.Question


internal fun questionsToSortedCategoryRating(questions: Array<Question>): List<Pair<String, Double>> {
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

    return ratedCategories.sortByValue()
}

fun HashMap<String, Double>.sortByValue(): List<Pair<String, Double>> {
    return this.toList().sortedBy { (_, value) -> value }
}