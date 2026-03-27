package com.example.sankargroupassignment.screen.NAVIGATION

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sankargroupassignment.appViewModel
import com.example.sankargroupassignment.screen.CallHistoryScreen
import com.example.sankargroupassignment.screen.CallScreen
import com.example.sankargroupassignment.screen.ContactScreen

// Define bottom nav items
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object History : BottomNavItem(Routes.History.route, "History", Icons.AutoMirrored.Filled.List)
    object Call : BottomNavItem(Routes.CallScreen.route, "Call", Icons.Default.Call)
    object Contacts : BottomNavItem(Routes.Contacts.route, "Contacts", Icons.Default.Person)
}

@Composable
fun NavApp(viewModel: appViewModel) {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem.History,
        BottomNavItem.Call,
        BottomNavItem.Contacts
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->

                    val isSelected = currentDestination?.hierarchy?.any { 
                        it.route == item.route
                    } == true

                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item.route) {

                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.CallScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.History.route) {
                CallHistoryScreen(viewModel = viewModel)
            }
            composable(Routes.CallScreen.route) {
                CallScreen(veiwModel = viewModel)
            }
            composable(Routes.Contacts.route) {
                ContactScreen(viewModel = viewModel)
            }
        }
    }
}
