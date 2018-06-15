package nl.han.ica.examplatform.persistence.partialanswer

import nl.han.ica.examplatform.models.answermodel.answer.PartialAnswer


/**
 * Database access object that handles all database queries regarding [PartialAnswer].
 */
interface IPartialAnswerDAO {

    /**
     * Insert partial answers in the database.
     */
    fun insertPartialAnswers(questionId: Int, partialAnswers: ArrayList<PartialAnswer>)

    /**
     * Update partialAnswers in the database.
     */
    fun updatePartialAnswers(questionId: Int, partialAnswers: ArrayList<PartialAnswer>)

    /**
     * Delete the partial answer from the database.
     */
    fun deletePartialAnswer(questionId: Int, partialAnswer: PartialAnswer)

    /**
     * Insert partial answers in a exam in the database
     */
    fun insertPartialAnswersInExam(questionId: Int, partialAnswers: ArrayList<PartialAnswer>)

    /**
     * Delete a partial answer from a exam in the database.
     */
    fun deletePartialAnswerFromExam(questionId: Int, partialAnswer: PartialAnswer)
}