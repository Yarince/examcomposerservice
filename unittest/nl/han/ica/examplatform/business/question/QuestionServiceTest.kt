package nl.han.ica.examplatform.business.question

import com.nhaarman.mockito_kotlin.anyOrNull
import nl.han.ica.examplatform.controllers.DatabaseException
import nl.han.ica.examplatform.controllers.question.CategoriesDontExistException
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.category.CategoryDAO
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
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
        val questionInserted = Question(
                questionId = 0,
                questionOrderInExam = 1,
                questionType = "OpenQuestion",
                questionText = "name",
                courseId = 1,
                examType = "Tentamen",
                answerType = "OpenQuestion",
                answerTypePluginVersion = "1.0",
                questionTypePluginVersion = "1.0",
                categories = categories,
                partialAnswers = arrayListOf())
        val expectedResult: ResponseEntity<Question> = ResponseEntity(questionInserted, HttpStatus.CREATED)

        doReturn(questionInserted).`when`(questionDAO).insertQuestion(questionInserted)
        doReturn(true).`when`(categoryDAO).checkIfCategoriesExist(categories)

        val result: ResponseEntity<Question> = questionService.addQuestion(questionInserted)
        assertNotNull(result)
        assertEquals(expectedResult, result)
    }

    @Test
    fun testAddQuestionSuccessWithSubQuestions() {
        val categories: ArrayList<String> = arrayListOf("ASD", "QA")
        val secondLayerSubQuestion = Question(
                questionId = 2,
                questionOrderInExam = 1,
                questionType = "OpenQuestion",
                questionText = "name",
                courseId = 1,
                examType = "exam",
                answerType = "OpenQuestion",
                answerTypePluginVersion = "1.0",
                questionTypePluginVersion = "1.0",
                categories = categories,
                partialAnswers = arrayListOf(),
                subQuestions = arrayListOf())
        val subQuestion = Question(
                questionId = 1,
                questionOrderInExam = 1,
                questionType = "OpenQuestion",
                questionText = "name",
                courseId = 1,
                examType = "exam",
                answerType = "OpenQuestion",
                answerTypePluginVersion = "1.0",
                questionTypePluginVersion = "1.0",
                categories = categories,
                partialAnswers = arrayListOf(),
                subQuestions = arrayListOf(secondLayerSubQuestion))
        val insertedQuestion = Question(
                questionId = 3,
                questionOrderInExam = 1,
                questionType = "OpenQuestion",
                questionText = "name",
                courseId = 1,
                examType = "exam",
                answerType = "OpenQuestion",
                answerTypePluginVersion = "1.0",
                questionTypePluginVersion = "1.0",
                categories = categories,
                partialAnswers = arrayListOf(),
                subQuestions = arrayListOf(subQuestion))
        val question = Question(
                questionId = null,
                questionOrderInExam = 1,
                questionType = "OpenQuestion",
                questionText = "name",
                courseId = 1,
                examType = "exam",
                answerType = "OpenQuestion",
                answerTypePluginVersion = "1.0",
                questionTypePluginVersion = "1.0",
                categories = categories,
                partialAnswers = arrayListOf(),
                subQuestions = arrayListOf(subQuestion))

        val expectedResult: ResponseEntity<Question> = ResponseEntity(insertedQuestion, HttpStatus.CREATED)

        `when`(questionDAO.insertQuestion(com.nhaarman.mockito_kotlin.any(), anyOrNull()))
                .thenReturn(insertedQuestion)
                .thenReturn(subQuestion)
                .thenReturn(secondLayerSubQuestion)
        doReturn(true).`when`(categoryDAO).checkIfCategoriesExist(categories)

        val result: ResponseEntity<Question> = questionService.addQuestion(question)

        assertNotNull(result)
        assertEquals(expectedResult, result)
    }

    @Test(expected = CategoriesDontExistException::class)
    fun testAddQuestionErrorCheckIfCategoriesExist() {
        val categories: ArrayList<String> = arrayListOf("ASD", "QA")
        val questionInserted = Question(
                questionId = 0,
                questionOrderInExam = 1,
                questionType = "OpenQuestion",
                questionText = "name",
                courseId = 1,
                examType = "exam",
                answerType = "OpenQuestion",
                answerTypePluginVersion = "1.0",
                questionTypePluginVersion = "1.0",
                categories = categories,
                partialAnswers = arrayListOf())

        doReturn(false).`when`(categoryDAO).checkIfCategoriesExist(categories)

        questionService.addQuestion(questionInserted)
    }

    @Test(expected = QuestionNotInsertedException::class)
    fun testAddQuestionErrorDatabaseException() {
        val categories: ArrayList<String> = arrayListOf("ASD", "QA")
        val questionInserted = Question(
                questionId = 0,
                questionOrderInExam = 1,
                questionType = "OpenQuestion",
                questionText = "name",
                courseId = 1,
                examType = "exam",
                answerType = "OpenQuestion",
                answerTypePluginVersion = "1.0",
                questionTypePluginVersion = "1.0",
                categories = categories,
                partialAnswers = arrayListOf())

        doThrow(DatabaseException("Message")).`when`(categoryDAO).checkIfCategoriesExist(categories)

        questionService.addQuestion(questionInserted)
    }

    @Test
    fun testGetQuestionsForCourse() {
        val courseId = 1
        val questions = arrayOf(Question(
                questionId = 0,
                questionOrderInExam = 1,
                questionType = "OpenQuestion",
                questionText = "name",
                courseId = 1,
                examType = "exam",
                answerType = "OpenQuestion",
                answerTypePluginVersion = "1.0",
                questionTypePluginVersion = "1.0",
                categories = arrayListOf("ASD", "QA"),
                partialAnswers = arrayListOf()))
        val expected: ResponseEntity<Array<Question>> = ResponseEntity(questions, HttpStatus.OK)
        doReturn(questions).`when`(questionDAO).getQuestionsByCourse(courseId)
        val result: ResponseEntity<Array<Question>> = questionService.getQuestionsForCourse(courseId)

        assertEquals(expected, result)
    }

    @Test
    fun testGetQuestionForId() {
        val questionId = 1
        val question = Question(
                questionId = 0,
                questionOrderInExam = 1,
                questionType = "OpenQuestion",
                questionText = "name",
                courseId = 1,
                examType = "exam",
                answerType = "OpenQuestion",
                answerTypePluginVersion = "1.0",
                questionTypePluginVersion = "1.0",
                categories = arrayListOf("ASD", "QA"),
                partialAnswers = arrayListOf())
        val expected: ResponseEntity<Question> = ResponseEntity(question, HttpStatus.OK)
        doReturn(question).`when`(questionDAO).getQuestionById(questionId)
        val result: ResponseEntity<Question> = questionService.getQuestionForId(questionId)

        assertEquals(expected, result)
    }

    @Test
    fun testUpdateQuestion() {
        val question = Question(
                questionId = 0,
                questionOrderInExam = 1,
                questionType = "OpenQuestion",
                questionText = "name",
                courseId = 1,
                examType = "exam",
                answerType = "OpenQuestion",
                answerTypePluginVersion = "1.0",
                questionTypePluginVersion = "1.0",
                categories = arrayListOf("ASD", "QA"),
                partialAnswers = arrayListOf())
        val expected: ResponseEntity<Question> = ResponseEntity(question, HttpStatus.ACCEPTED)
        doReturn(question).`when`(questionDAO).updateQuestion(question)
        val result: ResponseEntity<Question> = questionService.updateQuestion(question)

        assertEquals(expected, result)
    }
}
