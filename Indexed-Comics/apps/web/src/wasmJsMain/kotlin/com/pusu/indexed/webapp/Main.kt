package com.pusu.indexed.webapp

import androidx.compose.runtime.Composable
import com.pusu.indexed.shared.core.ui.SharedAppRoot
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        SharedAppRoot()
    }
}
