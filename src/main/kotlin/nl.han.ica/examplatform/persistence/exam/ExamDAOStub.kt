package nl.han.ica.examplatform.persistence.exam

import nl.han.ica.examplatform.controllers.responseexceptions.ExamNotFoundException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.exam.SimpleExam
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.models.question.QuestionType
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ExamDAOStub {
    fun getExams(): Array<SimpleExam> {
        return arrayOf(SimpleExam(1, "SWA Toets 1", "SWA"),
                SimpleExam(2, "SWA Toets 2", "SWA"),
                SimpleExam(3, "APP Toets algoritmen", "APP")
        )
    }

    fun getExam(id: Int): Exam {
        // Database query on exam with this ID.
        // If it does not exist, throw: ExamNotFoundException
        // Here the DAO should query an exam, containing exam meta data, questions and answers
        // These values should then be put into an Exam object
        return Exam(examId = id,
                durationInMinutes = 10,
                startTime = Date(6000),
                course = "APP",
                examType = ExamType.EXAM,
                name = "Exam $id",
                location = "Nijmegen",
                instructions = "Use of a calculator is prohibited",
                questions = arrayListOf(Question(questionId = 1,
                        questionType = QuestionType.OPEN_QUESTION,
                        questionText = "Explain why DCAR is better",
                        examType = ExamType.EXAM), Question(questionId = 2,
                        questionType = QuestionType.MULTIPLE_CHOICE_QUESTION,
                        questionText = "Choose between A, B and C",
                        examType = ExamType.EXAM)
                )
        )
    }

    fun insertExam(exam: Exam): Exam {
        // Database logic needs to be added here
        return exam.copy()
    }

    fun updateExam(exam: Exam): Exam {
        // Database logic needs to be added here
        return exam.copy()
    }
}
