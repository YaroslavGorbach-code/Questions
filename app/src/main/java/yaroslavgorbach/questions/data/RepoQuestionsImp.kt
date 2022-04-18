package yaroslavgorbach.questions.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import yaroslavgorbach.questions.data.factory.QuestionsFactory
import yaroslavgorbach.questions.data.model.Question
import yaroslavgorbach.questions.data.questions.RepoQuestions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoQuestionsImp @Inject constructor(private val questionsFactory: QuestionsFactory) : RepoQuestions {
    override fun observe(): Flow<List<Question>> {
        return flowOf(questionsFactory.create())
    }
}