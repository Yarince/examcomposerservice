package nl.han.ica.examplatform.models.exam

import io.swagger.annotations.ApiModelProperty
import nl.han.ica.examplatform.models.question.Question
import java.util.Date

data class Exam(
        @ApiModelProperty(notes = "The ID of the exam")
        val examId: Int? = null,
        @ApiModelProperty(notes = "The name of the exam", required = true, allowEmptyValue = false)
        val name: String,
        @ApiModelProperty(notes = "The duration in minutes of the exam", required = true)
        val durationInMinutes: Int,
        @ApiModelProperty(notes = "The start time of the exam", required = true)
        val startTime: Date,
        @ApiModelProperty(notes = "The end time of the exam")
        val endTime: Date = Date(startTime.time + durationInMinutes),
        @ApiModelProperty(notes = "The course of the exam")
        val course: String,
        @ApiModelProperty(notes = "The version of the exam")
        val version: Int = 1,
        @ApiModelProperty(notes = "The type of the exam can be practice test or exam")
        val examType: ExamType,
        @ApiModelProperty(notes = "The instructions for the exam")
        val instructions: String? = null,
        @ApiModelProperty(notes = "The location of the exam")
        val location: String? = null,
        @ApiModelProperty(notes = "The questions in the exam")
        val questions: ArrayList<Question>? = null
)