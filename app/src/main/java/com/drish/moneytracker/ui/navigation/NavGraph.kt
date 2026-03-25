package com.drish.moneytracker.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.drish.moneytracker.ui.auth.AuthViewModel
import com.drish.moneytracker.ui.auth.ForgotPasswordScreen
import com.drish.moneytracker.ui.auth.LoginScreen
import com.drish.moneytracker.ui.auth.SignupScreen
import com.drish.moneytracker.ui.components.BottomNavigation
import com.drish.moneytracker.ui.dashboard.DashboardScreen
import com.drish.moneytracker.ui.groups.CreateGroupScreen
import com.drish.moneytracker.ui.groups.GroupDetailScreen
import com.drish.moneytracker.ui.groups.GroupsScreen
import com.drish.moneytracker.ui.profile.ProfileScreen
import com.drish.moneytracker.ui.profile.ThemeSettingsScreen
import com.drish.moneytracker.ui.transactions.AddTransactionScreen
import com.drish.moneytracker.ui.transactions.PersonDetailScreen
import com.drish.moneytracker.ui.transactions.TransactionsScreen

private val bottomNavRoutes = setOf(
    NavDestinations.DASHBOARD,
    NavDestinations.TRANSACTIONS,
    NavDestinations.GROUPS,
    NavDestinations.PROFILE
)

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in bottomNavRoutes

    val startDestination = if (isLoggedIn) NavDestinations.DASHBOARD else NavDestinations.LOGIN

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigation(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Auth
            composable(NavDestinations.LOGIN) {
                LoginScreen(
                    onNavigateToSignup = { navController.navigate(NavDestinations.SIGNUP) },
                    onNavigateToForgotPassword = { navController.navigate(NavDestinations.FORGOT_PASSWORD) },
                    onLoginSuccess = {
                        navController.navigate(NavDestinations.DASHBOARD) {
                            popUpTo(NavDestinations.LOGIN) { inclusive = true }
                        }
                    }
                )
            }
            composable(NavDestinations.SIGNUP) {
                SignupScreen(
                    onNavigateToLogin = { navController.popBackStack() },
                    onSignupSuccess = {
                        navController.navigate(NavDestinations.DASHBOARD) {
                            popUpTo(NavDestinations.LOGIN) { inclusive = true }
                        }
                    }
                )
            }
            composable(NavDestinations.FORGOT_PASSWORD) {
                ForgotPasswordScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Main screens
            composable(NavDestinations.DASHBOARD) {
                DashboardScreen(
                    onAddTransaction = { navController.navigate(NavDestinations.ADD_TRANSACTION) }
                )
            }
            composable(NavDestinations.TRANSACTIONS) {
                TransactionsScreen(
                    onPersonClick = { personId ->
                        navController.navigate(NavDestinations.personDetail(personId))
                    },
                    onAddTransaction = { navController.navigate(NavDestinations.ADD_TRANSACTION) }
                )
            }
            composable(
                route = NavDestinations.PERSON_DETAIL,
                arguments = listOf(navArgument("personId") { type = NavType.StringType })
            ) {
                PersonDetailScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onEditTransaction = { transactionId ->
                        navController.navigate(NavDestinations.editTransaction(transactionId))
                    }
                )
            }
            composable(NavDestinations.ADD_TRANSACTION) {
                AddTransactionScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(
                route = NavDestinations.EDIT_TRANSACTION,
                arguments = listOf(navArgument("transactionId") { type = NavType.StringType })
            ) {
                AddTransactionScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Groups
            composable(NavDestinations.GROUPS) {
                GroupsScreen(
                    onGroupClick = { groupId ->
                        navController.navigate(NavDestinations.groupDetail(groupId))
                    },
                    onCreateGroup = { navController.navigate(NavDestinations.CREATE_GROUP) }
                )
            }
            composable(NavDestinations.CREATE_GROUP) {
                CreateGroupScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(
                route = NavDestinations.GROUP_DETAIL,
                arguments = listOf(navArgument("groupId") { type = NavType.StringType })
            ) {
                GroupDetailScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Profile
            composable(NavDestinations.PROFILE) {
                ProfileScreen(
                    onNavigateToThemeSettings = { navController.navigate(NavDestinations.THEME_SETTINGS) },
                    onLogout = {
                        navController.navigate(NavDestinations.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable(NavDestinations.THEME_SETTINGS) {
                ThemeSettingsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
