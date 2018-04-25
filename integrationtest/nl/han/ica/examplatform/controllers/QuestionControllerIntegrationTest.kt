package nl.han.ica.examplatform.controllers

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import nl.han.ica.examplatform.models.course.CourseType
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.models.question.QuestionType
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
import org.springframework.web.client.postForEntity


@RunWith(SpringJUnit4ClassRunner::class)
@PropertySource("classpath:application.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class QuestionControllerIntegrationTest {

    @Value("\${local.server.port}")
    var port: Int = 0

    val restTemplate = RestTemplate()

    @Test
    fun testCreateQuestion() {
        val expectedResult = Question(parentQuestionId = null, examTypeId = ExamType.EXAM, courseId = CourseType.APP, questionType = QuestionType.OPEN_QUESTION, sequenceNumber = null, answerText = null, answerKeywords = null, assessmentComments =null)
        val requestJson = """{"course": "${expectedResult.courseId}", "examType": "${expectedResult.examTypeId}", "questionType": "${expectedResult.questionType}"}"""

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity = HttpEntity(requestJson, headers)
        val result = restTemplate.postForEntity<Question>("http://localhost:$port/question", entity)

        assertNotNull(result)
        assertEquals(HttpStatus.CREATED, result.statusCode)
        assertEquals(expectedResult, result.body)
    }
}
