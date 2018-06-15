package nl.han.ica.examplatform.business.classes

import junit.framework.TestCase
import nl.han.ica.examplatform.persistence.classes.ClassDAO
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@RunWith(MockitoJUnitRunner::class)
class ClassServiceTest {

    @InjectMocks
    private lateinit var classService: ClassService

    @Mock
    private lateinit var classDAO: ClassDAO

    @Test
    fun testGetAllClasses() {
        val expected = arrayListOf("APP1", "3EJAAR", "4EJAAR")

        Mockito.doReturn(expected).`when`(classDAO).getAllClasses()
        val result = classService.getAllClasses()
        TestCase.assertNotNull(result)
        TestCase.assertEquals(ResponseEntity(expected, HttpStatus.OK), result)
    }
}