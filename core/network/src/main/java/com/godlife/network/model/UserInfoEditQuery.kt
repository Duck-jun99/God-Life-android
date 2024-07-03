package com.godlife.network.model

data class ImageUploadQuery(
    val status: String,
    val body: ImageUploadBody,
    val message: String
)

data class ImageUploadStimulusQuery(
    val status: String,
    val body: String,
    val message: String
)

data class ImageUploadBody(
    val originalName: String,
    val serverName: String
)

data class UpdateIntroduceQuery(
    val status: String,
    val body: Boolean,
    val message: String
)
