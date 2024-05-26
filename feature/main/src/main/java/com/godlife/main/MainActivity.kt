package com.godlife.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.godlife.community_page.CommunityPageScreen
import com.godlife.community_page.navigation.CommunityPageRoute
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.main_page.MainPageScreen
import com.godlife.main_page.navigation.MainPageRoute
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
fun MainUiTheme(
    mainActivity: MainActivity,
    createNavigator: CreatetodolistNavigator,
    loginNavigator: LoginNavigator,
    createPostNavigator: CreatePostNavigator
){
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
                        MainPageScreen(mainActivity, createNavigator, createPostNavigator)
                    }

                    composable(communityTab.route) {
                        CommunityPageScreen()
                    }

                    composable(settingTab.route) {
                        SettingPageScreen(mainActivity, loginNavigator)
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
    //MainUiTheme()
}
