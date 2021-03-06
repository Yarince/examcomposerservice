package nl.han.ica.examplatform.business.examquestion

import nl.han.ica.examplatform.config.logger.loggerFor
import nl.han.ica.examplatform.controllers.exam.InvalidExamException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.exam.ExamDAO
import nl.han.ica.examplatform.persistence.exam.IExamDAO
import nl.han.ica.examplatform.persistence.question.IQuestionDAO
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

/**
 * Service class for handling the interoperability between the [Exam] model and the [Question] models within the exam.
 *
 * @param examDAO [ExamDAO] The ExamDAO
 * @param questionDAO [QuestionDAO] The QuestionDAO
 */
@Service
class ExamQuestionService(
        private val examDAO: IExamDAO,
        private val questionDAO: IQuestionDAO
) {
    private val logger = loggerFor(javaClass)

    /**
     * Validate if all Questions exist.
     *
     * @param questions Array of [Question]s to be checked
     * @throws InvalidExamException If one of the questions does not exist or are empty
     */
    fun checkQuestion(questions: ArrayList<Question>?) {
        questions?.let {
            for (question in it)
                if (!questionDAO.exists(question)) {
                    logger.error("Tried to add a question without questionId to an exam")
                    throw InvalidExamException("Question ${question.questionId} does not exist.")
                }
            return
        }
        logger.error("No questions given to add to exam")
        throw InvalidExamException("Questions in exam are empty.")
    }

    /**
     * Assign a Question to an existing Exam in the database.
     *
     * @param exam [Exam] to be updated in the database
     * @return ResponseEntity<[Question]> updated object from database
     */
    fun addQuestionToExam(exam: Exam): ResponseEntity<Exam> {
        checkQuestion(exam.questions)

        val updatedObject = examDAO.addQuestionsToExam(exam)

        return ResponseEntity(updatedObject, HttpStatus.ACCEPTED)
    }

    /**
     * Changes the order of questions in an exam
     *
     * @param examId [Int] The ID of the exam
     * @param questionsAndSequenceNumbers [Array]<[Pair]<[Int], [Int]>> An array containing the questionIds and the new sequence number
     */
    fun changeQuestionOrderInExam(examId: Int, questionsAndSequenceNumbers: Array<Pair<Int, Int>>) =
            examDAO.changeQuestionOrderInExam(examId, questionsAndSequenceNumbers)

    /**
     * De-couples questions from an exam.
     *
     * @param examId [Int] The ID of the exam
     * @param questionIds [Array]<[Int]> Array containing the IDs of the questions that should be removed
     */
    fun removeQuestionsFromExam(examId: Int, questionIds: Array<Int>) {
        if (questionIds.isEmpty())
            throw InvalidExamException("Can't remove any questions if no ID's are provided")

        if (questionDAO.answersGivenOnQuestions(questionIds))
            throw InvalidExamException("Can't remove questions if students gave answers to any of these.")

        return examDAO.removeQuestionsFromExam(examId, questionIds)
    }
}
