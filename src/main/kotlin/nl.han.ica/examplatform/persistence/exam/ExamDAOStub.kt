package nl.han.ica.examplatform.persistence.exam

import nl.han.ica.examplatform.controllers.responseExceptions.ExamNotFoundException
import nl.han.ica.examplatform.models.exam.Exam
import nl.han.ica.examplatform.models.exam.ExamType
import nl.han.ica.examplatform.models.question.Question
import nl.han.ica.examplatform.models.question.QuestionType
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ExamDAOStub {
    fun insertExam(exam: Exam): Exam {
        return exam
    }

    fun getExam(id: Int): Exam {
        // Database query on exam with this ID.
        // If it does not exist, throw: ExamNotFoundException
        // For testing purposes, ID -9999 throws a not found.
        // This has to be changed later
        if (id == -9999) {
            throw ExamNotFoundException("Exam with id $id was not found.")
        }

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
                questions = arrayOf(Question(questionId = 1,
                        questionType = QuestionType.OPEN_QUESTION,
                        questionText = "Explain why DCAR is better",
                        examType = ExamType.EXAM), Question(questionId = 2,
                        questionType = QuestionType.MULTIPLE_CHOICE_QUESTION,
                        questionText = "Choose between A, B and C",
                        examType = ExamType.EXAM)
                )
        )
    }
}