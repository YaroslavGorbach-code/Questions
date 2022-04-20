package yaroslavgorbach.questions.feature.questions.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import yaroslavgorbach.questions.R
import yaroslavgorbach.questions.data.questions.model.Question
import yaroslavgorbach.questions.feature.common.ui.theme.QuestionsTheme
import yaroslavgorbach.questions.feature.questions.model.QuestionsAction
import yaroslavgorbach.questions.feature.questions.model.QuestionsViewState
import yaroslavgorbach.questions.feature.questions.presentation.QuestionViewModel

@Composable
fun Questions(navigateToRecords: () -> Unit) {
    Questions(viewModel = hiltViewModel(), navigateToRecords = navigateToRecords)
}

@Composable
internal fun Questions(
    viewModel: QuestionViewModel,
    navigateToRecords: () -> Unit
) {
    val viewState = viewModel.state.collectAsState()

    Questions(
        state = viewState.value,
        actioner = viewModel::submitAction,
        navigateToRecords = navigateToRecords
    )
}

@Composable
internal fun Questions(
    state: QuestionsViewState,
    actioner: (QuestionsAction) -> Unit,
    navigateToRecords: () -> Unit
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
                color = MaterialTheme.colors.onSurface,
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
                    state.isMenuExpended,
                    actioner,
                    navigateToRecords
                )
            }
        } else {
            Menu(
                Modifier
                    .align(BottomEnd)
                    .padding(end = 16.dp),
                state.isMenuExpended,
                actioner,
                navigateToRecords
            )
        }
    }
}

@Composable
private fun Menu(
    modifier: Modifier,
    isExpended: Boolean,
    actioner: (QuestionsAction) -> Unit,
    navigateToRecords: () -> Unit
) {
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

@ExperimentalMaterialApi
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExercisesPreview() {
    QuestionsTheme {
        Questions {


        }
    }
}