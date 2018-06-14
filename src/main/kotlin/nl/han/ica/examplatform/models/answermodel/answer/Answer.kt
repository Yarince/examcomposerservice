package nl.han.ica.examplatform.models.answermodel.answer

import io.swagger.annotations.ApiModelProperty
import nl.han.ica.examplatform.models.question.Question

/**
 * Represents a correct answer given by a teacher.
 * This is <b>not</b> a answer that is given by a student.
 *
 * @param questionId [Int] The id of the [Question] this answer is for
 * @param example_answer [String] An example answer
 * @param description [String] The description of the Question
 * @param partial_answers [ArrayList]<[PartialAnswer]> List of parts that need to be in the answer
 * @param points [Int] The total amount of point for a question
 */
data class Answer(
        @ApiModelProperty(notes = "The id of the Question this answer is for")
        val questionId: Int,
        @ApiModelProperty(notes = "The description of the Question")
        val description: String? = null,
        @ApiModelProperty(notes = "The id of the Question this answer is for")
        val partial_answers: ArrayList<PartialAnswer>? = null,
        @ApiModelProperty(notes = "The total amount of point for a question")
        val points: Int? = partial_answers?.map { i -> i.points ?: 0 }?.sum()
)