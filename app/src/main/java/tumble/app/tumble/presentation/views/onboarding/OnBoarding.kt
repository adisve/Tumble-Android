package tumble.app.tumble.presentation.views.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tumble.app.tumble.ui.theme.Primary

@Composable
fun OnBoarding() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Welcome to on-boarding!")
    }
}