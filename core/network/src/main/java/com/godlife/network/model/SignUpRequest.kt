package com.godlife.network.model

data class SignUpRequest(
    val nickname: String,
    val email: String,
    val age: Int,
    val sex: String,
    val providerId: String,
    val providerName: String
)