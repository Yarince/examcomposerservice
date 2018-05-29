package nl.han.ica.examplatform.business.course

import nl.han.ica.examplatform.models.course.Course
import nl.han.ica.examplatform.persistence.course.CourseDAO
import org.junit.Test

import org.mockito.InjectMocks
import org.mockito.Mock

class CourseServiceTest {

    @InjectMocks
    private lateinit var courseService: CourseService

    @Mock
    private lateinit var courseDAO: CourseDAO

    @Test
    fun getAllCourses() {
        val expected = arrayListOf(Course(1, "app"), Course(2, "swa"))


    }
}