package yaroslavgorbach.questions.feature.recordings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import yaroslavgorbach.questions.feature.recordings.model.RecordUi
import java.io.File

@Composable
fun Record(recordUi: RecordUi) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(horizontal = 8.dp)
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

        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .padding(8.dp),
            value = recordUi.progress / 100f,
            onValueChange = {

            })

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colors.onSurface)
        )
    }
}


@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun RecordPreview() {
    Record(RecordUi(File("")))

}