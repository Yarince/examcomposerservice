package nl.han.ica.examplatform.business.decryptioncode

import com.nhaarman.mockito_kotlin.doReturn
import nl.han.ica.examplatform.persistence.decryptioncode.DecryptionCodeDAO
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@RunWith(MockitoJUnitRunner::class)
class DecryptionCodeServiceTest {

    @Mock
    private lateinit var decryptionCodeDAO: DecryptionCodeDAO

    @InjectMocks
    private lateinit var decryptionCodeService: DecryptionCodeService

    @Test
    fun testGetDecryptionCode() {
        val examId = 1
        val expected = "Code"
        doReturn(expected).`when`(decryptionCodeDAO).getDecryptionCode(examId)
        assertEquals(ResponseEntity(expected, HttpStatus.OK), decryptionCodeService.getDecryptionCode(examId))
    }
}