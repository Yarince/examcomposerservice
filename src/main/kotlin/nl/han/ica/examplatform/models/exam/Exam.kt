package nl.han.ica.examplatform.models.exam

import io.swagger.annotations.ApiModelProperty
import nl.han.ica.examplatform.models.question.Question
import java.util.*

/**
 * Represents an official exam.
 *
 * @param examId [Int] The ID of the exam
 * @param name [String] The name of the exam
 * @param durationInMinutes [Int] The duration in minutes of the exam
 * @param startTime [Date] The start time of the exam
 * @param endTime [Date] The end time of the exam
 * @param courseId [Int] The ID of the course that the exam is for
 * @param version [Int] The version of the exam
 * @param examType [String] The type of the exam can be practice test or exam
 * @param instructions [String] The instructions for the exam
 * @param location [String] The location of the exam
 * @param readyForDownload [Boolean] Boolean that states if students can download the exam
 * @param questions [ArrayList]<[Question]> The questions in the exam
 */
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
        @ApiModelProperty(notes = "The ID of the course that the exam is for", required = true)
        val courseId: Int,
        @ApiModelProperty(notes = "The version of the exam")
        val version: Int = 1,
        @ApiModelProperty(notes = "The type of the exam can be practice test or exam")
        val examType: String,
        @ApiModelProperty(notes = "The instructions for the exam")
        val instructions: String? = null,
        @ApiModelProperty(notes = "The location of the exam")
        val location: String? = null,
        @ApiModelProperty(notes = "Boolean that states if students can download the exam")
        val readyForDownload: Boolean? = false,
        @ApiModelProperty(notes = "The questions in the exam")
        val questions: ArrayList<Question>? = null,
        @ApiModelProperty(notes = "Codes to be able to decrypt exam files")
        val decryptionCodes: String? = null
)
