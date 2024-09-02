package com.godlife.login

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.godlife.designsystem.component.GodLifeButton
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.component.GodLifeSignUpTextField
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.OrangeLight
import com.godlife.designsystem.theme.OrangeMain
import com.godlife.designsystem.view.GodLifeErrorScreen
import com.godlife.designsystem.view.GodLifeLoadingScreen
import com.godlife.navigator.MainNavigator
import kotlinx.coroutines.delay

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    mainNavigator: MainNavigator,
    loginActivity: LoginActivity,
    signUpViewModel: SignUpViewModel = hiltViewModel()
){
    var showText by remember { mutableStateOf(false) }
    var showEditScreen by remember { mutableStateOf(false) }

    val navController = rememberNavController()

    val uiState by signUpViewModel.uiState.collectAsState()

    if(uiState is SignUpUiState.Init
        || uiState is SignUpUiState.Loading){

        LaunchedEffect(Unit) {
            // "회원가입을 도와드릴게요." 텍스트가 0.5초 후에 나타나도록 설정
            delay(500)
            showText = true
        }

        LaunchedEffect(showEditScreen) {
            // 2초 후에 EditNicknameScreen이 그려지도록 설정
            delay(2000)
            showEditScreen = true
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(10.dp)
        ) {

            AnimatedVisibility(visible = showText) {
                Text(
                    text = "새로운 굿생의 시작을 위해\n회원가입을 도와드릴게요.",
                    style = TextStyle(
                        color = GrayWhite,
                        fontSize = 20.sp
                    )
                )
            }

            Spacer(modifier.height(20.dp))

            AnimatedVisibility(
                visible = showEditScreen,
                enter = fadeIn(
                    initialAlpha = 0.7f
                )
            ) {

                NavHost(navController = navController, startDestination = "CheckNicknamePage"){

                    composable(
                        route = "CheckNicknamePage",
                        enterTransition = {
                            fadeIn(
                                initialAlpha = 0.7f
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(durationMillis = 300)
                            )
                        }
                    ){

                        SignUpCheckNicknameContent(
                            navController = navController,
                            viewModel = signUpViewModel
                        )

                    }

                    composable(
                        route = "CheckEmailPage",
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(durationMillis = 300)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(durationMillis = 300)
                            )
                        }
                    ){

                        SignUpCheckEmailContent(
                            navController = navController,
                            viewModel = signUpViewModel
                        )

                    }

                    composable(
                        route = "CheckAgePage",
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(durationMillis = 300)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(durationMillis = 300)
                            )
                        }
                    ){
                        SignUpCheckAgeContent(
                            navController = navController,
                            viewModel = signUpViewModel
                        )
                    }

                    composable(
                        route = "CheckGenderPage",
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(durationMillis = 300)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(durationMillis = 300)
                            )
                        }
                    ){
                        SignUpCheckGenderContent(
                            navController = navController,
                            viewModel = signUpViewModel
                        )
                    }

                    composable(
                        route = "ConfirmPage",
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(durationMillis = 300)
                            )
                        }
                    ){
                        SignUpConfirmContent(
                            navController = navController,
                            viewModel = signUpViewModel
                        )
                    }

                }

            }


        }

    }

    else if(uiState is SignUpUiState.Success){
        SignUpSuccessContent(
            mainNavigator = mainNavigator,
            loginActivity = loginActivity,
            viewModel = signUpViewModel
        )
    }

    else if(uiState is SignUpUiState.Error){
        GodLifeErrorScreen(
            errorMessage = (uiState as SignUpUiState.Error).message,
            buttonEnabled = false
        )
    }

    if(uiState is SignUpUiState.Loading){
        GodLifeLoadingScreen(
            text = "회원가입을 진행중이에요."
        )
    }



}

@Composable
fun SignUpCheckNicknameContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SignUpViewModel
){
    val nickname by viewModel.nickname.collectAsState()

    val checkedNickname by viewModel.checkedNickname.collectAsState()

    val checkedNicknameMessage by viewModel.checkedNicknameMessage.collectAsState()

    Column {

        Column(
            modifier = modifier
                .padding(20.dp)
                .background(color = OrangeLight, shape = RoundedCornerShape(8.dp))
                .padding(10.dp)
        ) {
            Text(
                text = "굿생러님의 닉네임을 만들어주세요.",
                style = TextStyle(
                    color = OrangeMain,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier.height(5.dp))

            Text(
                text = "닉네임은 수정이 불가하니 신중히 설정해주세요.",
                style = TextStyle(
                    color = OrangeMain,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier.height(20.dp))

            GodLifeSignUpTextField(
                hint = "10자까지 가능해요.",
                text = nickname,
                onTextChanged = { viewModel.setNickname(it) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done // 키보드 입력 종료 액션 설정
                ),
                actionCallBack = {
                    viewModel.checkNicknameLogic(nickname)
                },
                keyboardActions = KeyboardActions(onDone = {
                    // 키보드 입력 종료 시 통신 수행
                    //viewModel.checkNicknameLogic(nickname)
                }),
                singleLine = true
            )

            Spacer(modifier.height(5.dp))

            Text(
                text = checkedNicknameMessage,
                style = TextStyle(
                    color = GrayWhite
                ),
                modifier = modifier.align(Alignment.End),
                textAlign = TextAlign.End
            )

            Spacer(modifier.height(20.dp))

            GodLifeButtonWhite(
                modifier = modifier
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    viewModel.checkNicknameLogic(nickname)
                },
                text = {
                    Text(
                        text = "중복 체크",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )


        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            contentAlignment = Alignment.BottomCenter
        ){

            GodLifeButton(
                enabled = checkedNickname,
                onClick = {
                    navController.navigate("CheckEmailPage")
                },
                text = {
                    Text(
                        text = "다음",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )


        }


    }


}

@Composable
fun SignUpCheckEmailContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SignUpViewModel
){

    val email by viewModel.email.collectAsState()

    val checkedEmail by viewModel.checkedEmail.collectAsState()

    val checkedEmailMessage by viewModel.checkedEmailMessage.collectAsState()


    Column {
        Column(
            modifier = modifier
                .padding(20.dp)
                .background(color = OrangeLight, shape = RoundedCornerShape(8.dp))
                .padding(10.dp)
        ) {
            Text(
                text = "굿생러님의 이메일을 입력해주세요.",
                style = TextStyle(
                    color = OrangeMain,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier.height(5.dp))

            Text(
                text = "예: goodLife@goodLife.com",
                style = TextStyle(
                    color = OrangeMain,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier.height(20.dp))

            GodLifeSignUpTextField(
                hint = "올바른 이메일 형식을 입력해주세요.",
                text = email,
                onTextChanged = { viewModel.setEmail(it) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done // 키보드 입력 종료 액션 설정
                ),
                actionCallBack = {
                    viewModel.checkEmailLogic(email)
                },
                keyboardActions = KeyboardActions(onDone = {
                    // 키보드 입력 종료 시 통신 수행
                    //performCommunication(nickname, email)
                    //Log.e("Silkjsakjld", "키보드 입력 종료")
                })
            )

            Spacer(modifier.height(5.dp))

            Text(
                text = checkedEmailMessage,
                style = TextStyle(
                    color = GrayWhite
                ),
                modifier = modifier.align(Alignment.End),
                textAlign = TextAlign.End
            )

            Spacer(modifier.height(20.dp))

            GodLifeButtonWhite(
                modifier = modifier
                    .align(Alignment.CenterHorizontally),
                onClick = { viewModel.checkEmailLogic(email) },
                text = {
                    Text(
                        text = "중복 체크",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )


        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            contentAlignment = Alignment.BottomCenter
        ){

            GodLifeButton(
                enabled = checkedEmail,
                onClick = {
                    navController.navigate("CheckAgePage")
                },
                text = {
                    Text(
                        text = "다음",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )

        }

    }

}

@Composable
fun SignUpCheckAgeContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SignUpViewModel
){

    val age by viewModel.age.collectAsState()

    val checkedAge by viewModel.checkedAge.collectAsState()

    val checkedAgeMessage by viewModel.checkedAgeMessage.collectAsState()


    Column {
        Column(
            modifier = modifier
                .padding(20.dp)
                .background(color = OrangeLight, shape = RoundedCornerShape(8.dp))
                .padding(10.dp)
        ) {
            Text(
                text = "굿생러님의 나이를 알려주세요.",
                style = TextStyle(
                    color = OrangeMain,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier.height(20.dp))

            GodLifeSignUpTextField(
                hint = "나이를 입력해주세요.",
                text = age,
                onTextChanged = { viewModel.setAge(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number).copy(imeAction = ImeAction.Done) // 키보드 입력 종료 액션 설정
                ,
                actionCallBack = {
                    viewModel.checkAgeLogic(age)
                },
                keyboardActions = KeyboardActions(onDone = {
                    // 키보드 입력 종료 시 통신 수행
                    //performCommunication(nickname, email)
                    //Log.e("Silkjsakjld", "키보드 입력 종료")
                })
            )

            Spacer(modifier.height(5.dp))

            Text(
                text = checkedAgeMessage,
                style = TextStyle(
                    color = GrayWhite
                ),
                modifier = modifier.align(Alignment.End),
                textAlign = TextAlign.End
            )

            Spacer(modifier.height(20.dp))

            GodLifeButtonWhite(
                modifier = modifier
                    .align(Alignment.CenterHorizontally),
                onClick = { viewModel.checkAgeLogic(age) },
                text = {
                    Text(
                        text = "입력 완료",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )

        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            contentAlignment = Alignment.BottomCenter
        ){

            GodLifeButton(
                enabled = checkedAge,
                onClick = {
                    navController.navigate("CheckGenderPage")
                },
                text = {
                    Text(
                        text = "다음",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )

        }

    }



}

@Composable
fun SignUpCheckGenderContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SignUpViewModel
){

    val gender by viewModel.sex.collectAsState()

    val checkedGender by viewModel.checkedSex.collectAsState()

    //val checkedGenderMessage by viewModel.checkedSexMessage.collectAsState()

    Column {

        Column(
            modifier = modifier
                .padding(20.dp)
                .background(color = OrangeLight, shape = RoundedCornerShape(8.dp))
                .padding(10.dp)
        ) {
            Text(
                text = "마지막 단계에요.\n굿생러님의 성별은 어떻게 되시나요?",
                style = TextStyle(
                    color = OrangeMain,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier.height(20.dp))

            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){

                Checkbox(
                    checked = gender == "남",
                    onCheckedChange = {
                        viewModel.setSex("남")
                        viewModel.checkSexLogic("남")
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = OrangeMain,
                        uncheckedColor = GrayWhite
                    )
                )

                Text(
                    text = "남",
                    style = TextStyle(
                        color = if(gender == "남") OrangeMain else GrayWhite,
                        fontWeight = if(gender == "남") FontWeight.Bold else FontWeight.Normal
                    )
                )

                Checkbox(
                    checked = gender == "여",
                    onCheckedChange = {
                        viewModel.setSex("여")
                        viewModel.checkSexLogic("여")
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = OrangeMain,
                        uncheckedColor = GrayWhite
                    )
                )

                Text(
                    text = "여",
                    style = TextStyle(
                        color = if(gender == "여") OrangeMain else GrayWhite,
                        fontWeight = if(gender == "여") FontWeight.Bold else FontWeight.Normal
                    )
                )

                Spacer(modifier.width(15.dp))

            }

        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            contentAlignment = Alignment.BottomCenter
        ){

            GodLifeButton(
                enabled = checkedGender,
                onClick = {
                    navController.navigate("ConfirmPage")
                },
                text = {
                    Text(
                        text = "다음",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )

        }

    }



}

@Composable
fun SignUpConfirmContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SignUpViewModel
){

    Column {
        Column(
            modifier = modifier
                .padding(20.dp)
                .background(color = OrangeLight, shape = RoundedCornerShape(8.dp))
                .padding(10.dp)
        ) {

            Text(
                text = "입력하신 정보가 맞는지 확인해주세요.",
                style = TextStyle(
                    color = OrangeMain,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(5.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Text(
                        text = "닉네임: ",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = viewModel.nickname.collectAsState().value,
                        style = TextStyle(
                            color = GrayWhite,
                            fontSize = 18.sp
                        )
                    )

                }

                Spacer(modifier = modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Text(
                        text = "이메일: ",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = viewModel.email.collectAsState().value,
                        style = TextStyle(
                            color = GrayWhite,
                            fontSize = 18.sp
                        )
                    )

                }

                Spacer(modifier = modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Text(
                        text = "나이: ",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = "${viewModel.age.collectAsState().value}세",
                        style = TextStyle(
                            color = GrayWhite,
                            fontSize = 18.sp
                        )
                    )

                }

                Spacer(modifier = modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Text(
                        text = "성별 : ",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = viewModel.sex.collectAsState().value,
                        style = TextStyle(
                            color = GrayWhite,
                            fontSize = 18.sp
                        )
                    )

                }

            }

            //Spacer(modifier = modifier.height(10.dp))

        }

        HorizontalDivider()

        Spacer(modifier = modifier.height(20.dp))

        Text(
            text = "굿생러가 될 준비가 되셨다면,\n굿생 시작 버튼을 눌러주세요!",
            style = TextStyle(
                color = GrayWhite,
                fontSize = 18.sp
            )
        )

        //Spacer(modifier = modifier.height(20.dp))

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            contentAlignment = Alignment.BottomCenter,
        ){

            GodLifeButtonWhite(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = ""
                    )
                },
                onClick = {
                          viewModel.signUp()
                          },
                text = {
                    Text(
                        text = "굿생 시작!",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )

        }

    }

}

@Composable
fun SignUpSuccessContent(
    modifier: Modifier = Modifier,
    mainNavigator: MainNavigator,
    loginActivity: LoginActivity,
    viewModel: SignUpViewModel,
){
    var showLogo by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }
    var showProgressBar by remember { mutableStateOf(false) }
    var navigateFlag by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500L)
        showLogo = true

        delay(1000L)
        showText = true

        delay(1000L)
        showProgressBar = true
        
        delay(3000L)
        navigateFlag = true
    }

    if(navigateFlag){
        mainNavigator.navigateFrom(
            activity = loginActivity,
            withFinish = true
        )
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AnimatedVisibility(
            visible = showLogo,
            enter = fadeIn(
                initialAlpha = 0.2f
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.goodlife_inside_logo),
                contentDescription = "",
                modifier = modifier
                    .size(300.dp)
            )
        }


        AnimatedVisibility(
            visible = showText,
            enter = fadeIn(
                initialAlpha = 0.2f
            )
        ) {
            Text(
                text = "${viewModel.nickname.collectAsState().value}님\n환영해요!",
                style = TextStyle(
                    color = GrayWhite,
                    fontSize = 20.sp
                ),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier.height(100.dp))

        AnimatedVisibility(
            visible = showProgressBar,
            enter = fadeIn(
                initialAlpha = 0.2f
            )
        ) {

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                CircularProgressIndicator(
                    color = OrangeMain,
                )

                Spacer(modifier.width(10.dp))

                Text(
                    text = "잠시후 자동으로 이동합니다.",
                    style = TextStyle(
                        color = OrangeMain,
                        fontSize = 14.sp
                    ),
                    textAlign = TextAlign.Center
                )

            }

        }


    }
}


@Preview
@Composable
fun SignUpPagePreview(
    modifier: Modifier = Modifier
){
    var showText by remember { mutableStateOf(false) }
    var showEditScreen1 by remember { mutableStateOf(false) }

    val navController = rememberNavController()


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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(10.dp)
    ) {

        AnimatedVisibility(visible = showText) {
            Text(
                text = "새로운 굿생의 시작을 위해\n회원가입을 도와드릴게요.",
                style = TextStyle(
                    color = GrayWhite,
                    fontSize = 20.sp
                )
            )
        }

        Spacer(modifier.height(20.dp))

        AnimatedVisibility(
            visible = showEditScreen1,
            enter = fadeIn(
                initialAlpha = 0.7f
            )
        ) {

            NavHost(navController = navController, startDestination = "CheckNicknamePage"){

                composable(
                    route = "CheckNicknamePage",
                    enterTransition = {
                        fadeIn(
                            initialAlpha = 0.7f
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(durationMillis = 300)
                        )
                    }
                ){

                    SignUpCheckNicknamePreview(
                        navController = navController
                    )

                }

                composable(
                    route = "CheckEmailPage",
                    enterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(durationMillis = 300)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(durationMillis = 300)
                        )
                    }
                ){

                    SignUpCheckEmailPreview(
                        navController = navController
                    )

                }

                composable(
                    route = "CheckAgePage",
                    enterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(durationMillis = 300)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(durationMillis = 300)
                        )
                    }
                ){
                    SignUpCheckAgePreview(
                        navController = navController
                    )
                }

                composable(
                    route = "CheckGenderPage",
                    enterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(durationMillis = 300)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(durationMillis = 300)
                        )
                    }
                ){
                    SignUpCheckGenderPreview(
                        navController = navController
                    )
                }

                composable(
                    route = "ConfirmPage",
                    enterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(durationMillis = 300)
                        )
                    }
                ){
                    SignUpConfirmPreview(
                        navController = navController
                    )
                }

            }

        }


    }
}

@Preview
@Composable
fun SignUpCheckNicknamePreview(
    modifier: Modifier = Modifier,
    navController: NavController? = null
){
    var nickname by remember { mutableStateOf("") }

    Column {

        Column(
            modifier = modifier
                .padding(20.dp)
                .background(color = OrangeLight, shape = RoundedCornerShape(8.dp))
                .padding(10.dp)
        ) {
            Text(
                text = "굿생러님의 닉네임을 만들어주세요.",
                style = TextStyle(
                    color = OrangeMain,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier.height(5.dp))

            Text(
                text = "닉네임은 수정이 불가하니 신중히 설정해주세요.",
                style = TextStyle(
                    color = OrangeMain,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier.height(20.dp))

            GodLifeSignUpTextField(
                hint = "10자까지 가능해요.",
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

            Spacer(modifier.height(5.dp))

            Text(
                text = "사용 가능한 닉네임이에요.",
                style = TextStyle(
                    color = GrayWhite
                ),
                modifier = modifier.align(Alignment.End),
                textAlign = TextAlign.End
            )

            Spacer(modifier.height(20.dp))

            GodLifeButtonWhite(
                modifier = modifier
                    .align(Alignment.CenterHorizontally),
                onClick = { /*TODO*/ },
                text = {
                    Text(
                        text = "중복 체크",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )


        }

        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ){

            GodLifeButton(
                onClick = {
                    navController?.navigate("CheckEmailPage")
                },
                text = {
                    Text(
                        text = "다음",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )


        }


    }


}

@Preview
@Composable
fun SignUpCheckEmailPreview(
    modifier: Modifier = Modifier,
    navController: NavController? = null
){

    var email by remember { mutableStateOf("") }

    Column {
        Column(
            modifier = modifier
                .padding(20.dp)
                .background(color = OrangeLight, shape = RoundedCornerShape(8.dp))
                .padding(10.dp)
        ) {
            Text(
                text = "굿생러님의 이메일을 입력해주세요.",
                style = TextStyle(
                    color = OrangeMain,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier.height(5.dp))

            Text(
                text = "예: goodLife@goodLife.com",
                style = TextStyle(
                    color = OrangeMain,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier.height(20.dp))

            GodLifeSignUpTextField(
                hint = "올바른 이메일 형식을 입력해주세요.",
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

            Spacer(modifier.height(5.dp))

            Text(
                text = "사용 가능한 이메일이에요.",
                style = TextStyle(
                    color = GrayWhite
                ),
                modifier = modifier.align(Alignment.End),
                textAlign = TextAlign.End
            )

            Spacer(modifier.height(20.dp))

            GodLifeButtonWhite(
                modifier = modifier
                    .align(Alignment.CenterHorizontally),
                onClick = { /*TODO*/ },
                text = {
                    Text(
                        text = "중복 체크",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )


        }

        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ){

            GodLifeButton(
                onClick = {
                    navController?.navigate("CheckAgePage")
                },
                text = {
                    Text(
                        text = "다음",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )

        }

    }



}

@Preview
@Composable
fun SignUpCheckAgePreview(
    modifier: Modifier = Modifier,
    navController: NavController? = null
){

    var age by remember { mutableStateOf("") }

    Column {
        Column(
            modifier = modifier
                .padding(20.dp)
                .background(color = OrangeLight, shape = RoundedCornerShape(8.dp))
                .padding(10.dp)
        ) {
            Text(
                text = "굿생러님의 나이를 알려주세요.",
                style = TextStyle(
                    color = OrangeMain,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier.height(20.dp))

            GodLifeSignUpTextField(
                hint = "나이를 입력해주세요.",
                text = age,
                onTextChanged = {age = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number).copy(imeAction = ImeAction.Done) // 키보드 입력 종료 액션 설정
                ,
                keyboardActions = KeyboardActions(onDone = {
                    // 키보드 입력 종료 시 통신 수행
                    //performCommunication(nickname, email)
                    Log.e("Silkjsakjld", "키보드 입력 종료")
                })
            )

            Spacer(modifier.height(5.dp))

            Text(
                text = "입력이 확인되었어요.",
                style = TextStyle(
                    color = GrayWhite
                ),
                modifier = modifier.align(Alignment.End),
                textAlign = TextAlign.End
            )

            Spacer(modifier.height(20.dp))

            GodLifeButtonWhite(
                modifier = modifier
                    .align(Alignment.CenterHorizontally),
                onClick = { /*TODO*/ },
                text = {
                    Text(
                        text = "입력 완료",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )

        }

        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ){

            GodLifeButton(
                onClick = {
                    navController?.navigate("CheckGenderPage")
                },
                text = {
                    Text(
                        text = "다음",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )

        }

    }



}

@Preview
@Composable
fun SignUpCheckGenderPreview(
    modifier: Modifier = Modifier,
    navController: NavController? = null
){

    var gender by remember { mutableStateOf("") }

    Column {

        Column(
            modifier = modifier
                .padding(20.dp)
                .background(color = OrangeLight, shape = RoundedCornerShape(8.dp))
                .padding(10.dp)
        ) {
            Text(
                text = "마지막 단계에요.\n굿생러님의 성별은 어떻게 되시나요?",
                style = TextStyle(
                    color = OrangeMain,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier.height(20.dp))

            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){

                Checkbox(
                    checked = gender == "남",
                    onCheckedChange = { gender = "남"},
                    colors = CheckboxDefaults.colors(
                        checkedColor = OrangeMain,
                        uncheckedColor = GrayWhite
                    )
                )

                Text(
                    text = "남",
                    style = TextStyle(
                        color = if(gender == "남") OrangeMain else GrayWhite,
                        fontWeight = if(gender == "남") FontWeight.Bold else FontWeight.Normal
                    )
                )

                Checkbox(
                    checked = gender == "여",
                    onCheckedChange = { gender = "여" },
                    colors = CheckboxDefaults.colors(
                        checkedColor = OrangeMain,
                        uncheckedColor = GrayWhite
                    )
                )

                Text(
                    text = "여",
                    style = TextStyle(
                        color = if(gender == "여") OrangeMain else GrayWhite,
                        fontWeight = if(gender == "여") FontWeight.Bold else FontWeight.Normal
                    )
                )

                Spacer(modifier.width(15.dp))

            }

        }

        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ){

            GodLifeButton(
                onClick = {
                    navController?.navigate("ConfirmPage")
                },
                text = {
                    Text(
                        text = "다음",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )

        }

    }



}

@Preview
@Composable
fun SignUpConfirmPreview(
    modifier: Modifier = Modifier,
    navController: NavController? = null
){

    Column {
        Column(
            modifier = modifier
                .padding(20.dp)
                .background(color = OrangeLight, shape = RoundedCornerShape(8.dp))
                .padding(10.dp)
        ) {

            Text(
                text = "입력하신 정보가 맞는지 확인해주세요.",
                style = TextStyle(
                    color = OrangeMain,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(5.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Text(
                        text = "닉네임: ",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = "${"HERE"}",
                        style = TextStyle(
                            color = GrayWhite,
                            fontSize = 18.sp
                        )
                    )

                }

                Spacer(modifier = modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Text(
                        text = "이메일: ",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = "${"HERE"}",
                        style = TextStyle(
                            color = GrayWhite,
                            fontSize = 18.sp
                        )
                    )

                }

                Spacer(modifier = modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Text(
                        text = "나이: ",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = "${"HERE"}세",
                        style = TextStyle(
                            color = GrayWhite,
                            fontSize = 18.sp
                        )
                    )

                }

                Spacer(modifier = modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Text(
                        text = "성별 : ",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = "${"HERE"}",
                        style = TextStyle(
                            color = GrayWhite,
                            fontSize = 18.sp
                        )
                    )

                }

            }

            //Spacer(modifier = modifier.height(10.dp))

        }

        Column(
            modifier = modifier
                .fillMaxSize(),
        ){

            HorizontalDivider()

            Spacer(modifier = modifier.height(20.dp))


            Text(
                text = "굿생러가 될 준비가 되셨다면,\n굿생 시작 버튼을 눌러주세요!",
                style = TextStyle(
                    color = GrayWhite,
                    fontSize = 18.sp
                )
            )

            Spacer(modifier = modifier.height(20.dp))

            GodLifeButtonWhite(
                modifier = modifier
                    .align(Alignment.CenterHorizontally),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = ""
                    )
                },
                onClick = { /*TODO*/ },
                text = {
                    Text(
                        text = "굿생 시작!",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )

        }

    }

}

@Preview
@Composable
fun SignUpSuccessPreview(
    modifier: Modifier = Modifier
){
    var showLogo by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }
    var showProgressBar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500L)
        showLogo = true

        delay(1000L)
        showText = true

        delay(1000L)
        showProgressBar = true
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AnimatedVisibility(
            visible = showLogo,
            enter = fadeIn(
                initialAlpha = 0.2f
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.goodlife_inside_logo),
                contentDescription = "",
                modifier = modifier
                    .size(300.dp)
            )
        }


        AnimatedVisibility(
            visible = showText,
            enter = fadeIn(
                initialAlpha = 0.2f
            )
        ) {
            Text(
                text = "환영해요!",
                style = TextStyle(
                    color = GrayWhite,
                    fontSize = 20.sp
                ),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier.height(100.dp))

        AnimatedVisibility(
            visible = showProgressBar,
            enter = fadeIn(
                initialAlpha = 0.2f
            )
        ) {

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                CircularProgressIndicator(
                    color = OrangeMain,
                )

                Spacer(modifier.width(10.dp))

                Text(
                    text = "잠시후 자동으로 이동합니다.",
                    style = TextStyle(
                        color = OrangeMain,
                        fontSize = 14.sp
                    ),
                    textAlign = TextAlign.Center
                )

            }

        }


    }
}

