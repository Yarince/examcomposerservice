package nl.han.ica.examplatform.controllers

import nl.han.ica.examplatform.models.answer.OpenAnswer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.*
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnswerControllerIntegrationTest(@Autowired private val restTemplate: TestRestTemplate) {

    @Test
    fun testAddOpenAnswerToQuestion() {
        val expected: HttpStatus = HttpStatus.OK
        val requestJson = """{
            "questionId" : 5,
            "description" : "dis",
            "comment" : "com",
            "keywords" : ["key1", "key2"]
        }
        """.trimMargin()

        // https://stackoverflow.com/questions/33242126/spring-resttemplate-put-entity-to-server?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity = HttpEntity(requestJson, headers)
        val result = restTemplate.exchange("/answers", HttpMethod.PUT, entity, HttpStatus.OK::class.java)

        Assertions.assertNotNull(result)
        Assertions.assertEquals(expected, result.statusCode)
    }
}