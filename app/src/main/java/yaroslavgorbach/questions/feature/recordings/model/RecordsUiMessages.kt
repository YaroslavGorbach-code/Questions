package yaroslavgorbach.questions.feature.recordings.model

sealed class RecordsUiMessages {
    class RecordDeletedSnack(val record: RecordUi) : RecordsUiMessages()
}