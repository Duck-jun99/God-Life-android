package com.godlife.community_page.navigation

import androidx.navigation.NavController

fun NavController.navigateCommunityPage() {
    navigate(CommunityPageRoute.route)
}


object CommunityPageRoute {
    const val route = "Community-Page"
}

object FamousPostRoute{
    const val route = "FamousPostScreen"
}

object LatestPostRoute{
    const val route = "LatestPostScreen"
}

object StimulusPostRoute{
    const val route = "StimulusPostScreen"
}

object RankingRoute{
    const val route = "RankingScreen"
}

object PostDetailRoute{
    const val route = "PostDetailScreen"

}

object StimulusPostDetailRoute{
    const val route = "StimulusDetailScreen"
}

object SearchResultRoute{
    const val route = "SearchResultScreen"

}