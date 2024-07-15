package com.godlife.network.model

import retrofit2.http.Body
import java.time.LocalDateTime

data class ReportRequest(
    val reporterNickname: String,
    val reporterId: Long,
    val receivedNickname: String,
    val receivedId: Long,
    val reason: String,
    val reportContent: String,
    val reportId: Long,
    val reportTime: LocalDateTime,
    val reportType: String
)