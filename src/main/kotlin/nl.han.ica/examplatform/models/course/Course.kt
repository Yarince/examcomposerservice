package nl.han.ica.examplatform.models.course

import io.swagger.annotations.ApiModelProperty

data class Course(
        @ApiModelProperty(notes = "The id of the course")
        val courseId: Int,
        @ApiModelProperty(notes = "The name of the course")
        val name: String,
        @ApiModelProperty(notes = "The code of the course")
        val courseCode: String
)
