package nl.han.ica.examplatform.controllers.exam

import nl.han.ica.examplatform.business.exam.PracticeExamService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class PracticeExamControllerTest {

    @InjectMocks
    private
    lateinit var practiceExamController: PracticeExamController

    @Mock
    private
    lateinit var practiceExamService: PracticeExamService

    @Test
    fun generatePracticeExam() {
        val expected = null
        doReturn(ResponseEntity<Any>(expected, HttpStatus.CREATED)).`when`(practiceExamService).generatePracticeExam()
        val result = practiceExamController.generatePracticeExam()
        assertEquals(ResponseEntity(expected, HttpStatus.CREATED), result)
    }
}