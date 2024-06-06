package com.godlife.community_page.post_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godlife.domain.GetPostDetailUseCase
import com.godlife.domain.LocalPreferenceUserUseCase
import com.godlife.network.model.PostDetailBody
import com.godlife.network.model.PostDetailQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val localPreferenceUserUseCase: LocalPreferenceUserUseCase
): ViewModel() {

    private val _postDetail = MutableStateFlow<PostDetailQuery>(PostDetailQuery("", null, ""))
    val postDetail: StateFlow<PostDetailQuery> = _postDetail

    fun getPostDetail(postId: String) {

        var auth = ""

        viewModelScope.launch {
            launch { auth = "Bearer ${localPreferenceUserUseCase.getAccessToken()}" }.join()

            _postDetail.value = getPostDetailUseCase.executeGetPostDetail(auth, postId)
        }
    }

}