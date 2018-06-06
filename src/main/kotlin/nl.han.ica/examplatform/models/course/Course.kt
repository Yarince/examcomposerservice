package nl.han.ica.examplatform.models.course

import io.swagger.annotations.ApiModelProperty

/**
 * Represents a course that is that can used to group and filter the [Exam]s.
 *
 * @param courseId [Int] The id of the course
 * @param name [String] The name of the course
 */
data class Course(
        @ApiModelProperty(notes = "The id of the course")
        val courseId: Int,
        @ApiModelProperty(notes = "The name of the course")
        val name: String,
        @ApiModelProperty(notes = "The abbreviation of the course name")
        val courseCode: String
)
