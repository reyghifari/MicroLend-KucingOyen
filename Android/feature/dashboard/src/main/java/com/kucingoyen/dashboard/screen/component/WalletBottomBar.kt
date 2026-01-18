package com.kucingoyen.dashboard.screen.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kucingoyen.core.theme.BaseColor
import com.kucingoyen.dashboard.TextGray
import com.kucingoyen.dashboard.screen.DashboardViewModel

@Composable
fun WalletBottomBar(
    dashboardViewModel: DashboardViewModel
) {

    val selectedBar by dashboardViewModel.bottomBarSelected.collectAsStateWithLifecycle()

    NavigationBar(
        containerColor = BaseColor.JetBlack.Normal,
        contentColor = TextGray
    ) {
        NavigationBarItem(
            selected = selectedBar == 0,
            onClick = {dashboardViewModel.updateBottomBarSelected(0)},
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                indicatorColor = Color.Transparent,
                unselectedIconColor = TextGray,
                unselectedTextColor = TextGray
            )
        )
        NavigationBarItem(
            selected = selectedBar == 1,
            onClick = {dashboardViewModel.updateBottomBarSelected(1)},
            icon = { Icon(Icons.Default.GridView, contentDescription = "My Loan") },
            label = { Text("My Loan") },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = TextGray, unselectedTextColor = TextGray,      selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                indicatorColor = Color.Transparent,)
        )
        NavigationBarItem(
            selected = selectedBar == 2,
            onClick = {dashboardViewModel.updateBottomBarSelected(2)},
            icon = { Icon(Icons.Default.Apps, contentDescription = "Apps") },
            label = { Text("Profile") },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = TextGray, unselectedTextColor = TextGray,      selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                indicatorColor = Color.Transparent,)
        )
    }
}