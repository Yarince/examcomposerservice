package nl.han.ica.examplatform.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.question.Question
import org.json.JSONObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.boot.test.web.client.postForObject
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*
import org.springframework.boot.convert.ApplicationConversionService.configure
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class QuestionControllerIntegrationTest(@Autowired private val restTemplate: TestRestTemplate) {

    @Test
    fun testCreateQuestion() {
        val requestBodyToBeSent = JSONObject()
        requestBodyToBeSent.put("course", "APP")
        requestBodyToBeSent.put("examType", "EXAM")
        requestBodyToBeSent.put("questionType", "APP")

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val entity = HttpEntity(requestBodyToBeSent, headers)
        val jsonHttpMessageConverter = MappingJackson2HttpMessageConverter()
        jsonHttpMessageConverter.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        restTemplate.restTemplate.messageConverters.add(jsonHttpMessageConverter)

        val result = restTemplate.postForEntity<Question>("/question", requestBodyToBeSent)
        Assertions.assertNotNull(result)
        Assertions.assertEquals(result.statusCode, HttpStatus.CREATED)
        Assertions.assertEquals("[{\"name\":\"name-0\",\"durationInMinutes\":10,\"startTime\":\"1970-01-01T00:00:06.000+0000\",\"course\":\"APP\",\"examType\":\"EXAM\",\"examId\":null,\"endTime\":\"1970-01-01T00:00:06.010+0000\",\"instructions\":null,\"location\":null,\"questions\":null},{\"name\":\"name-1\",\"durationInMinutes\":10,\"startTime\":\"1970-01-01T00:00:06.000+0000\",\"course\":\"APP\",\"examType\":\"EXAM\",\"examId\":null,\"endTime\":\"1970-01-01T00:00:06.010+0000\",\"instructions\":null,\"location\":null,\"questions\":null}]".trimMargin(),
                result.body)
    }
}