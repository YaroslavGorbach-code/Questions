package yaroslavgorbach.questions.feature.recordings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import yaroslavgorbach.questions.R
import yaroslavgorbach.questions.feature.recordings.model.RecordsAction
import yaroslavgorbach.questions.feature.recordings.model.RecordsViewState
import yaroslavgorbach.questions.feature.recordings.presentation.RecordingsViewModel

@Composable
fun Recordings(onBack: () -> Unit) {
    Recordings(viewModel = hiltViewModel(), onBack = onBack)
}

@Composable
internal fun Recordings(
    viewModel: RecordingsViewModel,
    onBack: () -> Unit
) {
    val viewState = viewModel.state.collectAsState()

    Recordings(
        state = viewState.value,
        actioner = viewModel::submitAction,
        onBack = onBack
    )
}

@Composable
internal fun Recordings(
    state: RecordsViewState,
    actioner: (RecordsAction) -> Unit,
    onBack: () -> Unit
) {

    Column {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(id = R.string.recordings),
                style = MaterialTheme.typography.caption,
                fontSize = 38.sp,
                modifier = Modifier.weight(1f)
            )

            Icon(
                Icons.Default.Close,
                contentDescription = "",
                modifier = Modifier
                    .align(CenterVertically)
                    .size(28.dp)
                    .clickable { onBack() }
            )
        }

        LazyColumn {
            items(state.records) { record ->
                Record(recordUi = record)
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecordingsPreview() {
    Recordings {}
}