package com.godlife.main

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface

import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.godlife.community_page.CommunityPageScreen
import com.godlife.community_page.navigation.CommunityPageRoute
import com.godlife.community_page.navigation.PostDetailRoute
import com.godlife.community_page.post_detail.PostDetailScreen
import com.godlife.community_page.report.ReportScreen
import com.godlife.community_page.report.navigation.ReportScreenRoute
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
import com.godlife.profile.ProfileEditScreen
import com.godlife.profile.ProfileScreen
import com.godlife.profile.navigation.ProfileEditScreenRoute
import com.godlife.profile.navigation.ProfileScreenRoute
import com.godlife.service.MyFirebaseMessagingService
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

        // 현재 기기에 설정된 쓰기 권한을 가져오기 위한 변수
        var writePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE )

        // 현재 기기에 설정된 읽기 권한을 가져오기 위한 변수
        var readPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)

        // 읽기 권한과 쓰기 권한에 대해서 설정이 되어있지 않다면
        if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
            // 읽기, 쓰기 권한을 요청.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                1
            )
        }


        /** FCM Token값 가져오기 */
        MyFirebaseMessagingService().getFirebaseToken{
            Log.e("MainActivity", "FCM Token: $it")
        }

        /*
        /** PostNotification 대응 */
        checkAppPushNotification()

        //사용안하면 삭제하기
        /** DynamicLink 수신확인 */
        initDynamicLink()

         */

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

        val snackBarHostState = remember { SnackbarHostState() }
        SnackbarHost(hostState = snackBarHostState)


        val mainTab = BottomNavItem(title = "Main", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home, route = MainPageRoute.route)
        val communityTab = BottomNavItem(title = "God Life", selectedIcon = Icons.AutoMirrored.Filled.List, unselectedIcon = Icons.AutoMirrored.Outlined.List, route = CommunityPageRoute.route)
        val settingTab = BottomNavItem(title = "Setting", selectedIcon = Icons.Filled.Settings, unselectedIcon = Icons.Outlined.Settings, route = SettingPageRoute.route)


        val tabBarItems = listOf(mainTab, communityTab, settingTab)


        val navController = rememberNavController()

        val currentRoute = remember { mutableStateOf(MainPageRoute.route)}

        val bottomBarVisibleState = remember { mutableStateOf(true) }


        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            Scaffold(
                bottomBar = {
                    if(bottomBarVisibleState.value) { MyBottomNavigation(tabBarItems, navController) }
                            },
                snackbarHost = { SnackbarHost(snackBarHostState)}
                ) { innerPadding ->
                NavHost(navController = navController, startDestination = mainTab.route,modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {

                    //val bottomPaddingValue = innerPadding.calculateBottomPadding().value.dp

                    //bottomBar
                    composable(mainTab.route) {
                        MainPageScreen(
                            mainActivity = mainActivity,
                            createNavigator = createNavigator,
                            createPostNavigator = createPostNavigator,
                            loginNavigator = loginNavigator,
                            navController = navController)

                        currentRoute.value = mainTab.route
                        bottomBarVisibleState.value = true
                    }

                    composable(communityTab.route) {
                        CommunityPageScreen(
                            parentNavController = navController,
                            bottomBarVisibleState = bottomBarVisibleState
                        )
                        currentRoute.value = communityTab.route
                        bottomBarVisibleState.value = true
                    }

                    composable(settingTab.route) {
                        SettingPageScreen(mainActivity = mainActivity, loginNavigator = loginNavigator, navController = navController)
                        currentRoute.value = settingTab.route
                        bottomBarVisibleState.value = true
                    }

                    //프로필 화면
                    composable("${ProfileScreenRoute.route}/{userId}", arguments =
                    listOf(navArgument("userId"){type = NavType.StringType})
                    ){
                        val userId = it.arguments?.getString("userId")
                        if(userId != null){
                            ProfileScreen(navController = navController, userId = userId)
                            currentRoute.value = ProfileScreenRoute.route
                            bottomBarVisibleState.value = false
                        }

                    }

                    //프로필 수정 화면
                    composable(ProfileEditScreenRoute.route){
                        ProfileEditScreen(
                            navController = navController,
                            snackbarHostState = snackBarHostState
                        )
                        currentRoute.value = ProfileEditScreenRoute.route
                        bottomBarVisibleState.value = false
                    }

                    //게시물 상세 화면
                    composable("${PostDetailRoute.route}/{postId}", arguments = listOf(navArgument("postId"){type = NavType.StringType})){
                        val postId = it.arguments?.getString("postId")
                        if (postId != null) {
                            PostDetailScreen(
                                postId = postId,
                                parentNavController = navController,
                                navController = navController
                            )
                            bottomBarVisibleState.value = false
                        }
                    }

                    //신고화면
                    composable("${ReportScreenRoute.route}/{postId}/{userNickname}/{userId}/{category}/ {title}",
                        arguments = listOf(
                            navArgument("postId"){type = NavType.StringType},
                            navArgument("userNickname"){type = NavType.StringType},
                            navArgument("userId"){type = NavType.StringType},
                            navArgument("category"){type = NavType.StringType},
                            navArgument("title"){type = NavType.StringType},
                        )
                    ){
                        val postId = it.arguments?.getString("postId")
                        val userNickname = it.arguments?.getString("userNickname")
                        val userId = it.arguments?.getString("userId")
                        val category = it.arguments?.getString("category")
                        val title = it.arguments?.getString("title")
                        if (postId != null && userNickname != null && userId != null && category != null && title != null) {
                            ReportScreen(
                                postId = postId,
                                userNickname = userNickname,
                                userId = userId,
                                category = category,
                                title = title,
                                navController = navController
                            )
                        }
                        bottomBarVisibleState.value = false
                    }


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


