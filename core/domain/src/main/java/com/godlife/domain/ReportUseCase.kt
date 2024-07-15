package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import java.time.LocalDateTime
import javax.inject.Inject

class ReportUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeReport(authorization: String,
                              reporterNickname: String,
                              reporterId: Long,
                              receivedNickname: String,
                              receivedId: Long,
                              reason: String,
                              reportContent: String,
                              reportId: Long,
                              reportTime: LocalDateTime,
                              reportType: String
    ) = networkRepository.report(authorization, reporterNickname, reporterId, receivedNickname, receivedId, reason, reportContent, reportId, reportTime, reportType)

}