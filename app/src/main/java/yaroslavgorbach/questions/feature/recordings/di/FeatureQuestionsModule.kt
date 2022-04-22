package yaroslavgorbach.questions.feature.recordings.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import yaroslavgorbach.questions.utills.player.RecordPlayer
import yaroslavgorbach.questions.utills.player.RecordPlayerImp

@Module
@InstallIn(SingletonComponent::class)
object FeatureRecordsModule {

    @Provides
    fun provideRecordPlayer(): RecordPlayer {
        return RecordPlayerImp()
    }

}