package nl.han.ica.examplatform.models.exam

import io.swagger.annotations.ApiModelProperty
import nl.han.ica.examplatform.models.question.Question
import java.util.Date

data class PreparedExam(
        @ApiModelProperty(notes = "The ID of the exam")
        val examId: Int,

        @ApiModelProperty(notes = "The name of the exam")
        val name: String,

        @ApiModelProperty(notes = "The duration in minutes of the exam")
        val durationInMinutes: Int,

        @ApiModelProperty(notes = "The start time of the exam")
        val startTime: Date,

        @ApiModelProperty(notes = "The course of the exam")
        val course: String,

        @ApiModelProperty(notes = "The type of the exam can be practice test or exam")
        val examType: ExamType,

        @ApiModelProperty(notes = "The end time of the exam")
        val endTime: Date = Date(startTime.time + durationInMinutes),

        @ApiModelProperty(notes = "The instructions for the exam")
        val instructions: String,

        @ApiModelProperty(notes = "The location of the exam")
        val location: String,

        @ApiModelProperty(notes = "The questions in the exam")
        val questions: Array<Question>
)