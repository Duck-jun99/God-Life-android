package com.godlife.login

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.godlife.designsystem.component.GodLifeSignUpTextField
import com.godlife.designsystem.component.GodLifeTextField
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.OrangeMain
import kotlinx.coroutines.delay

@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel = hiltViewModel()
){

    val checkedNickname by signUpViewModel.checkedNickname.collectAsState()
    val checkedEmail by signUpViewModel.checkedEmail.collectAsState()
    val checkedAge by signUpViewModel.checkedAge.collectAsState()
    val checkedSex by signUpViewModel.checkedSex.collectAsState()


    val navController = rememberNavController()

    //val navBackStackEntry by navController.currentBackStackEntryAsState()
    //val currentRoute = navBackStackEntry?.destination?.route

    if(checkedNickname && checkedEmail && checkedAge && checkedSex){
        navController.navigate("SignUpConfirmScreen")
    }

    NavHost(navController = navController, startDestination = "SignUpScreen1",
        modifier = Modifier.fillMaxHeight()) {

        composable("SignUpScreen1"){
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(initialAlpha = 0.4f),
                exit = fadeOut()
            ) {
                SignUpScreen1(signUpViewModel)
            }

        }

        composable("SignUpConfirmScreen"){
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(initialAlpha = 0.4f)
            ) {
                SignUpConfirmScreen(signUpViewModel)
            }

        }
    }

}

@Composable
fun SignUpScreen1(
    signUpViewModel: SignUpViewModel
){
    var showText by remember { mutableStateOf(false) }
    var showEditScreen1 by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        // "회원가입을 도와드릴게요." 텍스트가 0.5초 후에 나타나도록 설정
        delay(500)
        showText = true
    }

    LaunchedEffect(showEditScreen1) {
        // 2초 후에 EditNicknameScreen이 그려지도록 설정
        delay(2000)
        showEditScreen1 = true
    }

    GodLifeTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(OrangeMain)
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

                AnimatedVisibility(visible = showEditScreen1,
                    enter = fadeIn(
                        initialAlpha = 0.4f
                    )
                ) {
                    EditScreen1(signUpViewModel)
                }


            }
        }
    }
}

@Composable
fun EditScreen1(
    signUpViewModel:SignUpViewModel
){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OrangeMain)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            var nickname by remember { signUpViewModel.nickname }
            var email by remember { signUpViewModel.email }

            Log.e("nickname", nickname)
            Log.e("email", email)

            val checkedNickname by signUpViewModel.checkedNickname.collectAsState()

            val checkedNicknameMessage by signUpViewModel.checkedNicknameMessage.collectAsState()

            val checkedServerNickname by signUpViewModel.checkedServerNickname.collectAsState()

            val checkedEmail by signUpViewModel.checkedEmail.collectAsState()

            val checkedEmailMessage by signUpViewModel.checkedEmailMessage.collectAsState()

            val checkedServerEmail by signUpViewModel.checkedServerEmail.collectAsState()

            Text(text = "닉네임을 입력해주세요.", style = TextStyle(
                color = Color.White,
                fontSize = 20.sp
            ))

            Spacer(modifier = Modifier.size(10.dp))

            GodLifeSignUpTextField(
                text = nickname,
                onTextChanged = {nickname = it},
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done // 키보드 입력 종료 액션 설정
                ),
                actionCallBack = {

                    // 키보드 입력 종료 시 통신 수행
                    signUpViewModel.checkNicknameLogic(nickname)
                    Log.e("Silkjsakjld", "키보드 입력 종료")
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.size(5.dp))

            Text(text = checkedNicknameMessage,
                style = TextStyle(color = Color.White),
                modifier = Modifier.align(Alignment.End),
                textAlign = TextAlign.End)

            Spacer(modifier = Modifier.size(50.dp))

            Text(text = "이메일을 입력해주세요.", style = TextStyle(
                color = Color.White,
                fontSize = 20.sp
            ))

            Spacer(modifier = Modifier.size(10.dp))

            GodLifeSignUpTextField(
                text = email,
                onTextChanged = {email = it},
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done // 키보드 입력 종료 액션 설정
                ),
                actionCallBack = {

                    // 키보드 입력 종료 시 통신 수행
                    //performCommunication(nickname, email)
                    signUpViewModel.checkEmailLogic(email)
                    Log.e("Silkjsakjld", "키보드 입력 종료")
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.size(5.dp))

            Text(text = checkedEmailMessage,
                style = TextStyle(color = Color.White),
                modifier = Modifier.align(Alignment.End),
                textAlign = TextAlign.End)

            Spacer(modifier = Modifier.size(50.dp))

            if(checkedNickname && checkedEmail) {

                AnimatedVisibility(visible = true,
                    enter = fadeIn(
                        initialAlpha = 0.4f
                    )
                ) {

                    EditScreen2(signUpViewModel)
                }
            }
        }
    }
}

@Composable
fun EditScreen2(
    signUpViewModel:SignUpViewModel
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OrangeMain)
    ) {
        Column(

        ) {

            var age by remember { signUpViewModel.age}
            var sex by remember { signUpViewModel.sex }

            val checkedAge by signUpViewModel.checkedAge.collectAsState()
            val checkedAgeMessage by signUpViewModel.checkedAgeMessage.collectAsState()

            val checkedSex by signUpViewModel.checkedSex.collectAsState()
            val checkedSexMessage by signUpViewModel.checkedSexMessage.collectAsState()


            Text(text = "나이를 입력해주세요.", style = TextStyle(
                color = Color.White,
                fontSize = 20.sp
            ))

            Spacer(modifier = Modifier.size(10.dp))

            GodLifeSignUpTextField(
                text = age,
                onTextChanged = {age = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number).copy(imeAction = ImeAction.Done),
                actionCallBack = {
                    // 키보드 입력 종료 시 통신 수행
                    //performCommunication(nickname, email)
                    signUpViewModel.checkAgeLogic(age)
                    Log.e("Silkjsakjld", "키보드 입력 종료")
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.size(5.dp))

            Text(text = checkedAgeMessage,
                style = TextStyle(color = Color.White),
                modifier = Modifier.align(Alignment.End),
                textAlign = TextAlign.End)

            Spacer(modifier = Modifier.size(50.dp))

            Text(text = "성별을 입력해주세요.", style = TextStyle(
                color = Color.White,
                fontSize = 20.sp
            ))

            Spacer(modifier = Modifier.size(10.dp))

            GodLifeSignUpTextField(
                text = sex,
                onTextChanged = {sex = it},
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done // 키보드 입력 종료 액션 설정
                ),
                actionCallBack = {
                    // 키보드 입력 종료 시 통신 수행
                    //performCommunication(nickname, email)
                    signUpViewModel.checkSexLogic(sex)
                    Log.e("Silkjsakjld", "키보드 입력 종료")
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.size(5.dp))

            Text(text = checkedSexMessage,
                style = TextStyle(color = Color.White),
                modifier = Modifier.align(Alignment.End),
                textAlign = TextAlign.End)

        }
    }
}


@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview(){
    GodLifeTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(OrangeMain)
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
                .background(OrangeMain)
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

                GodLifeTextField(
                    text = nickname,
                    onTextChanged = {nickname = it},
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

                GodLifeTextField(
                    text = email,
                    onTextChanged = {email = it},
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
                .background(OrangeMain)
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

                GodLifeTextField(text = age, onTextChanged = {age = it},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.size(100.dp))

                Text(text = "성별을 입력해주세요.", style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp
                ))

                Spacer(modifier = Modifier.size(10.dp))

                GodLifeTextField(text = sex, onTextChanged = {sex = it})


            }
        }
    }
}
