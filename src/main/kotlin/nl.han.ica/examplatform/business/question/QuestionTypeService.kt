package nl.han.ica.examplatform.business.question

import nl.han.ica.examplatform.persistence.question.QuestionTypeDAO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class QuestionTypeService(private val questionTypeDAO: QuestionTypeDAO) {

    /**
     * Get all questionTypes from the database.
     *
     * @return [ResponseEntity]<ArrayList<String>> List of questionTypes.
     */
    fun getQuestionTypes(): ResponseEntity<ArrayList<String>> = ResponseEntity(questionTypeDAO.getAllQuestionTypes(), HttpStatus.OK)
}