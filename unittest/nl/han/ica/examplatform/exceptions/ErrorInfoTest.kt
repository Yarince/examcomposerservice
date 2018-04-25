package nl.han.ica.examplatform.exceptions

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class ErrorInfoTest {

    private val errorInfo: ErrorInfo = ErrorInfo(
            "dev",
            "usr",
            "code",
            "url"
    )

    @Test
    fun testGetDeveloperMessage() {
        assertEquals("dev", errorInfo.developerMessage)
    }

    @Test
    fun testGetUserMessage() {
        assertEquals("usr", errorInfo.userMessage)
    }

    @Test
    fun testGetErrorCode() {
        assertEquals("code", errorInfo.errorCode)
    }

    @Test
    fun testGetMoreInfo() {
        assertEquals("url", errorInfo.moreInfo)
    }

    @Test
    fun testToString() {
        val string = errorInfo.toString()
        assertTrue(string.contains("dev"))
        assertTrue(string.contains("usr"))
        assertTrue(string.contains("code"))
        assertTrue(string.contains("url"))
    }
}