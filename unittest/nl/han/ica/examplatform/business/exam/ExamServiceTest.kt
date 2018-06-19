package nl.han.ica.examplatform.business.exam

import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import nl.han.ica.examplatform.controllers.exam.InvalidExamException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.SimpleExam
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.category.CategoryDAO
import nl.han.ica.examplatform.persistence.exam.ExamDAO
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*


@RunWith(MockitoJUnitRunner::class)
internal class ExamServiceTest {

    @InjectMocks
    private lateinit var examService: ExamService

    @Mock
    private lateinit var examDAO: ExamDAO

    // This is unused, but without it the tests fail because of injection of this.
    @Mock
    private lateinit var questionDAO: QuestionDAO

    @Mock
    private lateinit var categoryDAO: CategoryDAO

    @Test
    fun testGetExams() {
        val expected = arrayListOf(SimpleExam(1, "SWA Toets 1", 1),
                SimpleExam(2, "SWA Toets 2", 1),
                SimpleExam(3, "APP Toets algoritmen", 2)
        )

        doReturn(expected).`when`(examDAO).getExams()

        val result = examService.getExams()
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.OK), result)
    }

    @Test(expected = InvalidExamException::class)
    fun testCheckExamEmptyId() {
        // Faulty exam object
        val exam = Exam(
                5,
                "name-0",
                10,
                Date(6000),
                courseId = 1,
                version = 1,
                examType = "Tentamen")

        examService.checkExam(exam)
    }

    @Test(expected = InvalidExamException::class)
    fun testCheckExamEmptyQuestions() {
        // Faulty exam object
        val exam = Exam(
                null,
                "name-0",
                10,
                Date(6000),
                courseId = 1,
                version = 1,
                examType = "Exam",
                questions = arrayListOf(Question(
                        questionType = "OpenQuestion",
                        courseId = 1,
                        examType = "Tentamen",
                        answerType = "OpenQuestion",
                        answerTypePluginVersion = "1.0",
                        questionTypePluginVersion = "1.0",
                        partialAnswers = arrayListOf())))

        examService.checkExam(exam)
    }
    @Test
    fun testAddExam() {
        val examInserted = Exam(
                name = "name-0",
                durationInMinutes = 10,
                startTime = Date(6000),
                courseId = 1,
                examType = "Tentamen")

        val expectedResult: ResponseEntity<Exam> = ResponseEntity(examInserted, HttpStatus.CREATED)
        doReturn(examInserted).`when`(examDAO).insertExam(examInserted)
        val result: ResponseEntity<Exam> = examService.addExam(examInserted)
        assertEquals(expectedResult, result)
    }

    @Test
    fun testGetExam() {
        val idOfExamToGet = 1
        val expected = Exam(name = "name-0", durationInMinutes = 10, startTime = Date(6000), courseId = 1, examType = "Tentamen", examId = idOfExamToGet, questions = arrayListOf())
        doReturn(expected).`when`(examDAO).getExam(idOfExamToGet)
        val result = examService.getExam(idOfExamToGet)
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.OK), result)
    }

    @Test
    fun testAddClassesToExam() {
        val examIdToAddClassesTo = 1
        val classesToAddToExam = arrayListOf("3EJAAR", "4EJAAR", "1EJAAR")
        val expected = HttpStatus.ACCEPTED
        doReturn(expected).`when`(examDAO).addClassesToExam(examIdToAddClassesTo, classesToAddToExam)
        val result = examService.addClassesToExam(examIdToAddClassesTo, classesToAddToExam)
        assertNotNull(result)
        assertEquals(ResponseEntity(expected, HttpStatus.ACCEPTED), result)
    }

    @Test
    fun testDeleteExam(){
        val examId = 1

        examService.deleteExam(examId)
        verify(examDAO,
                times(1))
                .deleteExam(examId)
    }

    @Test
    fun testPublishExamTrue(){
        val examID = 1
        val shouldBePublished = true

        examService.publishExam(examID, shouldBePublished)
        verify(examDAO,
                times(1))
                .publishExam(examID, shouldBePublished)
    }

    @Test
    fun testPublishExamFalse(){
        val examID = 1
        val shouldBePublished = false

        examService.publishExam(examID, shouldBePublished)
        verify(examDAO,
                times(1))
                .publishExam(examID, shouldBePublished)
    }

    @Test
    fun updateExam(){
        val exam = Exam(
                examId = 1,
                name = "Exam",
                durationInMinutes = 60,
                startTime = Date(6000),
                endTime = Date(8000),
                courseId = 1,
                version = 2,
                examType = "exam",
                instructions = "No instructions",
                location = "Arnhem",
                readyForDownload = false,
                questions = arrayListOf(Question(
                        questionType = "OpenQuestion",
                        courseId = 1,
                        examType = "Tentamen",
                        answerType = "OpenQuestion",
                        answerTypePluginVersion = "1.0",
                        questionTypePluginVersion = "1.0",
                        partialAnswers = arrayListOf())),
                decryptionCodes = "decryptionCodes",
                classes = arrayListOf("class 1", "class 2"))
        val expected: ResponseEntity<Exam> = ResponseEntity(exam, HttpStatus.ACCEPTED)

        doReturn(exam).`when`(examDAO).updateExam(exam)

        val result: ResponseEntity<Exam> = examService.updateExam(exam)
        verify(examDAO,
                times(1))
                .updateExam(exam)
        assertEquals(result, expected)
    }
}