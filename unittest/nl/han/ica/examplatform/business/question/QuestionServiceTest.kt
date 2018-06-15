package nl.han.ica.examplatform.business.question

import nl.han.ica.examplatform.controllers.DatabaseException
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.category.CategoryDAO
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals


@RunWith(MockitoJUnitRunner::class)
class QuestionServiceTest {

    @InjectMocks
    lateinit var questionService: QuestionService

    @Mock
    private lateinit var questionDAO: QuestionDAO

    @Mock
    private lateinit var categoryDAO: CategoryDAO

    @Test
    fun testAddQuestionSuccess() {
        val categories = arrayListOf("ASD", "QA")
        val questionInserted = Question(questionId = 0, questionOrderInExam = 1, questionType = "OpenQuestion", questionText = "name", questionPoints = 5, courseId = 1, examType = "Tentamen", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", pluginVersion = "1.0", categories = categories)
        val expectedResult = ResponseEntity(questionInserted, HttpStatus.CREATED)

        doReturn(questionInserted).`when`(questionDAO).insertQuestion(questionInserted)
        doReturn(true).`when`(categoryDAO).checkIfCategoriesExist(categories)

        val result = questionService.addQuestion(questionInserted)
        assertNotNull(result)
        assertEquals(expectedResult, result)
    }

    @Test(expected = QuestionNotInsertedException::class)
    fun testAddQuestionError() {
        val categories = arrayListOf("ASD", "QA")
        val questionInserted = Question(questionId = 0, questionOrderInExam = 1, questionType = "OpenQuestion", questionText = "name", questionPoints = 5, courseId = 1, examType = "Tentamen", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", pluginVersion = "1.0", categories = categories)
        val expectedResult = ResponseEntity<Question>(HttpStatus.INTERNAL_SERVER_ERROR)

        doThrow(DatabaseException("DAO Error"))
                .`when`(questionDAO)
                .insertQuestion(questionInserted)
        doReturn(true).`when`(categoryDAO).checkIfCategoriesExist(categories)

        questionService.addQuestion(questionInserted)
    }
}
