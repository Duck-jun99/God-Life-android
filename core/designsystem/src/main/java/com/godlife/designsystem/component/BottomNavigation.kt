package com.godlife.designsystem.component

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.PurpleMain
import com.godlife.model.navigationbar.BottomNavItem



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabIconView(
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
@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {

    val mainTab = BottomNavItem(title = "Main", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home, route = "MainPageRoute.route")
    val communityTab = BottomNavItem(title = "God Life", selectedIcon = Icons.AutoMirrored.Filled.List, unselectedIcon = Icons.AutoMirrored.Outlined.List, route = "CommunityPageRoute.route")
    val settingTab = BottomNavItem(title = "Setting", selectedIcon = Icons.Filled.Settings, unselectedIcon = Icons.Outlined.Settings, route = "SettingPageRoute.route")


    val tabBarItems = listOf(mainTab, communityTab, settingTab)

    NavigationBar(
        containerColor = Color.White,
        contentColor = contentColorFor(backgroundColor = Color.White),
        tonalElevation = 7.dp

    ) {
        tabBarItems.forEach { tabBarItem ->
            NavigationBarItem(
                selected = true,
                onClick = { /* */ },
                icon = {
                    TabIconView(
                        isSelected = true,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title,
                        badgeAmount = tabBarItem.badgeAmount
                    )
                },
                label = { Text(tabBarItem.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PurpleMain,
                    selectedTextColor = PurpleMain,
                    unselectedIconColor = GrayWhite,
                    unselectedTextColor = GrayWhite,
                    disabledIconColor = GrayWhite3,
                    disabledTextColor = GrayWhite3,
                    indicatorColor = Color.White
                ),
            )
        }
    }
}
