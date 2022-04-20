package yaroslavgorbach.questions.bussines.recordings

import yaroslavgorbach.questions.data.recordings.RepoRecords
import javax.inject.Inject

class DeleteAllRecordsInteractor @Inject constructor(private val repoRecords: RepoRecords) {
    operator fun invoke() = repoRecords.deleteAllRecords()
}