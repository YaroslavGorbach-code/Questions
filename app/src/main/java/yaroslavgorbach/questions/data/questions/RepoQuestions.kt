package yaroslavgorbach.questions.data.questions

import kotlinx.coroutines.flow.Flow
import yaroslavgorbach.questions.data.questions.model.Question

interface RepoQuestions {
    fun observe(): Flow<List<Question>>
}