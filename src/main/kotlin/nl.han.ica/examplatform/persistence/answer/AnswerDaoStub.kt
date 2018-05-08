package nl.han.ica.examplatform.persistence.answer

import nl.han.ica.examplatform.models.answer.Answer
import org.springframework.stereotype.Repository

@Repository
class AnswerDaoStub : AnswerDAO {

    override fun addAnswerToQuestion(answer: Answer) {
        println(answer)
    }
}