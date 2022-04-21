package yaroslavgorbach.questions.feature.questions.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import yaroslavgorbach.questions.utills.permition.PermissionManager
import yaroslavgorbach.questions.utills.permition.PermissionManagerImp
import yaroslavgorbach.questions.utills.voicerecorder.VoiceRecorder
import yaroslavgorbach.questions.utills.voicerecorder.VoiceRecorderImp

@Module
@InstallIn(SingletonComponent::class)
object FeatureQuestionsModule {

    @Provides
    fun provideVoiceRecorder(app: Application): VoiceRecorder {
        return VoiceRecorderImp(app)
    }

    @Provides
    fun providePermissionManager(app: Application): PermissionManager {
        return PermissionManagerImp(app)
    }
}