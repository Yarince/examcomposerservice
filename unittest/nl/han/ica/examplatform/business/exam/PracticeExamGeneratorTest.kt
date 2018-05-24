package nl.han.ica.examplatform.business.exam

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.question.QuestionDAO
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertTrue


@RunWith(MockitoJUnitRunner::class)
internal class PracticeExamGeneratorTest {

    @Mock
    private lateinit var questionDAO: QuestionDAO

    @Test
    fun testGenerateExamSuccess() {
        val courseId = 1
        val categories = arrayOf("ATAM", "DCAR")
        val expectedQuestions = arrayOf(
                Question(courseId = courseId, questionId = 1, categories = arrayOf("QA", "ATAM")),
                Question(courseId = courseId, questionId = 5, categories = arrayOf("ASR", "DCAR", "QA")),
                Question(courseId = courseId, questionId = 6, categories = arrayOf("DCAR")),
                Question(courseId = courseId, questionId = 7, categories = arrayOf("DCAR")),
                Question(courseId = courseId, questionId = 8, categories = arrayOf("DCAR")),
                Question(courseId = courseId, questionId = 9, categories = arrayOf("DCAR"))
        )

        doReturn(expectedQuestions).`when`(questionDAO).getQuestions(courseId, categories)

        val result = generateExam(courseId, categories, questionDAO)

        assertNotNull(result)
        assertEquals(courseId, result.courseId)
        assertEquals(expectedQuestions.size / 2, result.questions.size)

        // Check if all categories are in the practice exam
        val filteredQuestions: MutableMap<String, List<Question>> = mutableMapOf()
        for (category in categories) {
            filteredQuestions[category] = result.questions.filter { it.categories.contains(category) }
        }
        val allCategoriesInResult = filteredQuestions.keys.toTypedArray()
        assertTrue(categories.contentEquals(allCategoriesInResult))

        // Check if there are duplicate questions
        result.questions.forEach { println(it) }
        assertEquals(result.questions.size, result.questions.distinctBy { Pair(it.questionText, it.categories) }.size)
    }
}