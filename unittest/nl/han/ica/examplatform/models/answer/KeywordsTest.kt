package nl.han.ica.examplatform.models.answer

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class KeywordsTest {

    @Test
    fun testCreateValidKeywords() {
        Keywords(arrayOf("01", "02"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testCreateInvalidKeywords() {
        val expected = Keywords::class.java
        val result = Keywords(arrayOf("01", "0 2")).javaClass
        assertNotNull(result)
        assertEquals(expected, result)
    }
}