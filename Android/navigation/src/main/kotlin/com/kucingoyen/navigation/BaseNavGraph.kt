package com.kucingoyen.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kucingoyen.auth.screens.login.LoginScreen
import com.kucingoyen.auth.screens.splash.SplashScreen
import com.kucingoyen.dashboard.DashboardScreen
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
            startDestination = BaseNav.Dashboard.DashboardScreen.name,
            route = NavModule.DashboardModule.name){

            composable(route = BaseNav.Dashboard.DashboardScreen.name) { navBackStackEntry ->
                DashboardScreen{
                    navController.navigate(BaseNav.Dashboard.DetailRequestLoanScreen.name)
                }
            }
            composable(route = BaseNav.Dashboard.DetailRequestLoanScreen.name) { navBackStackEntry ->
                DetailRequestLoanScreen()
            }
        }
    }
}