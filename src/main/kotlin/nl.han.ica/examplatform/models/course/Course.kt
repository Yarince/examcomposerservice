package nl.han.ica.examplatform.models.course

import io.swagger.annotations.ApiModelProperty

data class Course(

        @ApiModelProperty(notes = "The id of the course")
        val courseId: Int,

        @ApiModelProperty(notes = "The code of the course")
        val courseCode: String,

        @ApiModelProperty(notes = "The id of the semester this course is available")
        val semesterId: Int,

        @ApiModelProperty(notes = "The id of the coordinator of the course")
        val courseCoordinatorId: Int,

        @ApiModelProperty(notes = "The name of the course")
        val courseName: String
)