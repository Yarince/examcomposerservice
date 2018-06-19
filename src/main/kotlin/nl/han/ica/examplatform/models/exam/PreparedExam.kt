package nl.han.ica.examplatform.models.exam

import io.swagger.annotations.ApiModelProperty
import nl.han.ica.examplatform.models.question.Question
import java.util.*

/**
 * A prepared exam is an exam that is ready for a student to do.
 *
 * @param examId [Int] The ID of the exam.
 * @param name [String] The name of the exam.
 * @param durationInMinutes [Int] The duration in minutes of the exam.
 * @param startTime [Date] The start time of the exam.
 * @param endTime [Date] The end time of the exam.
 * @param courseName [String] The name of the course that the question is for.
 * @param version [Int] The version number of the exam.
 * @param examType [String] The name of the plugin.
 * @param instructions [String] Instructions for the exam.
 * @param location [String] The location of which the exam is assessed.
 * @param classes [Array]<[String]> The classes the exam is for.
 * @param creator [String] The creator of the exam.
 * @param tools [String] The tools that can be used for the exam.
 * @param questions [Array]<[Question]> The questions in the exam.
 */
data class PreparedExam(
        @ApiModelProperty(notes = "The ID of the exam")
        val examId: Int,
        @ApiModelProperty(notes = "The name of the exam", required = true, allowEmptyValue = false)
        val name: String,
        @ApiModelProperty(notes = "The duration in minutes of the exam", required = true)
        val durationInMinutes: Int,
        @ApiModelProperty(notes = "The start time of the exam", required = true)
        val startTime: Date,
        @ApiModelProperty(notes = "The end time of the exam")
        val endTime: Date = Date(startTime.time + durationInMinutes),
        @ApiModelProperty(notes = "The name of the course that the question is for", required = true)
        val courseName: String,
        @ApiModelProperty(notes = "The version of the exam")
        val version: Int = 1,
        @ApiModelProperty(notes = "The type of the exam can be practice test or exam")
        val examType: String,
        @ApiModelProperty(notes = "The instructions for the exam")
        val instructions: String? = null,
        @ApiModelProperty(notes = "The location of the exam")
        val location: String? = null,
        @ApiModelProperty(notes = "The classes that the exam is for")
        val classes: Array<String>,
        @ApiModelProperty(notes = "The creator of the exam")
        val creator: String,
        @ApiModelProperty(notes = "The tools that can be used during the exam")
        val tools: String? = null,
        @ApiModelProperty(notes = "The questions in the exam")
        val questions: ArrayList<Question>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PreparedExam

        if (!Arrays.equals(classes, other.classes)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(classes)
    }
}
