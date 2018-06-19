package nl.han.ica.examplatform.controllers.decryptioncode

import com.nhaarman.mockito_kotlin.doReturn
import nl.han.ica.examplatform.business.decryptioncode.DecryptionCodeService
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@RunWith(MockitoJUnitRunner::class)
class DecryptionCodeControllerTest {

    @InjectMocks
    private lateinit var decryptionCodeController: DecryptionCodeController

    @Mock
    private lateinit var decryptionCodeService: DecryptionCodeService

    @Test
    fun testGetDecryptionCode() {
        val examId = 1
        val expected = ResponseEntity("Code", HttpStatus.OK)
        doReturn(expected).`when`(decryptionCodeService).getDecryptionCode(examId)
        val actual = decryptionCodeController.getDecryptionCode(examId)
        assertNotNull(actual)
        assertEquals(expected, actual)
    }
}