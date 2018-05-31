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
                Question(questionId = 1, questionType = "OpenQuestion", examType = "Proeftoets", categories = arrayListOf("QA", "ATAM"), pluginVersion = "1.0"),
                Question(questionId = 2, questionType = "OpenQuestion", examType = "Proeftoets", categories = arrayListOf("DCAR"), pluginVersion = "1.0"),
                Question(questionId = 3, questionType = "OpenQuestion", examType = "Proeftoets", categories = arrayListOf("ATAM"), pluginVersion = "1.0"),
                Question(questionId = 4, questionType = "OpenQuestion", examType = "Proeftoets", categories = arrayListOf("ATAM"), pluginVersion = "1.0"),
                Question(questionId = 5, questionType = "OpenQuestion", examType = "Proeftoets", categories = arrayListOf("ASR","DCAR", "QA") , pluginVersion = "1.0"),
                Question(questionId = 6, questionType = "OpenQuestion", examType = "Proeftoets", categories = arrayListOf("DCAR"), pluginVersion = "1.0"),
                Question(questionId = 7, questionType = "OpenQuestion", examType = "Proeftoets", categories = arrayListOf("DCAR"), pluginVersion = "1.0"),
                Question(questionId = 8, questionType = "OpenQuestion", examType = "Proeftoets", categories = arrayListOf("DCAR"), pluginVersion = "1.0"),
                Question(questionId = 9, questionType = "OpenQuestion", examType = "Proeftoets", categories = arrayListOf("DCAR"), pluginVersion = "1.0"),
                Question(questionId = 10, questionType = "OpenQuestion", examType = "Proeftoets", categories = arrayListOf("DCAR") , pluginVersion = "1.0")
        )

        doReturn(expectedQuestions).`when`(questionDAO).getQuestionsByCourseAndCategory(courseId, categories)

        val result = generatePracticeExam(courseId, categories, questionDAO)

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
        assertEquals(result.questions.size, result.questions.distinctBy { Pair(it.questionId, it.categories) }.size)
    }
}
