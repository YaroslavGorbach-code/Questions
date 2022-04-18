package yaroslavgorbach.questions.data.questions

import kotlinx.coroutines.flow.Flow
import yaroslavgorbach.questions.data.model.Question

interface RepoQuestions {
    fun observe(): Flow<List<Question>>
}