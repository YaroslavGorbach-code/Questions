package yaroslavgorbach.questions.bussines.questions

import kotlinx.coroutines.flow.Flow
import yaroslavgorbach.questions.data.questions.model.Question
import yaroslavgorbach.questions.data.questions.RepoQuestions
import javax.inject.Inject

class GetQuestionsInterractor @Inject constructor(private val repoQuestions: RepoQuestions) {
    operator fun invoke(): Flow<List<Question>> {
        return repoQuestions.observe()
    }
}