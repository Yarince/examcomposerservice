package nl.han.ica.examplatform.business.answer

import nl.han.ica.examplatform.models.answermodel.answer.Answer
import nl.han.ica.examplatform.persistence.answer.AnswerDAO
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
internal class AnswerServiceTest {

    @Mock
    private lateinit var answerDAO: AnswerDAO

    @InjectMocks
    private lateinit var answerService: AnswerService

    @Test
    fun testAddOrUpdateAnswerInQuestion() {
        val answer = Answer(questionId = 1)

        answerService.addOrUpdateAnswerInQuestion(answer)
        verify(answerDAO, times(1)).addOrUpdateAnswerInQuestion(answer)
    }

    @Test
    fun testAddOrUpdateAnswerInQuestionInExam(){
        val answer = Answer(questionId = 1)
        val examID  = 1

        answerService.addOrUpdateAnswerInQuestionInExam(answer, examID)
        verify(answerDAO,
                times(1))
                .addOrUpdateAnswerInQuestionInExam(answer, examID)
    }

    @Test
    fun testGetAnswerForQuestion(){
        val questionId = 1

        answerService.getAnswerForQuestion(questionId)
        verify(answerDAO,
                times(1))
                .getAnswerForQuestion(questionId)
    }

    @Test
    fun testGetAnswersForExam(){
        val examId = 1

        answerService.getAnswersForExam(examId)
        verify(answerDAO,
                times(1))
                .getAnswersForExam(examId)
    }
}