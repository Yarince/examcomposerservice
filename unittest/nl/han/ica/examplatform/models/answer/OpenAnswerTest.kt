package nl.han.ica.examplatform.models.answer

import org.junit.Test
import kotlin.test.assertEquals

class OpenAnswerTest {

    @Test
    fun testValidCommentString() {
        val expected = OpenAnswer::class.java
        val result = OpenAnswer(3, "des", "com", Keywords(arrayOf())).javaClass
        assertEquals(expected, result)
    }

    @Test
    fun testValidCommentNull() {
        val expected = OpenAnswer::class.java
        val result = OpenAnswer(3, "des", null, Keywords(arrayOf())).javaClass
        assertEquals(expected, result)
    }
}