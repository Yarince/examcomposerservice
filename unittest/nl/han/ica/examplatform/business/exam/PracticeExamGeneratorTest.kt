package nl.han.ica.examplatform.business.exam

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.persistence.category.ICategoryDAO
import nl.han.ica.examplatform.persistence.question.IQuestionDAO
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertTrue


@RunWith(MockitoJUnitRunner::class)
internal class PracticeExamGeneratorTest {

    @Mock
    private lateinit var questionDAO: IQuestionDAO

    @Mock
    private lateinit var categoryDAO: ICategoryDAO

    @Test
    fun testGenerateExamSuccess() {
        val courseId = 1
        val studentNr = 1
        val categories = arrayListOf("ATAM", "DCAR")
        val expectedQuestions = arrayOf(
                Question(questionId = 1, questionType = "OpenQuestion", courseId = 1, examType = "Proeftoets", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", pluginVersion = "1.0", categories = arrayListOf("QA", "ATAM")),
                Question(questionId = 2, questionType = "OpenQuestion", courseId = 1, examType = "Proeftoets", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", pluginVersion = "1.0", categories = arrayListOf("DCAR")),
                Question(questionId = 3, questionType = "OpenQuestion", courseId = 1, examType = "Proeftoets", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", pluginVersion = "1.0", categories = arrayListOf("ATAM")),
                Question(questionId = 4, questionType = "OpenQuestion", courseId = 1, examType = "Proeftoets", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", pluginVersion = "1.0", categories = arrayListOf("ATAM")),
                Question(questionId = 5, questionType = "OpenQuestion", courseId = 1, examType = "Proeftoets", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", pluginVersion = "1.0", categories = arrayListOf("ASR", "DCAR", "QA")),
                Question(questionId = 6, questionType = "OpenQuestion", courseId = 1, examType = "Proeftoets", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", pluginVersion = "1.0", categories = arrayListOf("DCAR")),
                Question(questionId = 7, questionType = "OpenQuestion", courseId = 1, examType = "Proeftoets", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", pluginVersion = "1.0", categories = arrayListOf("DCAR")),
                Question(questionId = 8, questionType = "OpenQuestion", courseId = 1, examType = "Proeftoets", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", pluginVersion = "1.0", categories = arrayListOf("DCAR")),
                Question(questionId = 9, questionType = "OpenQuestion", courseId = 1, examType = "Proeftoets", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", pluginVersion = "1.0", categories = arrayListOf("DCAR")),
                Question(questionId = 10, questionType = "OpenQuestion", courseId = 1, examType = "Proeftoets", answerType = "OpenQuestion", answerTypePluginVersion = "1.0", pluginVersion = "1.0", categories = arrayListOf("DCAR"))
        )

        doReturn(expectedQuestions).`when`(questionDAO).getQuestionsByCourse(courseId)
        doReturn(categories).`when`(categoryDAO).getCategoriesByCourse(courseId)

        val result = generatePracticeExam(courseId, studentNr, questionDAO, categoryDAO)

        assertNotNull(result)
        assertEquals(courseId, result.courseId)
        assertEquals(expectedQuestions.size / 2, result.questions.size)

        // Check if all categories are in the practice exam
        val filteredQuestions: MutableMap<String, List<Question>> = mutableMapOf()
        for (category in categories) {
            filteredQuestions[category] = result.questions.filter { it.categories.contains(category) }
        }
        val allCategoriesInResult = filteredQuestions.keys.toTypedArray()
        assertTrue(categories.toArray()!!.contentEquals(allCategoriesInResult))

        // Check if there are duplicate questions
        assertEquals(result.questions.size, result.questions.distinctBy { Pair(it.questionId, it.categories) }.size)
    }
}
