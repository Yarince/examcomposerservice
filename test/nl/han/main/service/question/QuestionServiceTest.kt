package nl.han.main.service.question

import nl.han.main.model.question.ExamType
import nl.han.main.model.question.Question
import nl.han.main.model.question.QuestionType
import nl.han.main.persistence.question.QuestionDAO
import nl.han.main.service.question.QuestionService
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.math.exp
import kotlin.test.assertEquals



@RunWith(MockitoJUnitRunner::class)
class QuestionServiceTest {


    @InjectMocks
    lateinit var questionService: QuestionService

    @Mock
    private
    lateinit var questionDAO: QuestionDAO

    @Test
    fun testAddQuestionSuccess() {
        val questionInserted = Question(0, "name", QuestionType.OPEN_QUESTION, "APP", null, ExamType.EXAM, null)
        val expectedResult = ResponseEntity(questionInserted, HttpStatus.CREATED)

        doReturn(questionInserted).`when`(questionDAO).insertQuestion(questionInserted)

        val result = questionService.addQuestion(questionInserted)
        assertNotNull(result)
        assertEquals(expectedResult, result)
    }

}