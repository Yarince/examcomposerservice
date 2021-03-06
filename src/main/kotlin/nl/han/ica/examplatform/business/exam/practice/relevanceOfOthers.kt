package nl.han.ica.examplatform.business.exam.practice

import nl.han.ica.examplatform.models.question.QuestionResult
import nl.han.ica.examplatform.models.question.QuestionResultStats
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.exam.results.IExamResultsDAO
import nl.han.ica.examplatform.persistence.question.IQuestionDAO

/**
 * Here the results of other students on this subject should be analysed.
 * After this the worst performed question is returned.
 *
 * @param category the category of which the question should be about
 * @return [Question] the first made question of the student
 */
internal fun getMostRelevantNotAssessedQuestionOfCategory(category: String, questions: ArrayList<Question>, examResultsDAO: IExamResultsDAO, courseId: Int, studentNr: Int): Question? {
    val assessedQuestionsOfOthers: ArrayList<QuestionResultStats> = examResultsDAO.getResultsOfOthersInCategory(studentNr, category)

    // Filter questions to only contain current category
    val questionsOfCategory = questions.filter { it.categories.contains(category) }
    val questionIdsOfCategory = questionsOfCategory.map { it.questionId }
    val filteredQuestions = assessedQuestionsOfOthers.filter { questionIdsOfCategory.contains(it.questionId) }

    var mostRelevantQuestion: Pair<Int, Double>? = null

    if (filteredQuestions.isEmpty() && questionsOfCategory.isNotEmpty()) {
        // add question that hasn't been answered by anyone
        questionsOfCategory[0].questionId?.let {
            mostRelevantQuestion = Pair(it, 100.0)
        }
    } else {
        filteredQuestions.forEach {
            // The lower the rating, the more relevant the question is because of the low score on the question
            val rating = it.nCorrect.toDouble() / it.nResults

            mostRelevantQuestion?.let {
                if (rating < it.second)
                    mostRelevantQuestion = Pair(it.first, rating)
            } ?: run {
                mostRelevantQuestion = Pair(it.questionId, rating)
            }
        }
    }

    return questions.find { it.questionId == mostRelevantQuestion?.first }
}

internal fun getFirstAskedQuestion(questionsInCategory: List<QuestionResult>, questionDAO: IQuestionDAO): Question {
    return questionDAO.getQuestionById(questionsInCategory.sortedBy { it.submittedExamId }.first().questionId)
}