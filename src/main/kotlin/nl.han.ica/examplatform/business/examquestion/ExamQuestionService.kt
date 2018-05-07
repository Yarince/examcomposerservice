package nl.han.ica.examplatform.business.examquestion

import nl.han.ica.examplatform.controllers.responseexceptions.InvalidExamException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.exam.ExamDAOStub
import nl.han.ica.examplatform.persistence.question.QuestionDAOStub
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ExamQuestionService {

    /**
     * Connection to database for [Exam]
     */
    @Autowired
    private lateinit var examDAO: ExamDAOStub

    /**
     * Connection to database for [Question]
     */
    @Autowired
    private lateinit var questionDAO: QuestionDAOStub

    /**
     * Validate if all Questions exist
     *
     * @param questions Array of [Question]s to be checked
     * @throws InvalidExamException If one of the questions does not exist or are empty
     */
    fun checkQuestion(questions: Array<Question>?) {
        questions?.let {
            for (question in it)
                if (!questionDAO.exists(question))
                    throw InvalidExamException("Question ${question.questionId} does not exist.")
            return
        }
        throw InvalidExamException("Questions in exam are empty.")
    }

    /**
     * Assign a Question to an existing Exam the database
     *
     * @param exam [Exam] to be updated in the database
     * @return ResponseEntity<[Question]> updated object from database
     */
    fun addQuestionToExam(exam: Exam): ResponseEntity<Exam> {
        checkQuestion(exam.questions)

        val updatedObject = examDAO.updateExam(exam)

        return ResponseEntity(updatedObject, HttpStatus.ACCEPTED)
    }
}