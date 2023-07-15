package tumble.app.tumble.presentation.views.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import tumble.app.tumble.presentation.views.account.Account
import tumble.app.tumble.presentation.views.bookmarks.Bookmarks
import tumble.app.tumble.presentation.views.home.Home
import tumble.app.tumble.presentation.views.onboarding.OnBoarding
import tumble.app.tumble.presentation.views.search.Search

@Composable
fun TabBarNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = TabBarScreen.Home.route
    ) {
        composable(route = TabBarScreen.Home.route) {
            Home()
        }
        composable(route = TabBarScreen.Search.route) {
            Search()
        }
        composable(route = TabBarScreen.Bookmarks.route) {
            Bookmarks()
        }
        composable(route = TabBarScreen.Account.route) {
            Account()
        }
    }
}