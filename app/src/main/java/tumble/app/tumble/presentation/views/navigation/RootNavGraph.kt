package tumble.app.tumble.presentation.views.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import tumble.app.tumble.presentation.views.MainScreen
import tumble.app.tumble.presentation.views.onboarding.OnBoarding

@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "main") {
        composable("onBoarding") {
            OnBoarding()
        }
        composable("main") {
            MainScreen(navController = navController)
        }
    }
}