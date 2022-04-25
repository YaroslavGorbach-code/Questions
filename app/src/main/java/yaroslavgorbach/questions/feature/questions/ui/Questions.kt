package yaroslavgorbach.questions.feature.questions.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.SettingsVoice
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import yaroslavgorbach.questions.R
import yaroslavgorbach.questions.data.questions.model.Question
import yaroslavgorbach.questions.feature.common.ui.theme.QuestionsTheme
import yaroslavgorbach.questions.feature.questions.model.QuestionsAction
import yaroslavgorbach.questions.feature.questions.model.QuestionsUiMessage
import yaroslavgorbach.questions.feature.questions.model.QuestionsViewState
import yaroslavgorbach.questions.feature.questions.presentation.QuestionViewModel

@Composable
fun Questions(navigateToRecords: () -> Unit) {
    Questions(viewModel = hiltViewModel(), navigateToRecords = navigateToRecords)
}

@Composable
internal fun Questions(
    viewModel: QuestionViewModel,
    navigateToRecords: () -> Unit,
) {
    val viewState = viewModel.state.collectAsState()

    Questions(
        state = viewState.value,
        actioner = viewModel::submitAction,
        navigateToRecords = navigateToRecords,
        clearMessage = viewModel::clearMessage
    )
}

@Composable
internal fun Questions(
    state: QuestionsViewState,
    actioner: (QuestionsAction) -> Unit,
    navigateToRecords: () -> Unit,
    clearMessage: (Long) -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    ) {

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                actioner(QuestionsAction.StopRecording)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    state.message?.let { message ->
        val context = LocalContext.current
        when (message.message) {
            QuestionsUiMessage.RequestAudioPermission -> {
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        clearMessage(message.id)
                        if (isGranted) {
                            actioner(QuestionsAction.StartRecording)
                        } else {
                            actioner(QuestionsAction.ShowPermissionWarningDialog)
                        }
                    })

                LaunchedEffect(key1 = message.id, block = {
                    launcher.launch(Manifest.permission.RECORD_AUDIO)
                })
            }
            QuestionsUiMessage.GoToPermissionSettings -> {
                val launcher = rememberLauncherForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) {
                    clearMessage(message.id)
                }

                LaunchedEffect(key1 = message.id, block = {
                    launcher.launch(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    })
                })
            }
            QuestionsUiMessage.StopRecording -> {
                Toast.makeText(
                    LocalContext.current,
                    stringResource(id = R.string.record_saved),
                    Toast.LENGTH_LONG
                ).show()
                clearMessage(message.id)
            }
        }
    }

    if (state.isPermissionWarningDialogVisible) {
        AlertDialog(
            onDismissRequest = {
                actioner(QuestionsAction.HidePermissionWarningDialog)
            },
            title = { Text(stringResource(id = R.string.permission_denied_error_title), fontSize = 18.sp) },
            text = { Text(stringResource(id = R.string.audio_permission_denied_message), fontSize = 14.sp) },
            buttons = {
                Column(
                    modifier = Modifier.padding(all = 8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(4.dp),
                        onClick = {
                            actioner(QuestionsAction.HidePermissionWarningDialog)
                        }
                    ) {
                        Text(stringResource(id = R.string.cancel))
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(4.dp),
                        onClick = {
                            actioner(QuestionsAction.GoToPermissionSettings)
                            actioner(QuestionsAction.HidePermissionWarningDialog)
                        }
                    ) {
                        Text(stringResource(id = R.string.grant_permission))
                    }
                }
            }
        )
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxSize()

            ) {
                Column {
                    AppBarText()
                    Question(state.question, actioner)
                }

                if (state.isMenuExpended) {
                    Box(
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.5f))
                            .fillMaxSize()
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                actioner(QuestionsAction.ExpendOrHideMenu)
                            }
                    )
                }
                }

                NewQuestionButton(
                    modifier = Modifier.weight(0.4f),
                    state = state,
                    actioner = actioner,
                    navigateToRecords = navigateToRecords
                )
            }
        }
    }

@Composable
private fun Question(question: Question?, actioner: (QuestionsAction) -> Unit) {
    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 32.dp)
            .background(
                color = MaterialTheme.colors.onPrimary,
                shape = RoundedCornerShape(15.dp)
            )
            .fillMaxSize()
    ) {
        if (question == null){
            actioner(QuestionsAction.LoadQuestions)
        }

        question?.let { question ->
            Text(
                text = question.text,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .align(Center)
                    .padding(16.dp),
                fontSize = 28.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
private fun AppBarText() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.questions),
            style = MaterialTheme.typography.caption,
            fontSize = 38.sp
        )

        Text(
            text = stringResource(id = R.string.instruction),
            style = MaterialTheme.typography.caption,
            fontSize = 18.sp
        )
    }
}

@Composable
private fun NewQuestionButton(
    modifier: Modifier,
    state: QuestionsViewState,
    actioner: (QuestionsAction) -> Unit,
    navigateToRecords: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Center)
                .size(150.dp)
                .clickable {
                    actioner(QuestionsAction.ChangeQuestion)
                }
                .background(
                    color = MaterialTheme.colors.onPrimary,
                    shape = RoundedCornerShape(30.dp)
                )
        ) {
            Icon(
                modifier = Modifier
                    .size(100.dp)
                    .align(Center),
                imageVector = Icons.Default.Casino,
                contentDescription = "",
                tint = MaterialTheme.colors.primary
            )
        }

        Spacer(modifier = Modifier.size(100.dp))

        if (state.isMenuExpended) {
            Box(
                modifier = Modifier
                    .background(if (state.isMenuExpended) Color.Black.copy(alpha = 0.5f) else Color.Transparent)
                    .fillMaxSize()
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        if (state.isMenuExpended) actioner(QuestionsAction.ExpendOrHideMenu)
                    }
            ) {
                Menu(
                    Modifier
                        .align(BottomEnd)
                        .padding(end = 16.dp),
                    state,
                    actioner,
                    navigateToRecords
                )
            }
        } else {
            Menu(
                Modifier
                    .align(BottomEnd)
                    .padding(end = 16.dp),
                state,
                actioner,
                navigateToRecords
            )
        }
    }
}

@Composable
private fun Menu(
    modifier: Modifier,
    state: QuestionsViewState,
    actioner: (QuestionsAction) -> Unit,
    navigateToRecords: () -> Unit
) {
    Column(modifier) {
        if (state.isMenuExpended) {

            Row(
                Modifier
                    .padding(end = 16.dp)
                    .clickable {
                        actioner(QuestionsAction.ExpendOrHideMenu)

                        if (state.isRecordPermissionGranted) {
                            actioner(QuestionsAction.StartRecording)
                        } else {
                            actioner(QuestionsAction.RequestAudioPermission)
                        }
                    }) {
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .align(CenterVertically)
                        .width(140.dp)
                        .padding(end = 8.dp)
                        .background(
                            color = MaterialTheme.colors.onPrimary,
                            shape = RoundedCornerShape(4.dp)
                        ),
                ) {
                    Text(
                        text = stringResource(id = R.string.start_recording),
                        modifier = Modifier
                            .align(CenterStart)
                            .padding(start = 8.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = MaterialTheme.colors.onPrimary,
                            shape = RoundedCornerShape(100.dp)
                        )
                ) {
                    Icon(
                        Icons.Default.Mic,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Center),
                        contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.size(8.dp))

            Row(modifier = Modifier.clickable {
                actioner(QuestionsAction.ExpendOrHideMenu)
                navigateToRecords()
            }) {
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .align(CenterVertically)
                        .width(140.dp)
                        .padding(end = 8.dp)
                        .background(
                            color = MaterialTheme.colors.onPrimary,
                            shape = RoundedCornerShape(4.dp)
                        ),
                ) {
                    Text(
                        text = stringResource(id = R.string.recordings),
                        modifier = Modifier
                            .align(CenterStart)
                            .padding(start = 8.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = MaterialTheme.colors.onPrimary,
                            shape = RoundedCornerShape(100.dp)
                        )
                ) {
                    Icon(
                        Icons.Default.List,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Center),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
        }

        Box(
            modifier = Modifier
                .size(75.dp)
                .align(End)
                .clickable {
                    if (state.isRecording) {
                        actioner(QuestionsAction.StopRecording)
                    } else {
                        actioner(QuestionsAction.ExpendOrHideMenu)
                    }
                }
                .background(
                    color = MaterialTheme.colors.onPrimary,
                    shape = RoundedCornerShape(100.dp)
                )
        ) {
            if (state.isRecording) {
                Icon(
                    Icons.Default.Mic,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Center),
                    contentDescription = null,
                    tint = Color.Red
                )
            } else {
                Icon(
                    Icons.Default.SettingsVoice,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Center),
                    contentDescription = null
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExercisesPreview() {
    QuestionsTheme {
        Questions {


        }
    }
}