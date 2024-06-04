package com.godlife.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite2
import com.godlife.designsystem.theme.PurpleMain

@Composable
fun GodLifeTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    BasicTextField(
        value = text,
        onValueChange = onTextChanged,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = {

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)) {
                Text(text = text, style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp
                )
                )
                Divider(modifier = Modifier.fillMaxWidth(), color = Color.White)
            }

        }
    )
}

@Composable
fun GodLifeTextFieldGray(
    text: String,
    onTextChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    hint: String = "",
) {

    BasicTextField(
        value = text,
        onValueChange = onTextChanged,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = {

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)) {

                Text(text = if (text.isEmpty()) hint else text, style = TextStyle(
                    color = GrayWhite,
                    fontSize = 15.sp
                )
                )
                //Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), color = GrayWhite2)
            }

        }
    )
}

@Composable
fun GodLifeSearchTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    hint: String = "검색어를 입력해주세요."

    ) {

    BasicTextField(
        value = text,
        onValueChange = onTextChanged,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = {

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)) {

                Text(text = if (text.isEmpty()) hint else text, style = TextStyle(
                    color = GrayWhite,
                    fontSize = 15.sp)
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    color = GrayWhite2
                )
            }

        }
    )
}

@Preview(showBackground = true)
@Composable
fun TextFiledPreview(){
    GodLifeTheme {
        Column(modifier = Modifier
            .fillMaxSize()
            ) {
            var text = "Hi"

            Box(modifier = Modifier.background(PurpleMain)){

                GodLifeTextField(text = text, onTextChanged = { it -> text })
            }
            
            Spacer(modifier = Modifier.size(20.dp))

            Box(modifier = Modifier.background(Color.White)){
                
                GodLifeSearchTextField(text = text, onTextChanged = {it -> text})
            }


        }


    }
}