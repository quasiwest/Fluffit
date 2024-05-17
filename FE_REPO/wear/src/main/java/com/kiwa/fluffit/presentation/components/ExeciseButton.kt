package com.kiwa.fluffit.presentation.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.*
import com.kiwa.fluffit.R
import com.kiwa.fluffit.presentation.exercise.ExerciseViewModel
import com.kiwa.fluffit.presentation.theme.fluffitWearFontFamily

@Composable
fun ExerciseButton() {
    val exerciseViewModel: ExerciseViewModel = hiltViewModel()
    val isRunning by exerciseViewModel.isTimerRunning.collectAsState()

    val buttonText = if (isRunning)
        stringResource(R.string.exercise_stop_button) else stringResource(R.string.exercise_button)

    val context = LocalContext.current

    Box(modifier = Modifier
        .padding(5.dp)
        .fillMaxSize()) {
        Button(
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .width(80.dp)
                .height(30.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 4.dp),
            onClick = {
                if (isRunning) {
                    exerciseViewModel.pauseTimer()
//                    Toast.makeText(context, "Exercise Paused", Toast.LENGTH_SHORT).show()
                } else {
                    exerciseViewModel.startTimer()
//                    Toast.makeText(context, "Exercise Started", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text(text = buttonText, fontFamily = fluffitWearFontFamily)
        }
    }
}
