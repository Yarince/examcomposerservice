package nl.han.ica.examplatform.controllers

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import nl.han.ica.examplatform.models.answermodel.answer.Answer
import nl.han.ica.examplatform.models.answermodel.answer.PartialAnswer
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.http.*
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.sql.Connection
import java.sql.PreparedStatement

@RunWith(SpringJUnit4ClassRunner::class)
@PropertySource("classpath:application.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnswerControllerIntegrationTest {

    @Autowired
    final lateinit var environment: Environment

    val restTemplate = RestTemplate()

    private var builder: UriComponentsBuilder? = null

    private val testPartialAnswer = PartialAnswer(
            id = -888,
            text = "Partial Answer",
            points = 3)

    private val testQuestion = Question(
            questionId = -999,
            questionType = "OpenQuestion",
            courseId = 1,
            examType = "Tentamen",
            answerType = "MultipleChoice",
            answerTypePluginVersion = "1.0",
            pluginVersion = "1.0",
            partial_answers = arrayListOf(testPartialAnswer))

    private val testAnswer = Answer(
            questionId = testQuestion.questionId!!,
            description = "Answer Description",
            partial_answers = arrayListOf(testPartialAnswer))

    private var databaseConnection: Connection? = null

    @Before
    @Transactional
    fun setUp() {
        val port: String = environment.getProperty("local.server.port")!!
        builder = UriComponentsBuilder.fromHttpUrl("http://localhost:$port/ecs/answers")

        databaseConnection = MySQLConnection.getConnection()

        val insertQuestionSQL = """
            INSERT INTO `QUESTION`(
                `QUESTIONID`,
                `EXAMTYPENAME`,
                `COURSEID`,
                `QUESTIONTYPE`,
                `QUESTIONTYPEPLUGINVERSION`,
                `ANSWERTYPE`,
                `ANSWERTYPEPLUGINVERSION`
            ) VALUE (?, ?, ?, ?, ?, ?, ?)"""

        val insertAnswerSQL = """
            INSERT INTO `PARTIAL_ANSWER` (
              `PARTIALANSWERID`,
              `QUESTIONID`,
              `PARTIALANSWERTEXT`
            ) VALUES (?, ?, ?)"""

        val preparedStatementQuestion: PreparedStatement? = databaseConnection?.prepareStatement(insertQuestionSQL)
        preparedStatementQuestion?.setInt(1, testQuestion.questionId!!)
        preparedStatementQuestion?.setString(2, testQuestion.examType)
        preparedStatementQuestion?.setInt(3, testQuestion.courseId)
        preparedStatementQuestion?.setString(4, testQuestion.questionType)
        preparedStatementQuestion?.setString(5, testQuestion.pluginVersion)
        preparedStatementQuestion?.setString(6, testQuestion.answerType)
        preparedStatementQuestion?.setString(7, testQuestion.answerTypePluginVersion)

        preparedStatementQuestion?.executeUpdate()

        val preparedStatementAnswer: PreparedStatement? = databaseConnection?.prepareStatement(insertAnswerSQL)
        preparedStatementAnswer?.setInt(1, testPartialAnswer.id!!)
        preparedStatementAnswer?.setInt(2, testQuestion.questionId!!)
        preparedStatementAnswer?.setString(3, testPartialAnswer.text)

        preparedStatementAnswer?.executeUpdate()
    }

    @After
    fun tearDown() {
        databaseConnection = MySQLConnection.getConnection()

        val deleteAnswerSQL = "DELETE FROM PARTIAL_ANSWER WHERE QUESTIONID = ?"
        val deleteQuestionSQL = "DELETE FROM QUESTION WHERE QUESTIONID = ?"

        val preparedStatementAnswer: PreparedStatement? = databaseConnection?.prepareStatement(deleteAnswerSQL)
        preparedStatementAnswer?.setInt(1, testQuestion.questionId!!)

        preparedStatementAnswer?.executeUpdate()

        val preparedStatementQuestion: PreparedStatement? = databaseConnection?.prepareStatement(deleteQuestionSQL)
        preparedStatementQuestion?.setInt(1, testQuestion.questionId!!)

        preparedStatementQuestion?.executeUpdate()
    }

    @Test
    fun testAddAnswerToQuestion() {
        val expected: HttpStatus = HttpStatus.OK
        val requestJson = """{
            "questionId": ${testQuestion.questionId},
            "description": "Open Answer",
            "partial_answers": [{
                "id": ${testPartialAnswer.id!! - 1},
                "text": "Partial Answer 2",
                "points": 5
            }]
        }"""

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity: HttpEntity<String> = HttpEntity(requestJson, headers)
        val result: ResponseEntity<out HttpStatus> = restTemplate.exchange(
                builder?.toUriString(), HttpMethod.PUT, entity, HttpStatus.OK::class.java)

        assertNotNull(result)
        assertEquals(expected, result.statusCode)
    }

    @Test
    fun testUpdateAnswerFromQuestion() {
        val expected: HttpStatus = HttpStatus.OK
        val requestJson = """{
            "questionId": ${testQuestion.questionId},
            "description": "Open Answer",
            "partial_answers": [{
                "id": ${testPartialAnswer.id},
                "text": "Partial Answer Updated",
                "points": 5
            }]
        }"""

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity: HttpEntity<String> = HttpEntity(requestJson, headers)
        val result: ResponseEntity<out HttpStatus> = restTemplate.exchange(
                builder?.toUriString(), HttpMethod.PUT, entity, HttpStatus.OK::class.java)

        assertNotNull(result)
        assertEquals(expected, result.statusCode)
    }

    @Test(expected = HttpServerErrorException::class)
    fun testAddAnswerToQuestionNoPartialAnswers() {
        val requestJson = """{
            "questionId": ${testQuestion.questionId},
            "description": "Open Answer",
            "partial_answers": []
        }"""

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity: HttpEntity<String> = HttpEntity(requestJson, headers)
        restTemplate.exchange(builder?.toUriString(), HttpMethod.PUT, entity, HttpStatus.OK::class.java)
    }
}