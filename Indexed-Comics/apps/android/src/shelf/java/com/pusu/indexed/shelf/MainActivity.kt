package com.pusu.indexed.shelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pusu.indexed.shared.core.ui.SharedAppRoot
import com.pusu.indexed.shelf.ui.theme.ShelfTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShelfTheme {
                SharedAppRoot(title = "Shelf")
            }
        }
    }
}
