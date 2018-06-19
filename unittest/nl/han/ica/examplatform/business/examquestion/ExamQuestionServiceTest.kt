package nl.han.ica.examplatform.business.examquestion

import nl.han.ica.examplatform.controllers.exam.InvalidExamException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.exam.ExamDAO
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class ExamQuestionServiceTest {

    @InjectMocks
    lateinit var examQuestionService: ExamQuestionService

    @Mock
    lateinit var examDAO: ExamDAO

    @Mock
    lateinit var questionDAO: QuestionDAO

    @Test
    fun testCheckQuestion() {
        val question = Question(questionId = 1, questionType = "OpenQuestion", courseId = 1, examType = "Tentamen", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", questionTypePluginVersion = "1.0", partialAnswers = arrayListOf())
        doReturn(true).`when`(questionDAO).exists(question)
        examQuestionService.checkQuestion(arrayListOf(question))
        verify(questionDAO, times(1)).exists(question)
    }

    @Test(expected = InvalidExamException::class)
    fun testCheckQuestionEmpty() {
        examQuestionService.checkQuestion(null)
    }


    @Test(expected = InvalidExamException::class)
    fun testCheckQuestionNotExisting() {
        val question = Question(questionId = 1, questionType = "OpenQuestion", courseId = 1, examType = "Tentamen", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", questionTypePluginVersion = "1.0", partialAnswers = arrayListOf())
        doReturn(false).`when`(questionDAO).exists(question)
        examQuestionService.checkQuestion(arrayListOf(question))
    }

    @Test
    fun testAddQuestionToExam() {
        val expectedQuestion = Question(questionId = 0, questionOrderInExam = 1, questionType = "OpenQuestion", questionText = "name", courseId = 1, examType = "Tentamen", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", questionTypePluginVersion = "1.0", partialAnswers = arrayListOf())
        val expectedExam = Exam(examId = 1, name = "name-0", durationInMinutes = 10, startTime = Date(6000), courseId = 1, version = 1, examType = "Tentamen", questions = arrayListOf(expectedQuestion))

        doReturn(expectedExam).`when`(examDAO).addQuestionsToExam(expectedExam)
        doReturn(true).`when`(questionDAO).exists(expectedQuestion)

        assertEquals(ResponseEntity(expectedExam, HttpStatus.ACCEPTED), examQuestionService.addQuestionToExam(expectedExam))
    }

    @Test
    fun testRemoveQuestionsFromExam() {
        val examID = 1
        val questionIDs: Array<Int> = arrayOf(1, 2, 3)

        doReturn(false).`when`(questionDAO).answersGivenOnQuestions(questionIDs)

        examQuestionService.removeQuestionsFromExam(examID, questionIDs)
        verify(examDAO,
                times(1))
                .removeQuestionsFromExam(examID, questionIDs)
    }

    @Test(expected = InvalidExamException::class)
    fun testRemoveQuestionsFromExamNoQuestionIds() {
        val examID = 1
        val questionIDs: Array<Int> = arrayOf()

        examQuestionService.removeQuestionsFromExam(examID, questionIDs)
    }

    @Test(expected = InvalidExamException::class)
    fun testRemoveQuestionsFromExamAnswersGivenOnQuestion() {
        val examID = 1
        val questionIDs: Array<Int> = arrayOf(1, 2, 3)

        doReturn(true).`when`(questionDAO).answersGivenOnQuestions(questionIDs)

        examQuestionService.removeQuestionsFromExam(examID, questionIDs)
    }

    @Test
    fun testChangeQuestionOrderInExam(){
        val examID = 1
        val questionsAndSequenceNumbers: Array<Pair<Int, Int>> = arrayOf(
                Pair(1, 3),
                Pair(2, 1),
                Pair(3, 2))

        examQuestionService.changeQuestionOrderInExam(examID, questionsAndSequenceNumbers)

        verify(examDAO,
                times(1))
                .changeQuestionOrderInExam(examID, questionsAndSequenceNumbers)
    }
}