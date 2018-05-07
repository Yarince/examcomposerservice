package nl.han.ica.examplatform.controllers

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import nl.han.ica.examplatform.models.course.CourseType
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.exam.SimpleExam
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.models.question.QuestionType
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.PropertySource
import org.springframework.http.*
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.client.postForEntity
import org.springframework.web.util.UriComponentsBuilder
import java.sql.Connection
import java.sql.PreparedStatement
import java.util.*


@RunWith(SpringJUnit4ClassRunner::class)
@PropertySource("classpath:application.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExamControllerIntegrationTest {

    @Value("\${local.server.port}")
    var port: Int = 0

    val restTemplate = RestTemplate()

    private var databaseConnection: Connection? = null

    private val testQuestion = Question(questionId = -99999999)
    private val testExam = Exam(-99999999, "TestExam", 0, Date(1000), Date(1000), "APP", CourseType.APP, 1, ExamType.EXAM, "Use a calculator", "Nijmegen", arrayListOf(testQuestion))

    @Before
    @Transactional
    fun setUp() {
        databaseConnection = MySQLConnection.getConnection()
        // Insert some testdata that will be rolled back later
        val insertQuestionQuery = "INSERT INTO QUESTION (QUESTIONID, EXAMTYPEID, COURSEID, QUESTIONTYPE) VALUES (${testQuestion.questionId}, 1, 1, '${testQuestion.questionType.fieldName}')"
        val insertExamQuery = "INSERT INTO EXAM (EXAMID, COURSEID, EXAMTYPEID, EXAMNAME, EXAMCODE) VALUES (${testExam.examId}, ${testExam.courseId.value}, ${testExam.examType.examId}, '${testExam.name}', '${testExam.name}')"
        val preparedStatementQuestion = databaseConnection?.prepareStatement(insertQuestionQuery)
        preparedStatementQuestion?.executeUpdate()

        val preparedStatementExam = databaseConnection?.prepareStatement(insertExamQuery)
        preparedStatementExam?.executeUpdate()
    }

    @After
    @Rollback
    fun tearDown() {
        databaseConnection = MySQLConnection.getConnection()
        val deleteQuestionQuery = "DELETE from QUESTION where QuestionID = ${testQuestion.questionId}"
        val deleteExamQuery = "DELETE FROM EXAM WHERE EXAMID = ${testExam.examId}"
        val preparedStatementQuery: PreparedStatement? = databaseConnection?.prepareStatement(deleteQuestionQuery)
        preparedStatementQuery?.executeUpdate()

        val preparedStatementExam: PreparedStatement? = databaseConnection?.prepareStatement(deleteExamQuery)
        preparedStatementExam?.executeUpdate()
    }

    @Test
    fun testGetExams() {
        val expected = SimpleExam(testExam.examId!!, testExam.name, testExam.course)
        val builder = UriComponentsBuilder.fromHttpUrl("http://localhost:$port/exams")
        val entity = HttpEntity<Any>(HttpHeaders())

        val response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                Array<SimpleExam>::class.java)

        assertNotNull(response.body)
        assertEquals(response.statusCode, HttpStatus.OK)
        var success = false
        for (item in response.body!!) {
            if (item.examId == expected.examId) success = true
        }

        assertEquals(true, success)
    }

    @Test
    fun testAddExam() {
        val headers = HttpHeaders()
        val requestJson = """{
                                "examId":null,
                                "name":"name-0",
                                "durationInMinutes":10,
                                "startTime":"1970-01-01T00:00:06.000+0000",
                                "course":"APP",
                                "examType":"EXAM",
                                "examId":null,
                                "instructions":null,
                                "location":null,
                                "questions":null
                              }"""

        headers.contentType = MediaType.APPLICATION_JSON
        val entity = HttpEntity(requestJson, headers)
        val result = restTemplate.postForEntity<Exam>("http://localhost:$port/exams", entity)

        assertEquals(HttpStatus.CREATED, result.statusCode)
        assertEquals(Exam(name = "name-0", durationInMinutes = 10, startTime = Date(6000), course = "APP", version = 1, examType = ExamType.EXAM)
                , result.body)
    }

    @Test
    fun testAddQuestionToExam() {
        val headers = HttpHeaders()
        val requestJson = """{
                                "examId":1,
                                "name":"name-0",
                                "durationInMinutes":10,
                                "startTime":"1970-01-01T00:00:06.000+0000",
                                "course":"APP",
                                "examType":"EXAM",
                                "instructions":null,
                                "location":null,
                                "questions":[
                                {
                                    "questionId": 1,
                                    "questionText": "Openvraag text",
                                    "questionType": "OPEN_QUESTION",
                                    "course": "APP",
                                    "subId": null,
                                    "examType": "EXAM",
                                    "subQuestions": null
                                    }
                                ]
                            }"""

        headers.contentType = MediaType.APPLICATION_JSON
        val entity = HttpEntity(requestJson, headers)
        val result = restTemplate.exchange<Exam>("http://localhost:$port/exams", HttpMethod.PUT, entity)

        assertNotNull(result)
        assertEquals(result.statusCode, HttpStatus.ACCEPTED)
        assertEquals(Exam(
                examId = 1,
                name = "name-0",
                durationInMinutes = 10,
                startTime = Date(6000),
                course = "APP",
                examType = ExamType.EXAM,
                questions = arrayListOf(
                        Question(1,
                                "Openvraag text",
                                QuestionType.OPEN_QUESTION,
                                "APP",
                                null, ExamType.EXAM,
                                null)))
                , result.body)
    }

    @Test
    fun testGetExamSuccess() {
        val requestParamExamId = 1

        val headers = HttpHeaders()
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE)

        val builder = UriComponentsBuilder.fromHttpUrl("http://localhost:$port/exams/$requestParamExamId")

        val entity = HttpEntity<Any>(headers)

        val response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                Exam::class.java)

        assertNotNull(response.body)
        assertEquals(response.statusCode, HttpStatus.OK)
        assertEquals(requestParamExamId, response.body?.examId)
    }
}
