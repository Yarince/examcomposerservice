package nl.han.ica.examplatform.business.exam

import com.nhaarman.mockito_kotlin.doReturn
import nl.han.ica.examplatform.controllers.responseexceptions.InvalidExamException
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.exam.OfficialExam
import nl.han.ica.examplatform.persistence.exam.ExamDAO
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class PracticeExamServiceTest {

    @InjectMocks
    private lateinit var practiceExamService: PracticeExamService

    @Mock
    private lateinit var examDAO: ExamDAO

    @Test(expected = InvalidExamException::class)
    fun testCheckExamEmptyId() {
        val exam = OfficialExam(5, "name-0", 10, Date(6000), courseId = 1,
                version = 1,
                examType = ExamType.EXAM) // Faulty exam object
        practiceExamService.checkExam(exam)
    }

    @Test
    fun generatePracticeExam() {
        val expected = null
        doReturn(expected).`when`(examDAO).generatePracticeExam()
        val result = practiceExamService.generatePracticeExam()
        assertEquals(ResponseEntity(expected, HttpStatus.CREATED), result)
    }
}