package nl.han.ica.examplatform.persistence.partialanswer

import nl.han.ica.examplatform.models.answermodel.answer.PartialAnswer
import org.springframework.stereotype.Repository

@Repository
class PartialAnswerDAO: IPartialAnswerDAO {
    override fun deletePartialAnswer(questionId: Int, partialAnswer: PartialAnswer) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertPartialAnswersInExam(questionId: Int, partialAnswers: ArrayList<PartialAnswer>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deletePartialAnswerFromExam(questionId: Int, partialAnswer: PartialAnswer) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}