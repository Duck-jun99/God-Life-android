package com.godlife.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface

import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.godlife.community_page.CommunityPageScreen
import com.godlife.community_page.navigation.CommunityPageRoute
import com.godlife.designsystem.component.TabIconView
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.main_page.MainPageScreen
import com.godlife.main_page.navigation.MainPageRoute
import com.godlife.model.navigationbar.BottomNavItem
import com.godlife.navigator.CreatePostNavigator
import com.godlife.navigator.CreatetodolistNavigator
import com.godlife.navigator.LoginNavigator
import com.godlife.setting_page.SettingPageScreen
import com.godlife.setting_page.navigation.SettingPageRoute
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var createNavigator: CreatetodolistNavigator

    @Inject
    lateinit var loginNavigator: LoginNavigator

    @Inject
    lateinit var createPostNavigator: CreatePostNavigator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainUiTheme(this, createNavigator, loginNavigator, createPostNavigator)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainUiTheme(
    mainActivity: MainActivity,
    createNavigator: CreatetodolistNavigator,
    loginNavigator: LoginNavigator,
    createPostNavigator: CreatePostNavigator
){
    GodLifeTheme {

        val mainTab = BottomNavItem(title = "Main", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home, route = MainPageRoute.route)
        val communityTab = BottomNavItem(title = "God Life", selectedIcon = Icons.AutoMirrored.Filled.List, unselectedIcon = Icons.AutoMirrored.Outlined.List, route = CommunityPageRoute.route)
        val settingTab = BottomNavItem(title = "Setting", selectedIcon = Icons.Filled.Settings, unselectedIcon = Icons.Outlined.Settings, route = SettingPageRoute.route)


        val tabBarItems = listOf(mainTab, communityTab, settingTab)


        val navController = rememberNavController()


        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            Scaffold(
                bottomBar = { MyBottomNavigation(tabBarItems, navController) },

            ) { innerPadding ->
                NavHost(navController = navController, startDestination = mainTab.route,modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {

                    val bottomPaddingValue = innerPadding.calculateBottomPadding().value.dp

                    //bottomBar
                    composable(mainTab.route) {
                        MainPageScreen(mainActivity, createNavigator, createPostNavigator)
                    }

                    composable(communityTab.route) {
                        CommunityPageScreen(paddingValue = bottomPaddingValue)
                    }

                    composable(settingTab.route) {
                        SettingPageScreen(mainActivity, loginNavigator)
                    }


                    /*
                    //Community 세부 기능
                    composable(LatestPostScreenRoute.route) {
                        LatestPostScreen()
                    }

                     */


                }
            }
        }
    }
}

@Composable
fun MyBottomNavigation(bottomNavItems: List<BottomNavItem>, navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    NavigationBar(
        containerColor = Color.White,
        contentColor = contentColorFor(backgroundColor = Color.White),
        modifier = Modifier.shadow(7.dp)
    ) {
        bottomNavItems.forEach { bottomNavItem ->
            NavigationBarItem(
                selected = currentRoute == bottomNavItem.route,
                onClick = {
                    navController.navigate(bottomNavItem.route){
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },

                icon = {
                    TabIconView(
                        isSelected = currentRoute == bottomNavItem.route,
                        selectedIcon = bottomNavItem.selectedIcon,
                        unselectedIcon = bottomNavItem.unselectedIcon,
                        title = bottomNavItem.title,
                        badgeAmount = bottomNavItem.badgeAmount
                    )
                },
                label = {
                    if(currentRoute == bottomNavItem.route){
                        Text(bottomNavItem.title) }
                    else null
                        },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PurpleMain,
                    selectedTextColor = PurpleMain,
                    unselectedIconColor = GrayWhite,
                    unselectedTextColor = GrayWhite,
                    disabledIconColor = GrayWhite3,
                    disabledTextColor = GrayWhite3,
                    indicatorColor = Color.White),
                )
        }
    }
}
