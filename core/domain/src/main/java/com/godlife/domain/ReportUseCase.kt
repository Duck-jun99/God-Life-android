package com.godlife.domain

import com.godlife.data.repository.NetworkRepository
import javax.inject.Inject

class ReportUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun executeReport(authorization: String,
                              reporterNickname: String,
                              reporterId: Int,
                              receivedNickname: String,
                              receivedId: Int,
                              reason: String,
                              reportContent: String,
                              reportId: Int,
                              reportTime: String,
                              reportType: String
    ) = networkRepository.report(authorization, reporterNickname, reporterId, receivedNickname, receivedId, reason, reportContent, reportId, reportTime, reportType)

}