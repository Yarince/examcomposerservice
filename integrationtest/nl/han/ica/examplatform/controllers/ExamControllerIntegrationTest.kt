package nl.han.ica.examplatform.controllers

import junit.framework.TestCase.*
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.PropertySource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import org.springframework.web.client.postForEntity
import java.util.*


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
        val result = restTemplate.getForEntity<String>("http://localhost:$port/exam")
        assertNotNull(result)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertEquals("""[{"name":"name-0","durationInMinutes":10,"startTime":"1970-01-01T00:00:06.000+0000","course":"APP","examType":"EXAM","examId":null,"endTime":"1970-01-01T00:00:06.010+0000","instructions":null,"location":null,"questions":null},{"name":"name-1","durationInMinutes":10,"startTime":"1970-01-01T00:00:06.000+0000","course":"APP","examType":"EXAM","examId":null,"endTime":"1970-01-01T00:00:06.010+0000","instructions":null,"location":null,"questions":null}]""".trimMargin(),
                result.body)
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
        val result = restTemplate.postForEntity<Exam>("http://localhost:$port/exam", entity)

        assertEquals(HttpStatus.CREATED, result.statusCode)
        assertEquals(Exam(name = "name-0", durationInMinutes = 10, startTime = Date(6000), course = "APP", version = 1, examType = ExamType.EXAM)
                , result.body)
    }
}
