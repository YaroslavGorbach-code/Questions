package yaroslavgorbach.questions.feature.recordings.model

data class RecordsViewState(
    val records: List<RecordUi>,
    val currentPlayingProgress: Int,
    val maxPlayingProgress: Int
) {
    companion object {
        val Test = RecordsViewState(records = emptyList(), currentPlayingProgress = 0, maxPlayingProgress = 0)
    }
}
