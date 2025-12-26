package com.pusu.indexed.comics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pusu.indexed.comics.ui.theme.CoooomicsTheme
import com.pusu.indexed.shared.core.ui.SharedAppRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoooomicsTheme {
                SharedAppRoot(title = "Coooomics")
            }
        }
    }
}
