package yaroslavgorbach.questions.feature.recordings.model

import yaroslavgorbach.questions.utills.UiMessage

data class RecordsViewState(
    val records: List<RecordUi>,
    val currentPlayingProgress: Int,
    val maxPlayingProgress: Int,
    val messages: UiMessage<RecordsUiMessages>? = null
) {
    companion object {
        val Test = RecordsViewState(
            records = emptyList(),
            currentPlayingProgress = 0,
            maxPlayingProgress = 0
        )
    }
}
