package yaroslavgorbach.questions.di.data

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import yaroslavgorbach.questions.data.RepoQuestionsImp
import yaroslavgorbach.questions.data.factory.QuestionsFactory
import yaroslavgorbach.questions.data.questions.RepoQuestions
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataQuestionsModule {

    @Provides
    fun provideQuestionsFactory(app: Application): QuestionsFactory {
        return QuestionsFactory(app)
    }

    @Singleton
    @Provides
    fun provideQuestionsRepo(questions: QuestionsFactory): RepoQuestions {
        return RepoQuestionsImp(questions)
    }
}