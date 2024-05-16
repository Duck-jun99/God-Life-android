package com.godlife.login

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.PurpleMain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel = hiltViewModel()
){

    var showText by remember { mutableStateOf(false) }
    var showSignUpScreen1 by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // "회원가입을 도와드릴게요." 텍스트가 0.5초 후에 나타나도록 설정
        delay(500)
        showText = true
    }

    LaunchedEffect(showSignUpScreen1) {
        // 2초 후에 SignUpScreen1이 그려지도록 설정
        delay(2000)
        showSignUpScreen1 = true
    }

    GodLifeTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleMain)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                AnimatedVisibility(visible = showText) {
                    Text(
                        text = "회원가입을 도와드릴게요.",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.size(50.dp))

                AnimatedVisibility(visible = showSignUpScreen1,
                    enter = fadeIn(
                        // Overwrites the initial value of alpha to 0.4f for fade in, 0 by default
                        initialAlpha = 0.4f
                    )
                ) {
                    SignUpScreen1(signUpViewModel)
                }

            }
        }
    }
}

@Composable
fun SignUpScreen1(
    signUpViewModel:SignUpViewModel
){
    GodLifeTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleMain)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                var nickname by remember { mutableStateOf("") }
                var email by remember { mutableStateOf("") }

                //var checkedNickname by remember { mutableStateOf(false) }
                //var checkedEmail by remember { mutableStateOf(false) }

                val checkedNickname by signUpViewModel.checkedNickname.collectAsState()
                val checkedEmail by signUpViewModel.checkedEmail.collectAsState()

                Text(text = "닉네임을 입력해주세요.", style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp
                ))

                Spacer(modifier = Modifier.size(10.dp))

                TextField(
                    value = nickname,
                    onValueChange = {nickname = it},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done // 키보드 입력 종료 액션 설정
                    ),
                    keyboardActions = KeyboardActions(onDone = {

                        // 키보드 입력 종료 시 통신 수행
                        checkNickname(nickname, signUpViewModel)
                        Log.e("Silkjsakjld", "키보드 입력 종료")
                    })
                )

                if(checkedNickname){

                    Spacer(modifier = Modifier.size(5.dp))

                    Text(text = "사용 가능한 닉네임이에요.",
                        style = TextStyle(color = Color.White),
                        modifier = Modifier.align(Alignment.End),
                        textAlign = TextAlign.End)
                }

                else{
                    Spacer(modifier = Modifier.size(5.dp))

                    Text(text = "다른 닉네임을 사용해주세요.",
                        style = TextStyle(color = Color.White),
                        modifier = Modifier.align(Alignment.End),
                        textAlign = TextAlign.End)
                }

                Spacer(modifier = Modifier.size(100.dp))

                Text(text = "이메일을 입력해주세요.", style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp
                ))

                Spacer(modifier = Modifier.size(10.dp))

                TextField(
                    value = email,
                    onValueChange = {email = it},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done // 키보드 입력 종료 액션 설정
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        // 키보드 입력 종료 시 통신 수행
                        //performCommunication(nickname, email)
                        checkEmail(email, signUpViewModel)
                        Log.e("Silkjsakjld", "키보드 입력 종료")
                    })
                )

                if(checkedEmail){
                    Spacer(modifier = Modifier.size(5.dp))

                    Text(text = "사용 가능한 이메일이에요.",
                        style = TextStyle(color = Color.White),
                        modifier = Modifier.align(Alignment.End),
                        textAlign = TextAlign.End)
                }

                else{
                    Spacer(modifier = Modifier.size(5.dp))

                    Text(text = "다른 이메일을 사용해주세요.",
                        style = TextStyle(color = Color.White),
                        modifier = Modifier.align(Alignment.End),
                        textAlign = TextAlign.End)
                }

            }
        }
    }
}


@Composable
fun SignUpScreen2(){
    GodLifeTheme {
        Box(
            modifier = Modifier
                .background(PurpleMain)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                var age by remember { mutableStateOf("") }
                var sex by remember { mutableStateOf("") }


                Text(text = "나이를 입력해주세요.", style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp
                ))

                Spacer(modifier = Modifier.size(10.dp))

                TextField(value = age, onValueChange = {age = it})

                Spacer(modifier = Modifier.size(100.dp))

                Text(text = "성별을 입력해주세요.", style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp
                ))

                Spacer(modifier = Modifier.size(10.dp))

                TextField(value = sex, onValueChange = {sex = it})


            }
        }
    }
}

private fun checkNickname(nickname :String, signUpViewModel: SignUpViewModel){
    CoroutineScope(Dispatchers.IO).launch {
        signUpViewModel.checkNickname(nickname)
    }
}

private fun checkEmail(email :String, signUpViewModel: SignUpViewModel){
    CoroutineScope(Dispatchers.IO).launch {
        signUpViewModel.checkEmail(email)
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview(){
    GodLifeTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleMain)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                var nickname by remember { mutableStateOf("") }
                var email by remember { mutableStateOf("") }
                var age by remember { mutableStateOf("") }
                var sex by remember { mutableStateOf("") }

                Text(text = "회원가입을 도와드릴게요.", style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp
                ))

                Spacer(modifier = Modifier.size(50.dp))

                SignUpScreen1Preview()

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreen1Preview(){
    GodLifeTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleMain)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                var nickname by remember { mutableStateOf("") }
                var email by remember { mutableStateOf("") }

                Text(text = "닉네임을 입력해주세요.", style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp
                ))

                Spacer(modifier = Modifier.size(10.dp))

                TextField(
                    value = nickname,
                    onValueChange = {nickname = it},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done // 키보드 입력 종료 액션 설정
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        // 키보드 입력 종료 시 통신 수행
                        //performCommunication(nickname, email)
                        Log.e("Silkjsakjld", "키보드 입력 종료")
                    })
                )
                Spacer(modifier = Modifier.size(5.dp))

                Text(text = "사용 가능한 닉네임이에요.",
                    style = TextStyle(color = Color.White),
                    modifier = Modifier.align(Alignment.End),
                    textAlign = TextAlign.End)

                Spacer(modifier = Modifier.size(100.dp))

                Text(text = "이메일을 입력해주세요.", style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp
                ))

                Spacer(modifier = Modifier.size(10.dp))

                TextField(
                    value = email,
                    onValueChange = {email = it},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done // 키보드 입력 종료 액션 설정
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        // 키보드 입력 종료 시 통신 수행
                        //performCommunication(nickname, email)
                        Log.e("Silkjsakjld", "키보드 입력 종료")
                    })
                )

                Spacer(modifier = Modifier.size(5.dp))

                Text(text = "사용 가능한 이메일이에요.",
                    style = TextStyle(color = Color.White),
                    modifier = Modifier.align(Alignment.End),
                    textAlign = TextAlign.End)


            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun SignUpScreen2Preview(){
    GodLifeTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleMain)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                var age by remember { mutableStateOf("") }
                var sex by remember { mutableStateOf("") }


                Text(text = "나이를 입력해주세요.", style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp
                ))

                Spacer(modifier = Modifier.size(10.dp))

                TextField(value = age, onValueChange = {age = it})

                Spacer(modifier = Modifier.size(100.dp))

                Text(text = "성별을 입력해주세요.", style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp
                ))

                Spacer(modifier = Modifier.size(10.dp))

                TextField(value = sex, onValueChange = {sex = it})


            }
        }
    }
}
