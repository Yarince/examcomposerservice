package nl.han.ica.examplatform.business.question

import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class QuestionService {

    @Autowired
    private lateinit var questionDAO: QuestionDAO

    /**
     * Add a new Question to the database
     *
     * @param question [Question] to be added in the database
     * @return ResponseEntity<[Question]> with new question inserted and an assigned id
     */
    fun addQuestion(question: Question): ResponseEntity<Question> {
        return try {
            val insertedQuestion = questionDAO.insertQuestion(question)
            ResponseEntity(insertedQuestion, HttpStatus.CREATED)
        } catch (exception: Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Get all questions of a specific course
     *
     * @param courseId [Int] ID of the course that the questions should be retrieved from
     * @return [ResponseEntity]<[Array]<[Question]>> Contains the list with questions
     */
    fun getQuestionsForCourse(courseId: Int): ResponseEntity<Array<Question>> {
        return ResponseEntity(questionDAO.getQuestions(courseId), HttpStatus.CREATED)
    }

    /**
     * Get question by question Id.
     *
     * @param questionId [Int] ID of the question that you want retrieved.
     * @return [ResponseEntity]<[Question]> The question.
     */
    fun getQuestionForId(questionId: Int): ResponseEntity<Question> = ResponseEntity(questionDAO.getQuestion(questionId), HttpStatus.OK)
}
