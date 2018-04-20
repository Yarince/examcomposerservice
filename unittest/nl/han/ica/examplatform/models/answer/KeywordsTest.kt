package nl.han.ica.examplatform.models.answer

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
internal class KeywordsTest {

    @Test
    fun size(){
        val expected = 3
        val result =  Keywords(arrayOf("01", "02", "03")).size
        assertEquals(expected, result)
    }

    @Test
    fun createValidKeywords(){
        Keywords(arrayOf("01", "02"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun createInvalidKeywords(){
        val expected = Keywords::class.java
        val result = Keywords(arrayOf("01", "0 2")).javaClass
        assertNotNull(result)
        assertEquals(expected, result)
    }

    @Test
    fun doesContain(){
        val keywords = Keywords(arrayOf("01", "02"))
        assertTrue(keywords.contains("02"))
    }

    @Test
    fun doesNotContain(){
        val keywords = Keywords(arrayOf("01", "02"))
        assertTrue(!keywords.contains("03"))
    }

    @Test
    fun doesContainAll(){
        val keywords = Keywords(arrayOf("01", "02", "03"))
        assertTrue(keywords.containsAll(
                Keywords(arrayOf("01", "02"))
        ))
    }

    @Test
    fun doesNotContainAll(){
        val keywords = Keywords(arrayOf("01", "02", "03"))
        assertTrue(!keywords.containsAll(
                Keywords(arrayOf("01", "04"))
        ))
    }

    @Test
    fun isEmpty(){
        val keywords = Keywords(arrayOf())
        assertTrue(keywords.isEmpty())
    }

    @Test
    fun isNotEmpty(){
        val keywords = Keywords(arrayOf("01"))
        assertTrue(!keywords.isEmpty())
    }

    @Test
    fun iterator(){
        val keywords = Keywords(arrayOf("01", "02"))
        val result = keywords.iterator()
        assertNotNull(result)
        assertTrue(result is Iterator<String>)
    }
}