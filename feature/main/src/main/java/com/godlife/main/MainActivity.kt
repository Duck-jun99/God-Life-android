package com.godlife.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.godlife.community_page.CommunityPageScreen
import com.godlife.community_page.navigation.CommunityPageRoute
import com.godlife.designsystem.GodLifeTheme
import com.godlife.designsystem.NullColor
import com.godlife.designsystem.Purple40
import com.godlife.designsystem.PurpleMain
import com.godlife.designsystem.PurpleSecond
import com.godlife.main_page.MainPageScreen
import com.godlife.main_page.navigation.MainPageRoute
import com.godlife.setting_page.SettingPageScreen
import com.godlife.setting_page.navigation.SettingPageRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainUiTheme()
        }
    }
}


data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null,
    val route:String
)

@Composable
fun TabView(tabBarItems: List<TabBarItem>, navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    NavigationBar {
        tabBarItems.forEach { tabBarItem ->
            NavigationBarItem(
                selected = currentRoute == tabBarItem.route,
                onClick = {
                    navController.navigate(tabBarItem.route){
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },

                icon = {
                    TabBarIconView(
                        isSelected = currentRoute == tabBarItem.route,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title,
                        badgeAmount = tabBarItem.badgeAmount
                    )
                },
                label = {Text(tabBarItem.title)})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    badgeAmount: Int? = null
) {
    BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
        Icon(
            imageVector = if (isSelected) selectedIcon else unselectedIcon,
            contentDescription = title
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(count.toString())
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainUiTheme(){
    GodLifeTheme {

        val mainTab = TabBarItem(title = "Main", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home, route = MainPageRoute.route)
        val communityTab = TabBarItem(title = "God Life", selectedIcon = Icons.Filled.List, unselectedIcon = Icons.Outlined.List, route = CommunityPageRoute.route)
        val settingTab = TabBarItem(title = "Setting", selectedIcon = Icons.Filled.Settings, unselectedIcon = Icons.Outlined.Settings, route = SettingPageRoute.route)


        val tabBarItems = listOf(mainTab, communityTab, settingTab)


        val navController = rememberNavController()


        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                bottomBar = { TabView(tabBarItems, navController) }
            ) {
                NavHost(navController = navController, startDestination = mainTab.route) {


                    composable(mainTab.route) {
                        MainPageScreen()
                    }

                    composable(communityTab.route) {
                        CommunityPageScreen()
                    }

                    composable(settingTab.route) {
                        SettingPageScreen()
                    }


                }
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MainUiTheme()
}
