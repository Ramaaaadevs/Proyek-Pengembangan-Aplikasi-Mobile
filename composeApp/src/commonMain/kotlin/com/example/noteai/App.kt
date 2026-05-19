package com.example.noteai

import androidx.compose.runtime.Composable
import com.example.noteai.presentation.navigation.AppNavHost
import com.example.noteai.presentation.theme.NoteAITheme

@Composable
fun App() {
    NoteAITheme {
        AppNavHost()
    }
}
