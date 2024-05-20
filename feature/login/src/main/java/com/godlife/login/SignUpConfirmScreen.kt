package com.godlife.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godlife.designsystem.component.GodLifeButton
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.PurpleMain

@Composable
fun SignUpConfirmScreen(
    signUpViewModel: SignUpViewModel
){
    GodLifeTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleMain)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
                    .weight(0.8f)
            ) {

                AnimatedVisibility(visible = true) {
                    Text(
                        text = "아래 입력한 정보가 맞는지 확인해주세요.",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.size(100.dp))

                AnimatedVisibility(visible = true) {

                    Column {
                        Text(
                            text = "닉네임: ${signUpViewModel.nickname.value}",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.size(50.dp))

                        Text(
                            text = "이메일: ${signUpViewModel.email.value}",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.size(50.dp))

                        Row(modifier = Modifier.fillMaxWidth()){
                            Text(
                                text = "나이: ${signUpViewModel.age.value} 세",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 18.sp
                                ),
                                modifier = Modifier.weight(0.5f)
                            )

                            Text(
                                text = "성별: ${signUpViewModel.sex.value}",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 18.sp,
                                ),
                                modifier = Modifier.weight(0.5f)
                            )
                        }

                        Spacer(modifier = Modifier.size(50.dp))

                    }

                }


                Divider()

                Spacer(modifier = Modifier.size(50.dp))

                AnimatedVisibility(visible = true) {
                    Text(
                        text = "갓생러가 될 준비가 되셨다면,\n갓생 시작 버튼을 눌러주세요!",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    )
                }


            }

            Column(
                modifier = Modifier.padding(20.dp)
                    .align(Alignment.CenterHorizontally)
                    .weight(0.2f)
            ){
                GodLifeButtonWhite(onClick = {},
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.5f)
                ) {
                    Text(text = "갓생 시작",
                        color = PurpleMain,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpConfirmScreenPreview(){
    GodLifeTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleMain)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
                    .weight(0.8f)
            ) {

                AnimatedVisibility(visible = true) {
                    Text(
                        text = "아래 입력한 정보가 맞는지 확인해주세요.",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.size(100.dp))

                AnimatedVisibility(visible = true) {

                    Column {
                        Text(
                            text = "닉네임: ${"HERE"}",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.size(50.dp))

                        Text(
                            text = "이메일: ${"HERE"}",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.size(50.dp))

                        Row(modifier = Modifier.fillMaxWidth()){
                            Text(
                                text = "나이: ${"HERE"} 세",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 18.sp
                                ),
                                modifier = Modifier.weight(0.5f)
                            )

                            Text(
                                text = "성별: ${"HERE"}",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 18.sp,
                                ),
                                modifier = Modifier.weight(0.5f)
                            )
                        }

                        Spacer(modifier = Modifier.size(50.dp))


                    }

                }


                Divider()

                Spacer(modifier = Modifier.size(50.dp))

                AnimatedVisibility(visible = true) {
                    Text(
                        text = "갓생러가 될 준비가 되셨다면,\n갓생 시작 버튼을 눌러주세요!",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    )
                }


            }

            Column(
                modifier = Modifier.padding(20.dp)
                    .align(Alignment.CenterHorizontally)
                    .weight(0.2f)
            ){
                GodLifeButtonWhite(onClick = {},
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.5f)
                ) {
                    Text(text = "갓생 시작",
                        color = PurpleMain,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}