package nl.han.ica.examplatform.business.examquestion

import nl.han.ica.examplatform.controllers.responseExceptions.InvalidExamException
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

    @Autowired
    private
    lateinit var examDAO: ExamDAOStub

    @Autowired
    private
    lateinit var questionDAO: QuestionDAOStub

    fun checkQuestionNotNull(exam: Exam) {
        if (exam.questions == null)
            throw InvalidExamException("No questions sent. questions is empty.")
    }

    fun checkQuestion(questions: Array<Question>?) =
            questions?.let {
                it.iterator().forEach({ questionDAO.exists(it) })
            }


    fun addQuestionToExam(exam: Exam): ResponseEntity<Exam> {
        checkQuestion(exam.questions)


        val insertedObject = examDAO.updateExam(exam)
        return ResponseEntity(insertedObject, HttpStatus.ACCEPTED)
    }
}