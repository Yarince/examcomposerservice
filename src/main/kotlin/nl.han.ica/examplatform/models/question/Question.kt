package nl.han.ica.examplatform.models.question

import io.swagger.annotations.ApiModelProperty


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
        val questionPoints: Float? = null,
        @ApiModelProperty(notes = "ID of the course")
        val courseId: Int? = null,
        @ApiModelProperty(notes = "Type of exam the question is for.")
        val examType: String,
        @ApiModelProperty(notes = "The categories that this question is about")
        val categories: ArrayList<String> = arrayListOf(),
        @ApiModelProperty(notes = "A question can contain subquestions when the type is noQuestion")
        val subQuestions: ArrayList<Question>? = null
)
