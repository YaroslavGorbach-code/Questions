package yaroslavgorbach.questions.feature.recordings.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import yaroslavgorbach.questions.R
import yaroslavgorbach.questions.feature.recordings.model.RecordUi
import yaroslavgorbach.questions.feature.recordings.model.RecordsAction
import yaroslavgorbach.questions.feature.recordings.model.RecordsUiMessages
import yaroslavgorbach.questions.feature.recordings.model.RecordsViewState
import yaroslavgorbach.questions.feature.recordings.presentation.RecordingsViewModel

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun Recordings(onBack: () -> Unit) {
    Recordings(viewModel = hiltViewModel(), onBack = onBack)
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
internal fun Recordings(
    viewModel: RecordingsViewModel,
    onBack: () -> Unit
) {
    val viewState = viewModel.state.collectAsState()

    Recordings(
        state = viewState.value,
        actioner = viewModel::submitAction,
        onBack = onBack,
        clearMessage = viewModel::clearMessage
    )
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
internal fun Recordings(
    state: RecordsViewState,
    actioner: (RecordsAction) -> Unit,
    onBack: () -> Unit,
    clearMessage: (id: Long) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()


    state.messages?.let { message ->
        when (val m = message.message) {
            is RecordsUiMessages.RecordDeletedSnack -> {
                LaunchedEffect(key1 = message.id, block = {
                    val result =
                        scaffoldState.snackbarHostState.showSnackbar("Record deleted", "RESTORE")
                    when (result) {
                        SnackbarResult.Dismissed -> {
                            clearMessage(message.id)
                            actioner(RecordsAction.DeleteRecord(m.record))
                        }
                        SnackbarResult.ActionPerformed -> {
                            clearMessage(message.id)
                            actioner(RecordsAction.RestoreRecord(m.record))
                        }
                    }
                })
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(), scaffoldState = scaffoldState) {

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
                items(state.records, key = { it.name }) { record ->
                    Record(
                        modifier = Modifier.animateItemPlacement(),
                        recordUi = record,
                        playerProgress = state.currentPlayingProgress / 100f,
                        playerMaxProgress = state.maxPlayingProgress / 100f,
                        actioner = actioner
                    )
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecordingsPreview() {
    Recordings {}
}