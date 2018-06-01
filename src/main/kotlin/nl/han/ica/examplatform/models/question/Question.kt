package nl.han.ica.examplatform.models.question

import io.swagger.annotations.ApiModelProperty
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.answermodel.answer.Answer

/**
 * Represents a question that can be asked in a [Exam].
 * A Question needs to have a [Answer] connected to it.
 *
 * @param questionId [Int] The id that the Question has in the database
 * @param questionOrderInExam [Int] The index number that represents the position of the question in the exam
 * @param questionOrderText [String] Text that go's along with the [questionOrderInExam]
 * @param questionType [String] The type of question this question is
 * @param questionText [String] The sentence that makes a request for information
 * @param questionPoints [Int] The amount of point the question is worth
 * @param courseId [Int] The ID of the course the question is for
 * @param examType [String] The type of exam the question is for
 * @param pluginVersion [String] The version of the plugin used as question type
 * @param categories [ArrayList]<[String]> The list of categories of this question
 * @param subQuestions [Array]<[Question]> List of questions that represent one larger question
 */
data class Question(
        @ApiModelProperty(notes = "The ID of the question")
        val questionId: Int? = null,
        @ApiModelProperty(notes = "Order of question in an Exam")
        val questionOrderInExam: Int? = null,
        @ApiModelProperty(notes = "Text of order in exam text. Example: \"Question 1\" or \"a\"", required = true)
        val questionOrderText: String? = null,
        @ApiModelProperty(notes = "This is the name of the plugin used, e.g. OpenQuestion", required = true)
        val questionType: String,
        @ApiModelProperty(notes = "Text of the question. This could be null if the question has subquestions")
        val questionText: String? = null,
        @ApiModelProperty(notes = "Points assigned to a question in an Exam")
        val questionPoints: Int? = null,
        @ApiModelProperty(notes = "ID of the course")
        val courseId: Int? = null,
        @ApiModelProperty(notes = "Type of exam the question is for.")
        val examType: String,
        @ApiModelProperty(notes = "The version of the plugin used as questionType. References plugin version in database.", required = true)
        val pluginVersion: String,
        @ApiModelProperty(notes = "The categories that this question is about")
        val categories: ArrayList<String> = arrayListOf(),
        @ApiModelProperty(notes = "A question can contain subQuestions when the type is noQuestion")
        val subQuestions: ArrayList<Question>? = null
)