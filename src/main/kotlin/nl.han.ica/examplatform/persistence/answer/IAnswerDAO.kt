package nl.han.ica.examplatform.persistence.answer

import nl.han.ica.examplatform.models.answermodel.answer.Answer

/**
 * Interface that has to be implemented by all Answers.
 */
interface IAnswerDAO {

    /**
     * Add an Answer to a Question in the database.
     *
     * @param answer [Answer] The Answer you want to add to a [Question]
     */
    fun addAnswerToQuestion(answer: Answer)
}
