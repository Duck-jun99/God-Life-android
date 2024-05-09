package com.godlife.setting_page.navigation

import androidx.navigation.NavController

fun NavController.navigateSettingPage() {
    navigate(MainSettingRoute.route)
}


object MainSettingRoute {
    const val route = "Setting-Page"
}