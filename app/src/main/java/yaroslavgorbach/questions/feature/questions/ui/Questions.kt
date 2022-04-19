package yaroslavgorbach.questions.feature.questions.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.SettingsVoice
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import yaroslavgorbach.questions.R
import yaroslavgorbach.questions.feature.common.ui.theme.QuestionsTheme
import yaroslavgorbach.questions.feature.questions.model.QuestionsAction
import yaroslavgorbach.questions.feature.questions.model.QuestionsViewState
import yaroslavgorbach.questions.feature.questions.presentation.QuestionViewModel

@Composable
fun Questions() {
    Questions(viewModel = hiltViewModel())
}

@Composable
internal fun Questions(
    viewModel: QuestionViewModel,
) {
    val viewState = viewModel.state.collectAsState()

    Questions(
        state = viewState.value,
        actioner = viewModel::submitAction,
    )
}

@Composable
internal fun Questions(
    state: QuestionsViewState,
    actioner: (QuestionsAction) -> Unit
) {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Box(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxSize()

                ){
                    Column {
                        AppBarText()
                        Question(Modifier.weight(1f))
                    }

                    Box(
                        modifier = Modifier
                            .background(if (state.isMenuExpended) Color.Black.copy(alpha = 0.3f) else Color.Transparent)
                            .fillMaxSize()
                    )
                }

                NewQuestionButton(
                    modifier = Modifier.weight(0.4f),
                    state = state,
                    actioner = actioner
                )
            }
        }
    }

@Composable
private fun Question(modifier: Modifier) {
    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 32.dp)
            .background(
                color = MaterialTheme.colors.onSurface,
                shape = RoundedCornerShape(15.dp)
            )
            .fillMaxSize()
    ) {

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
    actioner: (QuestionsAction) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .align(Center)
                .size(150.dp)
                .background(
                    color = MaterialTheme.colors.onSurface,
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

        Box(
            modifier = Modifier
                .background(if (state.isMenuExpended) Color.Black.copy(alpha = 0.3f) else Color.Transparent)
                .fillMaxSize()
        ) {
            Menu(
                Modifier
                    .align(BottomEnd)
                    .padding(end = 16.dp),
                state.isMenuExpended,
                actioner
            )
        }

    }
}

@Composable
private fun Menu(modifier: Modifier, isExpended: Boolean, actioner: (QuestionsAction) -> Unit) {
    Column(modifier) {
        if (isExpended) {
            Row(Modifier.padding(end = 16.dp)) {
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .align(CenterVertically)
                        .width(140.dp)
                        .padding(end = 8.dp)
                        .background(
                            color = MaterialTheme.colors.onSurface,
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
                            color = MaterialTheme.colors.onSurface,
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

            Row {
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .align(CenterVertically)
                        .width(140.dp)
                        .padding(end = 8.dp)
                        .background(
                            color = MaterialTheme.colors.onSurface,
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
                            color = MaterialTheme.colors.onSurface,
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
                .clickable { actioner(QuestionsAction.ExpendOrHideMenu) }
                .background(
                    color = MaterialTheme.colors.onSurface,
                    shape = RoundedCornerShape(100.dp)
                )
        ) {
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

@Composable
private fun backgroundColor(isMenuExpended: Boolean): Color {
    return if (isMenuExpended) {
        Color.Black.copy(alpha = 0.3f)
    } else {
        MaterialTheme.colors.background
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExercisesPreview() {
    QuestionsTheme {
        Questions()
    }
}