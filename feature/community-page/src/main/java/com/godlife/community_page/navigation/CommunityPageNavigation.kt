package com.godlife.community_page.navigation

import androidx.navigation.NavController

fun NavController.navigateCommunityPage() {
    navigate(CommunityPageRoute.route)
}


object CommunityPageRoute {
    const val route = "Community-Page"
}