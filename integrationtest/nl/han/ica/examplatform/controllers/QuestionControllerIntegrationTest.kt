package nl.han.ica.examplatform.controllers

import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.models.question.QuestionType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuestionControllerIntegrationTest(@Autowired private val restTemplate: TestRestTemplate) {

    @Test
    fun testCreateQuestion() {
        val expectedResult = Question(course = "APP", examType = ExamType.EXAM, questionType = QuestionType.OPEN_QUESTION)
        val requestJson = """{
            "course": "${expectedResult.course}",
            "examType": "${expectedResult.examType}",
            "questionType": "${expectedResult.questionType}"
            }"""

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity = HttpEntity(requestJson, headers)
        val result = restTemplate.postForEntity<Question>("/question", entity)

        Assertions.assertNotNull(result)
        Assertions.assertEquals(HttpStatus.CREATED, result.statusCode)
        Assertions.assertEquals(expectedResult, result.body)
    }
}
