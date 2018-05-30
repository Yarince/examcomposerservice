package nl.han.ica.examplatform.business.course

import nl.han.ica.examplatform.models.course.Course
import nl.han.ica.examplatform.persistence.course.CourseDAO
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
class CourseServiceTest {

    @InjectMocks
    private lateinit var courseService: CourseService

    @Mock
    private lateinit var courseDAO: CourseDAO

    @Test
    fun getAllCourses() {
        val expected = arrayListOf(Course(1, "A course with a name", "acwan"),
                Course(2, "Software Architecture", "swa"))

        doReturn(expected).`when`(courseDAO).getAllCourses()

        val result = courseService.getAllCourses()
        assertEquals(ResponseEntity(expected, HttpStatus.OK), result)
    }
}