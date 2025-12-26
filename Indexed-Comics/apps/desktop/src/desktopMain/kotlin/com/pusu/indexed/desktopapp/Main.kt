package com.pusu.indexed.desktopapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.pusu.indexed.shared.core.ui.SharedAppRoot

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Indexed Desktop") {
        SharedAppRoot()
    }
}
