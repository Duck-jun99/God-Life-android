package com.godlife.model.todo

data class TodoTimeData(
    val y:Int,
    val m:Int,
    val d:Int
)

data class NotificationTimeData(
    val y:Int,
    val m:Int,
    val d:Int,

    val hour:Int,
    val minute:Int
)
