package com.godlife.login.fake

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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.godlife.designsystem.component.GodLifeSignUpTextField
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.OrangeMain
import kotlinx.coroutines.delay

@Composable
fun TestUserSignUpScreen(
    testSignUpViewModel: TestSignUpViewModel = hiltViewModel()
){

    val checkedNickname by testSignUpViewModel.checkedNickname.collectAsState()
    val checkedEmail by testSignUpViewModel.checkedEmail.collectAsState()
    val checkedAge by testSignUpViewModel.checkedAge.collectAsState()
    val checkedSex by testSignUpViewModel.checkedSex.collectAsState()


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
                FakeSignUpScreen1(testSignUpViewModel)
            }

        }

        composable("SignUpConfirmScreen"){
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(initialAlpha = 0.4f)
            ) {
                TestSignUpConfirmScreen(testSignUpViewModel)
            }

        }
    }

}

@Composable
fun FakeSignUpScreen1(
    testSignUpViewModel: TestSignUpViewModel
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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        item{
                            TestEditScreen1(testSignUpViewModel)
                        }
                    }

                }


            }
        }
    }
}


@Composable
fun TestEditScreen1(
    testSignUpViewModel: TestSignUpViewModel
){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OrangeMain)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            var id by remember { testSignUpViewModel.id }
            var nickname by remember { testSignUpViewModel.nickname }
            var email by remember { testSignUpViewModel.email }


            val checkedNickname by testSignUpViewModel.checkedNickname.collectAsState()

            val checkedNicknameMessage by testSignUpViewModel.checkedNicknameMessage.collectAsState()

            val checkedServerNickname by testSignUpViewModel.checkedServerNickname.collectAsState()

            val checkedEmail by testSignUpViewModel.checkedEmail.collectAsState()

            val checkedEmailMessage by testSignUpViewModel.checkedEmailMessage.collectAsState()

            val checkedServerEmail by testSignUpViewModel.checkedServerEmail.collectAsState()

            Text(text = "임의 ID를 입력, 중복체크 로직 없어서 서로 상의해야 됩니다.", style = TextStyle(
                color = Color.White,
                fontSize = 14.sp
            ))

            Spacer(modifier = Modifier.size(10.dp))

            GodLifeSignUpTextField(
                text = id,
                onTextChanged = {id = it},
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done // 키보드 입력 종료 액션 설정
                ),
                hint = "대략 10자리 수로 만들어보겠어요. ex) 9999999999",
                actionCallBack = {

                },
                singleLine = true
            )

            Spacer(modifier = Modifier.size(50.dp))

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
                    testSignUpViewModel.checkNicknameLogic(nickname)
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
                    testSignUpViewModel.checkEmailLogic(email)
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

                    TestEditScreen2(testSignUpViewModel)
                }
            }
        }
    }
}

@Composable
fun TestEditScreen2(
    testSignUpViewModel: TestSignUpViewModel
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OrangeMain)
    ) {
        Column(

        ) {

            var age by remember { testSignUpViewModel.age}
            var sex by remember { testSignUpViewModel.sex }

            val checkedAge by testSignUpViewModel.checkedAge.collectAsState()
            val checkedAgeMessage by testSignUpViewModel.checkedAgeMessage.collectAsState()

            val checkedSex by testSignUpViewModel.checkedSex.collectAsState()
            val checkedSexMessage by testSignUpViewModel.checkedSexMessage.collectAsState()


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
                    testSignUpViewModel.checkAgeLogic(age)
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
                    testSignUpViewModel.checkSexLogic(sex)
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
