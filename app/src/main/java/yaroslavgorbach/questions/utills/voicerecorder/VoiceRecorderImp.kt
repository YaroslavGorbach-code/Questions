package yaroslavgorbach.questions.utills.voicerecorder

import android.content.Context
import android.media.MediaRecorder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class VoiceRecorderImp(private val context: Context) : VoiceRecorder {
    private var _isRecording: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override val isRecording: StateFlow<Boolean>
        get() = _isRecording

    private val _isRecordSaved: MutableSharedFlow<Unit> = MutableSharedFlow()

    override val isRecordSaved: SharedFlow<Unit>
        get() = _isRecordSaved

    private var mediaRecorder: MediaRecorder? = null

    override suspend fun start(fileName: String) {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(
                context.getExternalFilesDir("/")!!.absolutePath + "/" + createAudioFileName(
                    fileName
                )
            )
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            start()

            _isRecording.emit(true)
        }
    }

    override suspend fun stop() {
        mediaRecorder?.let { recorder ->
            try {
                recorder.stop()
                recorder.release()
                _isRecordSaved.emit(Unit)
            } catch (e: Exception) {
            }
            mediaRecorder = null

            _isRecording.emit(false)
        }
    }

    private fun createAudioFileName(fileName: String): String {
        val format = SimpleDateFormat("MM_dd_yyyy", Locale.getDefault())

        return (format.format(Date().time).toString() + ".3gp")
            .replace(" ", "_")
    }
}