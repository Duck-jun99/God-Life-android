package com.godlife.setting_page.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ScoreDetailViewModel @Inject constructor(

): ViewModel(){

    private val _minScore = MutableStateFlow(0)
    val minScore: StateFlow<Int> = _minScore

    private val _maxScore = MutableStateFlow(0)
    val maxScore: StateFlow<Int> = _maxScore

    private val _requiredScore = MutableStateFlow(0)
    val requiredScore: StateFlow<Int> = _requiredScore

    private val _graphTargetValue = MutableStateFlow(0f)
    val graphTargetValue: StateFlow<Float> = _graphTargetValue

    private val _tier = MutableStateFlow("")
    val tier: StateFlow<String> = _tier




    suspend fun setUserTier(
        score: Int
    ){
        when(score){

            in 0..29 -> {
                _tier.value = "새싹"
                _minScore.value = 0
                _maxScore.value = 29
                _requiredScore.value = 30 - score
            }

            in 30..99 -> {
                _tier.value = "브론즈"
                _minScore.value = 30
                _maxScore.value = 99
                _requiredScore.value = 100 - score
            }

            in 100..199 -> {
                _tier.value = "실버"
                _minScore.value = 100
                _maxScore.value = 199
                _requiredScore.value = 200 - score
            }

            in 200..499 -> {
                _tier.value = "골드"
                _minScore.value = 200
                _maxScore.value = 499
                _requiredScore.value = 500 - score
            }

            in 500..699 -> {
                _tier.value = "다이아"
                _minScore.value = 500
                _maxScore.value = 699
                _requiredScore.value = 700 - score
            }

            in 700..999 -> {
                _tier.value = "마스터"
                _minScore.value = 700
                _maxScore.value = 999
                _requiredScore.value = 1000 - score
            }

            in 1000..Int.MAX_VALUE -> {
                _tier.value = "레전드"
                _minScore.value = 1000
                _maxScore.value = 1000
                _requiredScore.value = 0
            }

        }

        val range = maxScore.value - minScore.value
        val scoreInRange = score - minScore.value
        _graphTargetValue.value = (scoreInRange.toFloat() / range.toFloat()) * 650f
    }
}