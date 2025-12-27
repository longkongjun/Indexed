package com.pusu.indexed.androidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.pusu.indexed.shared.feature.discover.DiscoverScreen

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val scope = rememberCoroutineScope()
                val viewModel = remember {
                    App.instance.dependencyContainer.createDiscoverViewModel(scope)
                }
                
                DiscoverScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { animeId ->
                        // TODO: 导航到详情页
                        println("Navigate to anime detail: $animeId")
                    }
                )
            }
        }
    }
}
