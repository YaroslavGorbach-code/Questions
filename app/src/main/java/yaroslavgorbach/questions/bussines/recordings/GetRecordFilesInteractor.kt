package yaroslavgorbach.questions.bussines.recordings

import yaroslavgorbach.questions.data.recordings.RepoRecords
import java.io.File
import javax.inject.Inject

class GetRecordFilesInteractor @Inject constructor(private val repoRecords: RepoRecords) {

    operator fun invoke(): List<File> {
        return repoRecords.getRecordFiles()
    }

}