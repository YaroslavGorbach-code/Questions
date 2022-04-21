package yaroslavgorbach.questions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import yaroslavgorbach.questions.feature.questions.ui.Questions
import yaroslavgorbach.questions.feature.recordings.ui.Recordings

sealed class Screen(val route: String) {
    object Questions : Screen("Questions")
}

private sealed class LeafScreen(
    private val route: String,
) {
    fun createRoute(root: Screen) = "${root.route}/$route"

    object Questions : LeafScreen("Questions")
    object Recordings : LeafScreen("Recordings")
}

@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Questions.route,
        modifier = modifier,
    ) {
        addQuestionsTopLevel(navController)
    }
}

private fun NavGraphBuilder.addQuestionsTopLevel(
    navController: NavController,
) {
    navigation(
        route = Screen.Questions.route,
        startDestination = LeafScreen.Questions.createRoute(Screen.Questions),
    ) {
        addQuestions(navController, Screen.Questions)
        addRecordings(navController, Screen.Questions)
    }
}


private fun NavGraphBuilder.addQuestions(
    navController: NavController,
    root: Screen,
) {
    composable(LeafScreen.Questions.createRoute(root)) {
        Questions(navigateToRecords = {
            navController.navigate(LeafScreen.Recordings.createRoute(root))
        })
    }
}

private fun NavGraphBuilder.addRecordings(
    navController: NavController,
    root: Screen,
) {
    composable(
        LeafScreen.Recordings.createRoute(root)
    ) {
        Recordings {
            navController.popBackStack()
        }
    }

}
