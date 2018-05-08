package nl.han.ica.examplatform.controllers

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.PropertySource
import org.springframework.http.*
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.web.client.RestTemplate


@RunWith(SpringJUnit4ClassRunner::class)
@PropertySource("classpath:application.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class AnswerControllerIntegrationTest {

    @Value("\${local.server.port}")
    var port: Int = 0

    val restTemplate = RestTemplate()

    @Test
    fun testAddOpenAnswerToQuestion() {
        val expected: HttpStatus = HttpStatus.OK
        val requestJson = """{
            "questionId" : 5,
            "description" : "dis",
            "comment" : "com",
            "keywords" : ["key1", "key2"]
        }
        """

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity = HttpEntity(requestJson, headers)
        val result = restTemplate.exchange("http://localhost:$port//answers", HttpMethod.PUT, entity, HttpStatus.OK::class.java)

        assertNotNull(result)
        assertEquals(expected, result.statusCode)
    }
}