package nl.han.ica.examplatform.persistence.question

import nl.han.ica.examplatform.models.question.Question

/**
 * Database access object that handles all database queries regarding [Question].
 */
interface IQuestionDAO {
    /**
     * Adds a question to the database.
     *
     * @param question [Question] The question to be added.
     * @return [Question] the inserted question
     */
    fun insertQuestion(question: Question, parentQuestionId: Int? = null): Question

    /**
     * Checks if a question already exists in the database.
     *
     * @param question [Question] the question which should be checked on existing.
     * @return [Boolean] true if it exists, false if not.
     **/
    fun exists(question: Question?): Boolean

    /**
     * Gets all questions of a course.
     *
     * @param courseId [Int] The ID of course of which the questions should be retrieved.
     * @return [Array]<[Question]> An array of all questions corresponding to the course.
     */
    fun getQuestionsByCourse(courseId: Int): Array<Question>

    /**
     * Gets all questions of a course, within specific categories
     *
     * @param courseId [Int] The ID of course of which the questions should be retrieved
     * @param categories [Array] An array containing all the categories of which the questions should be retrieved
     * @return [Array]<[Question]> An array of all questions corresponding to the course and categories
     */
    fun getQuestionsByCourseAndCategory(courseId: Int, categories: Array<String>): Array<Question>

    /**
     * Gets all questions of a course, within specific categories
     *
     * @param examId [Int] The ID of exam of which the questions should be retrieved
     * @return [Array]<[Question]> An array of all questions corresponding to the course and categories
     */
    fun getQuestionsByExam(examId: Int): ArrayList<Question>

    /**
     * Get a question by questionId.
     *
     * @param questionId [Int] The ID of the question which should be retrieved.
     * @return [Question] Question corresponding to the ID.
     */
    fun getQuestionById(questionId: Int): Question

    /**
     * Check if question is answered by students.
     *
     * @param questionIds [Array]<[Int]> The IDs of the questions
     * @return [Boolean] true if any of them have been answered, otherwise false
     */
    fun answersGivenOnQuestions(questionIds: Array<Int>): Boolean
}