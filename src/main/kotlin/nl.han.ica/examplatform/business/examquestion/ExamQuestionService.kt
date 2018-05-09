package nl.han.ica.examplatform.business.examquestion

import nl.han.ica.examplatform.controllers.responseexceptions.InvalidExamException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.exam.ExamDAO
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ExamQuestionService {

    @Autowired
    private
    lateinit var examDAO: ExamDAO

    @Autowired
    private
    lateinit var questionDAO: QuestionDAO

    fun checkQuestion(questions: ArrayList<Question>?) {
        questions?.let {
            for (question in it)
                if (!questionDAO.exists(question))
                    throw InvalidExamException("Question ${question.questionId} does not exist.")
            return
        }
        throw InvalidExamException("Questions in exam are empty.")
    }

    fun addQuestionToExam(exam: Exam): ResponseEntity<Exam> {
        checkQuestion(exam.questions)

        val updatedObject = examDAO.addQuestionsToExam(exam)

        return ResponseEntity(updatedObject, HttpStatus.ACCEPTED)
    }
}