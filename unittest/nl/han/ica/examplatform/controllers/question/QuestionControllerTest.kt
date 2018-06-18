package nl.han.ica.examplatform.controllers.question

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import nl.han.ica.examplatform.business.question.QuestionService
import nl.han.ica.examplatform.business.question.QuestionTypeService
import nl.han.ica.examplatform.models.question.Question
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


@RunWith(MockitoJUnitRunner::class)
class QuestionControllerTest {

    @InjectMocks
    private
    lateinit var questionController: QuestionController

    @Mock
    private
    lateinit var questionService: QuestionService

    @Mock
    private
    lateinit var questionTypeService: QuestionTypeService

    @Test
    fun testCreateQuestion() {
        val expectedResultBody = Question(
                questionId = 0,
                questionOrderInExam = 1,
                questionType = "OpenQuestion",
                questionText = "name",
                questionPoints = 5,
                courseId = 1,
                examType = "Tentamen",
                answerType = "OpenQuestion",
                answerTypePluginVersion = "1.0",
                pluginVersion = "1.0",
                partialAnswers = arrayListOf())
        val expectedStatusCode = HttpStatus.CREATED

        doReturn(ResponseEntity(expectedResultBody, expectedStatusCode)).`when`(questionService).addQuestion(expectedResultBody)

        val result = questionController.createQuestion(expectedResultBody)

        assertNotNull(result)
        assertEquals(expectedResultBody, result.body)
        assertEquals(expectedStatusCode, result.statusCode)
    }
}