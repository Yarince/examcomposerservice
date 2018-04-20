package nl.han.ica.examplatform.controllers

import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.exam.SimpleExam
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*
import org.springframework.http.*
import org.springframework.http.HttpEntity
import org.springframework.web.util.UriComponentsBuilder


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExamControllerIntegrationTest(@Autowired private val restTemplate: TestRestTemplate) {

    @Test
    fun testGetExams() {
        val expected = arrayOf(SimpleExam(1, "SWA Toets 1", "SWA"),
                SimpleExam(2, "SWA Toets 2", "SWA"),
                SimpleExam(3, "APP Toets algoritmen", "APP")
        )
        val builder = UriComponentsBuilder.fromPath("/exam/all")
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
        val requestJson = """{"name":"name-0","durationInMinutes":10,"startTime":"1970-01-01T00:00:06.000+0000","course":"APP","examType":"EXAM","examId":null,"instructions":null,"location":null,"questions":null}"""

        headers.contentType = MediaType.APPLICATION_JSON
        val entity = HttpEntity(requestJson, headers)
        val result = restTemplate.postForEntity<Exam>("/exam", entity)

        assertNotNull(result)
        assertEquals(result.statusCode, HttpStatus.CREATED)
        assertEquals(Exam("name-0", 10, Date(6000), "APP", ExamType.EXAM)
                , result.body)
    }

    @Test
    fun testGetExamSuccess() {
        val requestParamExamId = 1

        val headers = HttpHeaders()
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE)

        val builder = UriComponentsBuilder.fromPath("/exam")
                .queryParam("id", requestParamExamId)

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

        val builder = UriComponentsBuilder.fromPath("/exam")
                .queryParam("id", requestParamExamId)

        val entity = HttpEntity<Any>(headers)

        val response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                Any::class.java)

        assertEquals(response.statusCode, HttpStatus.NOT_FOUND)
    }
}
