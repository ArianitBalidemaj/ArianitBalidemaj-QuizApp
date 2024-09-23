package com.example.arianitbalidemaj_quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arianitbalidemaj_quizapp.ui.theme.ArianitBalidemajQuizAppTheme
import androidx.compose.foundation.border
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArianitBalidemajQuizAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    QuizApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

data class Flashcard(val question: String, val answer: String)

@Composable
fun QuizApp(modifier: Modifier = Modifier) {
    val flashcards = listOf(
        Flashcard("What is the capital of Germany?", "Frankfurt"),
        Flashcard("What is 8 * 8?", "64"),
        Flashcard("What is the country with the biggest population?", "China")
    )

    var currentFlashcard by remember { mutableStateOf(0) }
    var userAnswer by remember { mutableStateOf("") }
    var quizComplete by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        SnackbarHost(hostState = snackbarHostState)

        if (quizComplete) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Quiz Complete!",
                        modifier = Modifier.padding(16.dp)
                    )
                    Button(
                        onClick = {
                            currentFlashcard = 0
                            userAnswer = ""
                            quizComplete = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Restart Quiz")
                    }
                }
            }
            LaunchedEffect(Unit) {
                scope.launch {
                    snackbarHostState.showSnackbar("Quiz Complete!")
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = flashcards[currentFlashcard].question,
                    modifier = Modifier.padding(16.dp)
                )
            }

            BasicTextField(
                value = userAnswer,
                onValueChange = { userAnswer = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                singleLine = true
            )

            Button(
                onClick = {
                    val correctAnswer = flashcards[currentFlashcard].answer
                    if (userAnswer.trim().equals(correctAnswer, ignoreCase = true)) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Correct!")
                        }
                        if (currentFlashcard + 1 >= flashcards.size) {
                            quizComplete = true
                        } else {
                            currentFlashcard++
                            userAnswer = ""
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Incorrect! Try again.")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Answer")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ArianitBalidemajQuizAppTheme {
        QuizApp()
    }
}