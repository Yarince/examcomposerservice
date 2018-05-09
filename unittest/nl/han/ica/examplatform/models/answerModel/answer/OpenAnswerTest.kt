package nl.han.ica.examplatform.models.answerModel.answer

import org.junit.Test
import kotlin.test.assertEquals

class OpenAnswerTest {

    @Test
    fun testValidCorrectQuestionAnswerString() {
        val expected = OpenAnswer::class.java
        val result = OpenAnswer(3, "des", Keywords(arrayListOf())).javaClass
        assertEquals(expected, result)
    }
}