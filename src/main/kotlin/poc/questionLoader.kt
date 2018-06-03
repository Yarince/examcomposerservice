package poc

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.FileReader

internal fun loadQuestions(courseId: Int, studentNr: Int, answered: Boolean = false): Array<Question> {
    // Here the DB should get questions for courseId and studentNr
    val reader = JsonReader(FileReader("src/main/kotlin/poc/resources/${if (answered) "questionsAnswered" else "questionBankNotAnswered"}.json"))
    return Gson().fromJson(reader, Array<Question>::class.java)
}