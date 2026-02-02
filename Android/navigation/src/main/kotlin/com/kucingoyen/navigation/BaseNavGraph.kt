package com.kucingoyen.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.kucingoyen.auth.screens.login.LoginScreen
import com.kucingoyen.auth.screens.splash.SplashScreen
import com.kucingoyen.dashboard.DashboardScreen
import com.kucingoyen.dashboard.DashboardViewModel
import com.kucingoyen.dashboard.deposit.RequestBalanceScreen
import com.kucingoyen.dashboard.screen.DetailRequestLoanScreen

@Composable
fun BaseNavGraph(navController : NavHostController){

    NavHost(navController, startDestination = BaseNav.Auth.SplashScreen.name) {

        composable(route = BaseNav.Auth.SplashScreen.name) { navBackStackEntry ->
            SplashScreen(navigateToLogin = {
                navController.navigate(NavModule.AuthModule.name)
            })
        }

        navigation(
            startDestination = BaseNav.Auth.LoginScreen.name,
            route = NavModule.AuthModule.name){

            composable(route = BaseNav.Auth.LoginScreen.name) { navBackStackEntry ->
                LoginScreen(){
                    navController.navigate(NavModule.DashboardModule.name)
                }
            }
        }

        navigation(
            startDestination = BaseNav.Dashboard.DashboardFeature.name,
            route = NavModule.DashboardModule.name
        ) {
            composable( BaseNav.Dashboard.DashboardFeature.name) {
                DashboardFeature(navController)
            }
        }
    }
}

@Composable
fun DashboardFeature(tringNavController: NavHostController) {
    DashboardNavigation(tringNavController)
}


@Composable
fun DashboardNavigation(
    tringNavController: NavHostController,
) {
    val dashboardNavController = rememberNavController()
    val dashboardViewModel: DashboardViewModel = hiltViewModel()

    NavHost(
        dashboardNavController,
        startDestination =  BaseNav.Dashboard.DashboardScreen.name
    ) {
        composable(route = BaseNav.Dashboard.DashboardScreen.name) { navBackStackEntry ->
            DashboardScreen(dashboardViewModel, onClickSend = {
                dashboardNavController.navigate("${BaseNav.Dashboard.RequestBalanceScreen.name}?index=0")
            }, onClickDeposit = {
                dashboardNavController.navigate("${BaseNav.Dashboard.RequestBalanceScreen.name}?index=1")
            }, requestLoan = {
                dashboardNavController.navigate(BaseNav.Dashboard.DetailRequestLoanScreen.name)
            })
        }
        composable(route = BaseNav.Dashboard.DetailRequestLoanScreen.name) { navBackStackEntry ->
            DetailRequestLoanScreen(dashboardViewModel)
        }
        composable(route = BaseNav.Dashboard.RequestBalanceScreen.name + "?index={index}") { navBackStackEntry ->
            val indexTab = navBackStackEntry.arguments?.getString("index") ?: ""
            RequestBalanceScreen(dashboardViewModel, indexTab){
                dashboardNavController.navigateUp()
            }
        }
    }
}
