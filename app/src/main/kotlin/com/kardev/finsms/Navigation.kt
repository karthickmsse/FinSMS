package com.kardev.finsms

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kardev.finsms.feature.analytics.AnalyticsScreen
import com.kardev.finsms.feature.analytics.HomeScreen
import com.kardev.finsms.feature.transactions.ReviewQueueScreen
import com.kardev.finsms.feature.transactions.TransactionsScreen

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object Transactions : Screen("transactions", "Transactions", Icons.Default.List)
    data object Analytics : Screen("analytics", "Analytics", Icons.Default.PieChart)
    data object Review : Screen("review", "Review", Icons.Default.Warning)
}

private val bottomNavItems = listOf(Screen.Home, Screen.Transactions, Screen.Analytics, Screen.Review)

@Composable
fun FinSmsNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Transactions.route) { TransactionsScreen() }
        composable(Screen.Analytics.route) { AnalyticsScreen() }
        composable(Screen.Review.route) { ReviewQueueScreen() }
    }
}

@Composable
fun FinSmsBottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        bottomNavItems.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = { navController.navigate(screen.route) { launchSingleTop = true } },
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { androidx.compose.material3.Text(screen.label) }
            )
        }
    }
}
