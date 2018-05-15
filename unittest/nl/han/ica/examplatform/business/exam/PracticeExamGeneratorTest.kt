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


@RunWith(MockitoJUnitRunner::class)
internal class PracticeExamGeneratorTest {

    @Mock
    private lateinit var questionDAO: QuestionDAO

    @Test
    fun testGenerateExamSuccess() {
        val courseId = 1
        val categories = arrayOf("QA", "DCAR", "ATAM")
        val expectedQuestions = arrayOf(
                Question(courseId = courseId, categories = arrayOf("QA", "ATAM")),
                Question(courseId = courseId, categories = arrayOf("QA")),
                Question(courseId = courseId, categories = arrayOf("QA", "ASR")),
                Question(courseId = courseId, categories = arrayOf("ASR")),
                Question(courseId = courseId, categories = arrayOf("ASR", "DCAR", "QA")),
                Question(courseId = courseId, categories = arrayOf("DCAR")),
                Question(courseId = courseId, categories = arrayOf("DCAR")),
                Question(courseId = courseId, categories = arrayOf("DCAR")),
                Question(courseId = courseId, categories = arrayOf("DCAR"))
        )

        doReturn(expectedQuestions).`when`(questionDAO).getQuestions(courseId, categories)

        val result = generateExam(courseId, categories, questionDAO)

        assertNotNull(result)
        assertEquals(courseId, result.courseId)
        assertEquals(expectedQuestions.size / 2, result.questions.size)
    }
}