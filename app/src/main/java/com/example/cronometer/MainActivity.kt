package com.example.cronometer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.ViewModel
import com.example.cronometer.ui.theme.CronometerTheme
import com.example.cronometer.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CronometerTheme {
                val viewModel: MainViewModel by viewModels()
               // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Cronometer(viewModel)
                }
            }
        }
    }

    @Composable
    fun Cronometer(viewModel: MainViewModel) {
        val timeRemaining by viewModel.timeRemaining.collectAsState()
        val (showTimePicker, setShowTimePicker) = remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "Time Remaining: ", modifier = Modifier.padding(bottom = 16.dp))
            Text(text = formatTimeFromSeconds(timeRemaining),
                modifier = Modifier.padding(bottom = 16.dp),
                style = TextStyle(fontSize = 36.sp)
            )
            Button(
                onClick = {
                    setShowTimePicker(true)
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Setup Cronometer")
            }
            Button(
                onClick = {
                    viewModel.stopCountdown()
                },
                modifier = Modifier.fillMaxWidth(),
            ){
                Text(text = "Stop Cronometer")
            }
        }

        if (showTimePicker) {
            CustomTimePickerDialog(
                onDismissRequest = { setShowTimePicker(false) },
                onTimeSelected = { hour, minute, second ->
                    val totalSeconds = hour * 3600 + minute * 60 + second
                    viewModel.startCountdown(totalSeconds)
                    setShowTimePicker(false) // Close the dialog after selecting a time
                }
            )
        }
    }

    @Composable
    fun CustomTimePickerDialog(
        title: String = "Select Time",
        onDismissRequest: () -> Unit,
        containerColor: Color = MaterialTheme.colorScheme.surface,
        onTimeSelected: (hour: Int, minute: Int, second: Int) -> Unit
    ) {
        var hourText by remember { mutableStateOf("") }
        var minuteText by remember { mutableStateOf("") }
        var secondText by remember { mutableStateOf("") }

        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Min)
                    .background(
                        shape = MaterialTheme.shapes.medium,
                        color = containerColor
                    ),
                color = containerColor
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        text = title,
                        style = MaterialTheme.typography.labelMedium
                    )

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ){
                        // Hour Picker
                        TimePickerTextField(
                            label = "Hour",
                            value = hourText,
                            onValueChange = { hourText = it },
                            modifier = Modifier
                                .weight(1f) // Each field gets equal width
                                .padding(end = 8.dp)
                        )

                        // Minute Picker
                        TimePickerTextField(
                            label = "Minute",
                            value = minuteText,
                            onValueChange = { minuteText = it.take(2).takeIf {
                                (it.toIntOrNull() ?: 0) <= 59
                            } ?: "59" },
                            modifier = Modifier
                                .weight(1f) // Each field gets equal width
                                .padding(end = 8.dp)
                        )

                        // Second Picker
                        TimePickerTextField(
                            label = "Second",
                            value = secondText,
                            onValueChange = { secondText = it.take(2).takeIf {
                                (it.toIntOrNull() ?: 0) <= 59
                            } ?: "59" },
                            modifier = Modifier.weight(1f) // Each field gets equal width
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        DismissButton {
                            onDismissRequest()
                        }
                        StartButton {
                            val hour = hourText.toIntOrNull() ?: 0
                            val minute = minuteText.toIntOrNull() ?: 0
                            val second = secondText.toIntOrNull() ?: 0
                            onTimeSelected(hour, minute, second)
                        }
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun CustomTimePickerDialogPreview() {
        // Define a sample time selected callback
        val onTimeSelected: (Int, Int, Int) -> Unit = { hour, minute, second ->
            // Do something with selected time
        }

        // Call your CustomTimePickerDialog composable with preview values
        CustomTimePickerDialog(
            title = "Select Time",
            onDismissRequest = { /* Preview action */ },
            containerColor = MaterialTheme.colorScheme.surface,
            onTimeSelected = onTimeSelected
        )
    }

    @Composable
    fun TimePickerTextField(
        label: String,
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        TextField(
            value = value,
            onValueChange = { onValueChange(it.take(2)) }, // Limit input to 2 characters
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = modifier
                .padding(bottom = 8.dp)
                .width(100.dp)
        )
    }

    @Composable
    fun StartButton(onClick: () -> Unit) {
        Button(
            onClick = onClick
        ) {
            Text("Start")
        }
    }

    @Composable
    fun DismissButton(onClick: () -> Unit) {
        Button(
            onClick = onClick
        ) {
            Text("Dismiss")
        }
    }

    fun formatTimeFromSeconds(totalSeconds: Int): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

}