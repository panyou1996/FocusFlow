package com.panyou.focusflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.panyou.focusflow.ui.home.HomeScreen
import com.panyou.focusflow.ui.home.HomeViewModel
import com.panyou.focusflow.ui.settings.SettingsScreen
import com.panyou.focusflow.ui.taskdetail.TaskDetailSheet
import com.panyou.focusflow.ui.taskdetail.TaskDetailViewModel
import com.panyou.focusflow.ui.theme.FocusFlowTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Tasks : Screen(
        route = "tasks",
        title = "Tasks",
        selectedIcon = Icons.Filled.CheckCircle,
        unselectedIcon = Icons.Outlined.CheckCircleOutline
    )
    
    data object Settings : Screen(
        route = "settings",
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels()
    private val detailViewModel: TaskDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        android.util.Log.d("FocusFlow", "MainActivity onCreate started")
        enableEdgeToEdge()
        setContent {
            FocusFlowTheme {
                FocusFlowApp(
                    homeViewModel = homeViewModel,
                    detailViewModel = detailViewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusFlowApp(
    homeViewModel: HomeViewModel,
    detailViewModel: TaskDetailViewModel
) {
    val navController = rememberNavController()
    val screens = listOf(Screen.Tasks, Screen.Settings)
    
    var selectedTaskId by remember { mutableStateOf<String?>(null) }

    if (selectedTaskId != null) {
        TaskDetailSheet(
            taskId = selectedTaskId!!,
            viewModel = detailViewModel,
            onDismiss = { selectedTaskId = null }
        )
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                tonalElevation = 0.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                screens.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Tasks.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Tasks.route) {
                HomeScreen(
                    viewModel = homeViewModel,
                    detailViewModel = detailViewModel,
                    onTaskClick = { taskId -> selectedTaskId = taskId }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
