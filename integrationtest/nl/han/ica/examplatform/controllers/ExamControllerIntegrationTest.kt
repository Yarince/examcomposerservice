package nl.han.ica.examplatform.controllers

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
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
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.client.postForEntity
import org.springframework.web.util.UriComponentsBuilder
import org.springframework.transaction.annotation.Transactional
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


    var testExam: SimpleExam? = null


    //TODO Make test data
    val testExamArray = arrayOf(
            Exam(1, "SWA Toets 1", 100,null,"SWA"),
            Exam(2, "SWA Toets 1", "SWA"),
            Exam(3, "APP Toets algoritmen", "APP")
    )

    private var conn: Connection? = null

    @Before
    @Transactional
    fun setUp() {

        conn = MySQLConnection.getConnection()

        // Clear the DB for this test
        val sqlDeleteString = "delete from EXAM; delete from COURSE"
        val preparedStatement1 = conn?.prepareStatement(sqlDeleteString)
        preparedStatement1?.executeUpdate()

        //TODO INSERT new COURSE


        for (exam in testExamArray) {
        //TODO INSERT CORRECT DATA
            val insertExamQuery = "INSERT INTO EXAM ( EXAMID, EXAMNAME,COURSEID) VALUES (?, ?, ?)"

            val preparedStatement2: PreparedStatement? = conn?.prepareStatement(insertExamQuery)
            preparedStatement2?.setInt(3, exam.course)
            preparedStatement2?.setInt(2, exam.examType.examId)
            preparedStatement2?.setString(1, exam.name)
            preparedStatement2?.setDate(5, java.sql.Date(exam.startTime.time))
            preparedStatement2?.setDate(6, java.sql.Date(exam.endTime.time))
            preparedStatement2?.setString(7, exam.instructions)
            preparedStatement2?.setInt(8, exam.version)
            preparedStatement2?.setString(9, exam.location)
            preparedStatement2?.executeUpdate()
        }



        val sqlString =
                "INSERT INTO EXAM (QUESTIONID, PARENTQUESTIONID, EXAMTYPEID, COURSEID, QUESTIONTEXT, QUESTIONTYPE, SEQUENCENUMBER, ANSWERTEXT, ANSWERKEYWORDS, ASSESSMENTCOMMENTS) " +
                        "VALUES (${testQuestion?.questionId}, ${testQuestion?.parentQuestionId}, ${testQuestion?.examTypeId?.value}, ${testQuestion?.courseId?.value}, ?, ?, ${testQuestion?.sequenceNumber}, ?, ${testQuestion?.answerKeywords}, ${testQuestion?.assessmentComments});"
        val preparedStatement3 = conn?.prepareStatement(sqlString)
        preparedStatement3?.setString(1, testQuestion?.questionText)
        preparedStatement3?.setString(2, testQuestion?.questionType.toString())
        preparedStatement3?.setString(3, testQuestion?.answerText)
        preparedStatement3?.executeUpdate()

    }

    @After
    @Rollback
    fun afterEach(){
        //TODO rollback
    }



    @Test
    fun testGetExams() {
        val expected = arrayOf(SimpleExam(1, "SWA Toets 1", "SWA"),
                SimpleExam(2, "SWA Toets 2", "SWA"),
                SimpleExam(3, "APP Toets algoritmen", "APP")
        )
        val builder = UriComponentsBuilder.fromHttpUrl("http://localhost:$port/exams")
        val entity = HttpEntity<Any>(HttpHeaders())

        val response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                Array<SimpleExam>::class.java)

        assertNotNull(response.body)
        assertEquals(response.statusCode, HttpStatus.OK)
        assertEquals(expected[0], response.body?.get(0))
        assertEquals(expected.size, response.body?.size)
        assertEquals(expected.last(), response.body?.last())
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
