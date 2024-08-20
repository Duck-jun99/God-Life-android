package com.godlife.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animate
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
import com.godlife.community_page.navigation.StimulusPostDetailRoute
import com.godlife.community_page.post_detail.PostDetailScreen
import com.godlife.community_page.post_detail.StimulusDetailScreen
import com.godlife.community_page.post_detail.post_update.stimulus.UpdateStimulusPostScreen
import com.godlife.community_page.post_detail.post_update.stimulus.UpdateStimulusPostScreenRoute
import com.godlife.community_page.report.ReportScreen
import com.godlife.community_page.report.navigation.ReportScreenRoute
import com.godlife.designsystem.component.TabIconView
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.main_page.MainPageScreen
import com.godlife.main_page.history.HistoryDetailScreen
import com.godlife.main_page.history.HistoryPageScreen
import com.godlife.main_page.navigation.HistoryDetailRoute
import com.godlife.main_page.navigation.HistoryPageRoute
import com.godlife.main_page.navigation.MainPageRoute
import com.godlife.main_page.navigation.NotificationListRoute
import com.godlife.main_page.notification.NotificationListScreen
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var createNavigator: CreatetodolistNavigator

    @Inject
    lateinit var loginNavigator: LoginNavigator

    @Inject
    lateinit var createPostNavigator: CreatePostNavigator

    //TEST
    @Inject
    lateinit var localPreferences: SharedPreferences

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

        //TEST
        Log.e("MainActivity", "refreshToken: ${localPreferences.getString("READ_ME_REFRESH_TOKEN", "")}")

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
            MainUiTheme(
                this,
                createNavigator,
                loginNavigator,
                createPostNavigator,
                intent
            )
        }

    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        setContent {
            MainUiTheme(
                this,
                createNavigator,
                loginNavigator,
                createPostNavigator,
                intent
            )
        }
    }
}

private fun handleNotificationIntent(
    intent: Intent?,
    navController: NavController
) {
    intent?.let {

        if (it.getStringExtra("navigation") == "normal"
            || it.getStringExtra("navigation") == "comment") {
            val postId = it.getStringExtra("postId")
            if (postId != null) {
                //navController.navigate("${PostDetailRoute.route}/$postId")
                // 기존 백 스택을 클리어하고 메인 화면을 시작점으로 설정
                navController.navigate(MainPageRoute.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }

                // 그 다음 PostDetail 화면으로 이동
                navController.navigate("${PostDetailRoute.route}/$postId")
            }
        }
        else if (it.getStringExtra("navigation") == "stimulus") {
            val postId = it.getStringExtra("postId")
            if (postId != null) {
                //navController.navigate("${PostDetailRoute.route}/$postId")
                // 기존 백 스택을 클리어하고 메인 화면을 시작점으로 설정
                navController.navigate(MainPageRoute.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }

                // 그 다음 PostDetail 화면으로 이동
                navController.navigate("${StimulusPostDetailRoute.route}/$postId")
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainUiTheme(
    mainActivity: MainActivity,
    createNavigator: CreatetodolistNavigator,
    loginNavigator: LoginNavigator,
    createPostNavigator: CreatePostNavigator,
    intent: Intent?
){
    GodLifeTheme {

        val snackBarHostState = remember { SnackbarHostState() }
        SnackbarHost(hostState = snackBarHostState)


        val mainTab = BottomNavItem(title = "Main", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home, route = MainPageRoute.route)
        val communityTab = BottomNavItem(title = "Community", selectedIcon = Icons.AutoMirrored.Filled.List, unselectedIcon = Icons.AutoMirrored.Outlined.List, route = CommunityPageRoute.route)
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

                    //굿생 기록 저장소 화면
                    composable(HistoryPageRoute.route){
                        HistoryPageScreen(navController = navController)
                        currentRoute.value = HistoryPageRoute.route
                        bottomBarVisibleState.value = false
                    }

                    //굿생 기록 상세 화면
                    composable("${HistoryDetailRoute.route}/{id}", arguments =
                    listOf(navArgument("id"){type = NavType.IntType})
                    ){
                        val id = it.arguments?.getInt("id")
                        if(id != null){
                            HistoryDetailScreen(
                                id = id,
                                navController = navController
                            )
                            currentRoute.value = HistoryDetailRoute.route
                            bottomBarVisibleState.value = false
                        }

                    }

                    //알림 리스트 화면
                    composable(NotificationListRoute.route){
                        NotificationListScreen(
                            navController = navController
                        )
                        currentRoute.value = NotificationListRoute.route
                        bottomBarVisibleState.value = false
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

                    //굿생 자극 상세 화면
                    composable("${StimulusPostDetailRoute.route}/{postId}", arguments = listOf(navArgument("postId"){type = NavType.StringType})){
                        val postId = it.arguments?.getString("postId")
                        if (postId != null) {
                            StimulusDetailScreen(
                                postId = postId,
                                navController = navController
                            )
                            bottomBarVisibleState.value = false
                        }

                    }

                    // 굿생 자극 수정 화면
                    composable("${UpdateStimulusPostScreenRoute.route}/{postId}", arguments = listOf(navArgument("postId"){type = NavType.StringType})){
                        val postId = it.arguments?.getString("postId")
                        if (postId != null) {
                            UpdateStimulusPostScreen(
                                postId = postId,
                                parentNavController = navController
                            )
                            bottomBarVisibleState.value = false
                        }
                    }

                    //신고화면
                    composable("${ReportScreenRoute.route}/{postId}/{userNickname}/{userId}/{category}/{title}",
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

                LaunchedEffect(Unit) {
                    handleNotificationIntent(
                        intent = intent,
                        navController = navController
                    )
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
                modifier = Modifier.drawWithContent {
                    drawContent()
                    if (currentRoute == bottomNavItem.route) {
                        drawLine(
                            color = PurpleMain,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 5.dp.toPx()
                        )
                    }
                },
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
                        //badgeAmount = bottomNavItem.badgeAmount
                    )
                },
                label = {
                    if(currentRoute == bottomNavItem.route){
                        Text(bottomNavItem.title, style = TextStyle(color = PurpleMain, fontWeight = FontWeight.Bold)) }
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

@Preview
@Composable
fun MyBottomNavigationPreview() {
    val mainTab = BottomNavItem(title = "Main", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home, route = MainPageRoute.route)
    val communityTab = BottomNavItem(title = "Community", selectedIcon = Icons.AutoMirrored.Filled.List, unselectedIcon = Icons.AutoMirrored.Outlined.List, route = CommunityPageRoute.route)
    val settingTab = BottomNavItem(title = "Setting", selectedIcon = Icons.Filled.Settings, unselectedIcon = Icons.Outlined.Settings, route = SettingPageRoute.route)

    val bottomNavItems = listOf(mainTab, communityTab, settingTab)

    var currentRoute by remember { mutableStateOf("Main") }
    var indicatorOffset by remember { mutableStateOf(0f) }

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val cScope = rememberCoroutineScope()

    // 그라데이션 컬러 리스트
    val gradientColors = listOf(
        Color(0xFFFF44A2),
        Color(0xFFFF5890),
        Color(0xFFFA6B80),
        Color(0xFFFF7B75),
        Color(0xFFFF8161),
        Color(0xFFFF884D)
    )


    NavigationBar(
        containerColor = Color.White,
        contentColor = contentColorFor(backgroundColor = Color.White),
        modifier = Modifier.shadow(7.dp)
    ) {
        val itemWidth = configuration.screenWidthDp / bottomNavItems.size

        Box(modifier = Modifier.fillMaxWidth()) {
            // 애니메이션이 적용된 인디케이터
            Box(
                Modifier
                    .offset(x = with(density) { indicatorOffset.toDp() })
                    .width(itemWidth.dp)
                    .height(3.dp)
                    .background(PurpleMain)
            )

            Row {
                bottomNavItems.forEachIndexed { index, bottomNavItem ->
                    NavigationBarItem(
                        selected = currentRoute == bottomNavItem.route,
                        onClick = {
                            currentRoute = bottomNavItem.route
                            // 애니메이션 적용
                            cScope.launch {
                                animate(
                                    initialValue = indicatorOffset,
                                    targetValue = index.toFloat() * (itemWidth.toFloat())
                                ) { value, _ ->
                                    indicatorOffset = value
                                }
                            }
                        },
                        icon = {
                            TabIconView(
                                isSelected = currentRoute == bottomNavItem.route,
                                selectedIcon = bottomNavItem.selectedIcon,
                                unselectedIcon = bottomNavItem.unselectedIcon,
                                title = bottomNavItem.title,
                            )
                        },
                        label = {
                            if(currentRoute == bottomNavItem.route){
                                Text(bottomNavItem.title, style = TextStyle(color = PurpleMain, fontWeight = FontWeight.Bold))
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Unspecified,
                            selectedTextColor = PurpleMain,
                            unselectedIconColor = GrayWhite,
                            unselectedTextColor = GrayWhite,
                            indicatorColor = Color.Transparent // 기본 인디케이터를 숨김.
                        ),
                    )
                }
            }
        }
    }
}