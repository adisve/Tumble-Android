package tumble.app.tumble.presentation.views

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import tumble.app.tumble.presentation.viewmodels.ParentViewModel
import tumble.app.tumble.presentation.views.navigation.TabBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.currentBackStackEntryAsState
import tumble.app.tumble.presentation.views.navigation.RootNavGraph
import tumble.app.tumble.presentation.views.navigation.TabBarNavGraph

@Composable
fun AppParent() {
    val navController = rememberNavController()
    val viewModel: ParentViewModel = viewModel()

    val combinedData by viewModel.combinedData.collectAsState()

    LaunchedEffect(key1 = combinedData) {
        if (!combinedData.userOnBoarded) {
            navController.navigate("onBoarding")
        } else {
            navController.navigate("main")
        }
    }

    RootNavGraph(navController = navController)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController) {
    val navBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackEntry?.destination

    Scaffold(
        bottomBar = {
            TabBar(
                navController = navController,
                currentDestination = currentDestination
            )
        }
    ) {
        TabBarNavGraph(navController = navController)
    }
}
