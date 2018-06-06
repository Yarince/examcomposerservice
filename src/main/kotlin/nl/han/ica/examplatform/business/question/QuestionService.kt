package nl.han.ica.examplatform.business.question

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.responseexceptions.CategoriesDontExistException
import nl.han.ica.examplatform.controllers.responseexceptions.DatabaseException
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.category.CategoryDAO
import nl.han.ica.examplatform.persistence.category.ICategoryDAO
import nl.han.ica.examplatform.persistence.question.IQuestionDAO
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

/**
 * Question service for handling requests related to the [Question] model.
 *
 * @param questionDAO [QuestionDAO] The QuestionDAO
 * @param categoryDAO [CategoryDAO] The CategoryDAO
 */
@Service
class QuestionService(
        private val questionDAO: IQuestionDAO,
        private val categoryDAO: ICategoryDAO) {

    private val logger = loggerFor(javaClass)

    /**
     * Add a new Question to the database, possibly with subquestions.
     *
     * @param question [Question] to be added in the database.
     * @return ResponseEntity<[Question]> with new question inserted and an assigned id.
     */
    fun addQuestion(question: Question): ResponseEntity<Question> =
            try {
                if(!categoryDAO.checkIfCategoriesExist(getAllCategoriesInQuestionAndSubQuestions(question))) throw CategoriesDontExistException("Categories dont exist")

                val insertedQuestion = questionDAO.insertQuestion(question)
                question.subQuestions?.let {
                    if (insertedQuestion.questionId == null) return@let
                    it.forEach {
                        addSubQuestions(it, insertedQuestion.questionId)
                    }
                }

                insertedQuestion.questionId?.let {
                    categoryDAO.addCategoriesToQuestion(question.categories, it)
                }

                ResponseEntity(insertedQuestion, HttpStatus.CREATED)
            } catch (exception: DatabaseException) {
                logger.error("Couldn't insert question: ${question.questionText}")
                ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
            }

    private fun addSubQuestions(question: Question, parentQuestionId: Int) {
        val insertedQuestion = questionDAO.insertQuestion(question, parentQuestionId)
        insertedQuestion.questionId?.let {
            categoryDAO.addCategoriesToQuestion(insertedQuestion.categories, it)
        }
        if (insertedQuestion.questionId == null) return
        if (question.subQuestions == null) return
        if (question.subQuestions.isEmpty()) return

        question.subQuestions.forEach {
            addSubQuestions(it, insertedQuestion.questionId)
        }
    }

    private fun getAllCategoriesInQuestionAndSubQuestions(question: Question, allCategories: ArrayList<String> = ArrayList()): ArrayList<String> {
        for (category in question.categories)
            if (!allCategories.contains(category))
                allCategories.add(category)

        if (question.subQuestions == null)
            return allCategories

        if (question.subQuestions.isEmpty())
            return allCategories


        question.subQuestions.forEach { return getAllCategoriesInQuestionAndSubQuestions(it, allCategories) }

        return allCategories
    }

    /**
     * Get all questions of a specific course.
     *
     * @param courseId [Int] ID of the course that the questions should be retrieved from.
     * @return [ResponseEntity]<[Array]<[Question]>> Contains the list with questions.
     */
    fun getQuestionsForCourse(courseId: Int): ResponseEntity<Array<Question>> =
            ResponseEntity(questionDAO.getQuestionsByCourse(courseId), HttpStatus.OK)

    /**
     * Get question by question Id.
     *
     * @param questionId [Int] ID of the question that you want retrieved.
     * @return [ResponseEntity]<[Question]> The question.
     */
    fun getQuestionForId(questionId: Int): ResponseEntity<Question> = ResponseEntity(questionDAO.getQuestionById(questionId), HttpStatus.OK)
}
