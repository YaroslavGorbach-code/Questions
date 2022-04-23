package yaroslavgorbach.questions.feature.recordings.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import yaroslavgorbach.questions.feature.recordings.model.RecordUi
import yaroslavgorbach.questions.feature.recordings.model.RecordsAction
import java.io.File

@ExperimentalMaterialApi
@Composable
fun Record(
    modifier: Modifier,
    recordUi: RecordUi,
    playerProgress: Float,
    playerMaxProgress: Float,
    actioner: (RecordsAction) -> Unit
) {
    val dismissState = rememberDismissState(confirmStateChange = {
        if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
            actioner(RecordsAction.DeleteRecord(recordUi))
        }
        true
    })

    SwipeToDismiss(
        modifier = modifier,
        state = dismissState,
        dismissThresholds = {
            FractionalThreshold(0.2f)
        },
        background = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    else -> MaterialTheme.colors.primary
                }
            )
            val icon = Icons.Default.Delete

            val scale by animateFloatAsState(
                if (dismissState.targetValue != DismissValue.Default) 1.2f else 0.75f
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = Dp(20f)),
            ) {
                Icon(
                    icon,
                    contentDescription = "Delete Icon",
                    tint = Color.White,
                    modifier = Modifier
                        .scale(scale)
                        .align(Alignment.CenterEnd)
                        .size(24.dp)
                )

                Icon(
                    icon,
                    contentDescription = "Delete Icon",
                    tint = Color.White,
                    modifier = Modifier
                        .scale(scale)
                        .align(Alignment.CenterStart)
                        .size(24.dp)
                )
            }
        }
    ) {

        Card(
            shape = RectangleShape,
            modifier = Modifier.wrapContentSize(),
            elevation = animateDpAsState(
                if (dismissState.dismissDirection != null) 4.dp else 0.dp
            ).value,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .padding(horizontal = 8.dp)
                    .clickable {
                        actioner(RecordsAction.RecordCLick(recordUi))
                    },
            ) {

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colors.onSurface)
                )

                Row(
                    Modifier
                        .fillMaxSize()
                        .align(CenterHorizontally)
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Icon(
                        recordUi.playIconRes,
                        contentDescription = "",
                        Modifier
                            .align(CenterVertically)
                            .size(48.dp),
                        tint = MaterialTheme.colors.primary
                    )

                    Text(
                        text = recordUi.name,
                        modifier = Modifier
                            .align(CenterVertically)
                            .padding(start = 16.dp),
                        fontSize = 18.sp
                    )

                    Text(
                        text = recordUi.durationString,
                        modifier = Modifier
                            .align(CenterVertically)
                            .weight(1f),
                        fontSize = 18.sp,
                        textAlign = TextAlign.End
                    )
                }

                if (recordUi.recordState == RecordUi.RecordState.Playing || recordUi.recordState == RecordUi.RecordState.Pause) {
                    Slider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .padding(8.dp),
                        value = playerProgress,
                        valueRange = 0f..playerMaxProgress,
                        onValueChange = {

                        })
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colors.onSurface)
                )
            }
        }
    }
}


@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun RecordPreview() {
    Record(modifier = Modifier, RecordUi(File("")), 0f, 0f) {}
}