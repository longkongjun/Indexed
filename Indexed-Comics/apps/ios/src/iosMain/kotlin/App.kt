import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeUIViewController
import com.pusu.indexed.shared.core.ui.SharedAppRoot

@Composable
fun IOSApp() {
    SharedAppRoot(title = "Indexed iOS")
}

fun MainViewController() = ComposeUIViewController { IOSApp() }
