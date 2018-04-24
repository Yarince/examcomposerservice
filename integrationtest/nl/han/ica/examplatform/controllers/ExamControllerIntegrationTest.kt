package nl.han.ica.examplatform.controllers

import junit.framework.TestCase.*
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.exam.SimpleExam
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.models.question.QuestionType
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.PropertySource
import org.springframework.http.*
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.client.postForEntity
import java.util.*
import org.springframework.http.HttpEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.util.UriComponentsBuilder

@RunWith(SpringJUnit4ClassRunner::class)
@PropertySource("classpath:application.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class ExamControllerIntegrationTest {

    @Value("\${local.server.port}")
    var port: Int = 0

    val restTemplate = RestTemplate()

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
                questions = arrayOf(
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

    @Test
    fun testGetExamNotFound() {
        // For now this is -9999, could be changed later when we know more about how the ID will be constructed
        val requestParamExamId = -9999

        val headers = HttpHeaders()
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE)

        val builder = UriComponentsBuilder.fromHttpUrl("http://localhost:$port/exams/$requestParamExamId")
        val entity = HttpEntity<Any>(headers)

        try {
            restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    Any::class.java)
            fail()
        } catch (e: HttpClientErrorException) {
            // Success
            assertEquals(404, e.statusCode.value())
        }
    }
}
