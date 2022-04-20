package yaroslavgorbach.questions.feature.recordings.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import yaroslavgorbach.questions.feature.recordings.model.RecordsAction
import yaroslavgorbach.questions.feature.recordings.model.RecordsViewState
import yaroslavgorbach.questions.feature.recordings.presentation.RecordingsViewModel

@Composable
fun Recordings() {
    Recordings(viewModel = hiltViewModel())
}

@Composable
internal fun Recordings(
    viewModel: RecordingsViewModel,
) {
    val viewState = viewModel.state.collectAsState()

    Recordings(
        state = viewState.value,
        actioner = viewModel::submitAction,
    )
}

@Composable
internal fun Recordings(
    state: RecordsViewState,
    actioner: (RecordsAction) -> Unit
) {


}

@ExperimentalMaterialApi
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecordingsPreview() {
    Recordings()
}