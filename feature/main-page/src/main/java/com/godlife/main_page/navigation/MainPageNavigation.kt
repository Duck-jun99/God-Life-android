package com.godlife.main_page.navigation

import androidx.navigation.NavController

fun NavController.navigateMainPage() {
    navigate(MainPageRoute.route)
}


object MainPageRoute {
    const val route = "Main-Page"
}