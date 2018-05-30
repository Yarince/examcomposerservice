package nl.han.ica.examplatform.business.question

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class QuestionService {
    private val logger = loggerFor(javaClass)

    @Autowired
    private lateinit var questionDAO: QuestionDAO

    /**
     * Add a new Question to the database, possibly with subquestions.
     *
     * @param question [Question] to be added in the database.
     * @return ResponseEntity<[Question]> with new question inserted and an assigned id.
     */
    fun addQuestion(question: Question): ResponseEntity<Question> {
        return try {
            val insertedQuestion = questionDAO.insertQuestion(question)
            question.subQuestions?.let {
                if (insertedQuestion.questionId == null) return@let
                it.forEach {
                    addSubQuestions(it, insertedQuestion.questionId)
                }
            }
            ResponseEntity(insertedQuestion, HttpStatus.CREATED)
        } catch (exception: Exception) {
            logger.error("Couldn't insert question: ${question.questionText}")
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    private fun addSubQuestions(question: Question, parentQuestionId: Int) {
        val insertedQuestion = questionDAO.insertQuestion(question, parentQuestionId)

        if (insertedQuestion.questionId == null) return
        if (question.subQuestions == null) return
        if (question.subQuestions.isEmpty()) return

        question.subQuestions.forEach {
            addSubQuestions(it, insertedQuestion.questionId)
        }
    }

    /**
     * Get all questions of a specific course.
     *
     * @param courseId [Int] ID of the course that the questions should be retrieved from.
     * @return [ResponseEntity]<[Array]<[Question]>> Contains the list with questions.
     */
    fun getQuestionsForCourse(courseId: Int): ResponseEntity<Array<Question>> {
        return ResponseEntity(questionDAO.getQuestions(courseId), HttpStatus.OK)
    }
}