package nl.han.ica.examplatform.models.answer

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class OpenAnswerTest {

    @Test
    fun testValid() {
        val expected = OpenAnswer::class.java
        val result = OpenAnswer(3, "des", "com", Keywords(arrayOf())).javaClass
        assertEquals(expected, result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testQuestionIdZero() {
        OpenAnswer(0, "des", "com", Keywords(arrayOf())).javaClass
    }

    @Test(expected = IllegalArgumentException::class)
    fun testQuestionIdNegative() {
        OpenAnswer(-10, "des", "com", Keywords(arrayOf())).javaClass
    }

    @Test(expected = IllegalArgumentException::class)
    fun testDescriptionEmpty() {
        OpenAnswer(1, "", "com", Keywords(arrayOf())).javaClass
    }

    @Test(expected = IllegalArgumentException::class)
    fun testCommentEmpty() {
        OpenAnswer(1, "des", "", Keywords(arrayOf())).javaClass
    }
}