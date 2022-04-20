package yaroslavgorbach.questions.data.questions.factory

import android.content.Context
import yaroslavgorbach.questions.R
import yaroslavgorbach.questions.data.questions.model.Question

class QuestionsFactory(private val context: Context) {
    fun create(): List<Question> {
        return context.resources.getStringArray(R.array.questions).map { questionText ->
            Question(text = questionText)
        }.shuffled()
    }
}