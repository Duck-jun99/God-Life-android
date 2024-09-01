package com.godlife.setting_page.navigation

import androidx.navigation.NavController

fun NavController.navigateSettingPage() {
    navigate(SettingPageRoute.route)
}


object SettingPageRoute {
    const val route = "Setting-Page"
}

object ScoreDetailPageRoute {
    const val route = "Score-Detail-Page"
}