package yaroslavgorbach.questions.utills.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File


class RecordPlayerImp : RecordPlayer, LifecycleObserver {

    private val _progress: MutableStateFlow<Int> = MutableStateFlow(0)

    override val progress: StateFlow<Int>
        get() = _progress

    private val _duration: MutableStateFlow<Int> = MutableStateFlow(0)

    override val duration: StateFlow<Int>
        get() = _duration

    private val _finishEvent: MutableSharedFlow<Unit> = MutableSharedFlow()

    override val finishEvent: SharedFlow<Unit>
        get() = _finishEvent

    private var mediaPlayer: MediaPlayer? = null

    private val progressHandler: Handler = Handler(Looper.getMainLooper())

    private var progressRunnable: Runnable? = null

    private fun startProgressRunnable() {
        progressRunnable = object : Runnable {
            override fun run() {
                progressHandler.postDelayed(this, 100)
                if (mediaPlayer != null) {
                    try {
                        _progress.tryEmit(mediaPlayer!!.currentPosition)
                        _duration.tryEmit(mediaPlayer!!.duration)
                    } catch (e: Exception) {
                    }
                }
            }
        }

        progressHandler.postDelayed(progressRunnable!!, 300)
    }

    private fun stopProgressRunnable() {
        progressRunnable?.let(progressHandler::removeCallbacks)
    }

    override suspend fun play(file: File) {

        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            stop()
        }

        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setDataSource(file.absolutePath)
            mediaPlayer!!.prepare()
            startProgressRunnable()
        } catch (exception: Exception) {

        }

        mediaPlayer?.setOnPreparedListener { mediaPlayer!!.start() }
        mediaPlayer?.setOnCompletionListener {
            GlobalScope.launch {
                _finishEvent.emit(Unit)
                stop()
            }
        }
    }

    override suspend fun stop() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
            _progress.emit(0)
            stopProgressRunnable()
        }
    }

    override suspend fun pause() {
        if (mediaPlayer != null) {
            mediaPlayer!!.pause()
            stopProgressRunnable()
        }
    }

    override suspend fun resume() {
        if (mediaPlayer != null) {
            mediaPlayer!!.start()
            startProgressRunnable()
        }
    }

    override suspend fun seekToo(progress: Int) {
        mediaPlayer!!.seekTo(progress)
    }
}