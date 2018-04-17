package nl.han.ica.examplatform.controllers

import nl.han.ica.examplatform.models.exam.Exam
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


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExamControllerIntegrationTest(@Autowired private val restTemplate: TestRestTemplate) {


    @Test
    fun testGetExams() {
        val result = restTemplate.getForEntity<String>("/exam")
        assertNotNull(result)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertEquals("[{\"examId\":0,\"name\":\"name-0\",\"durationInMinutes\":null,\"startTime\":null,\"endTime\":null,\"course\":null,\"examType\":null,\"instructions\":null,\"location\":null,\"questions\":null},{\"examId\":1,\"name\":\"name-1\",\"durationInMinutes\":null,\"startTime\":null,\"endTime\":null,\"course\":null,\"examType\":null,\"instructions\":null,\"location\":null,\"questions\":null}]", result.body)
    }

    @Test
    fun testHelloDto() {
        val result = restTemplate.postForEntity<Exam>("/exam", Exam(null, "name"))
        assertNotNull(result)
        assertEquals(result.statusCode, HttpStatus.CREATED)
        assertEquals(Exam(null, "name")
                , result.body)
    }
}
