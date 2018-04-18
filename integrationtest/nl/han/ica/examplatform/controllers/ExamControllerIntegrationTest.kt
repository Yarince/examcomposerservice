package nl.han.ica.examplatform.controllers

import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ExamControllerIntegrationTest(@Autowired private val restTemplate: TestRestTemplate) {


    @Test
    fun testGetExams() {
        val result = restTemplate.getForEntity<String>("/exam")
        assertNotNull(result)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertEquals("""[
            |{
                |"name":"name-0",
                |"durationInMinutes":10,
                |"startTime":"1970-01-01T00:00:06.000+0000",
                |"course":"APP",
                |"examType":"EXAM",
                |"examId":null,
                |"endTime":"1970-01-01T00:00:06.010+0000",
                |"instructions":null,
                |"location":null,
                |"questions":null
            |},{
                |"name":"name-0",
                |"durationInMinutes":10,
                |"startTime":"1970-01-01T00:00:06.000+0000",
                |"course":"APP",
                |"examType":"EXAM",
                |"examId":null,
                |"endTime":"1970-01-01T00:00:06.010+0000",
                |"instructions":null,
                |"location":null,
                |"questions":null
            |}
            |]""".trimMargin(),
                result.body)
    }

    @Test
    fun testHelloDto() {
        val result = restTemplate.postForEntity<Exam>("/exam", Exam("name-0", 10, Date(6000), "APP", ExamType.EXAM))
        assertNotNull(result)
        assertEquals(result.statusCode, HttpStatus.CREATED)
        assertEquals(Exam("name-0", 10, Date(6000), "APP", ExamType.EXAM)
                , result.body)
    }
}
