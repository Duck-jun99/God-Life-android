package com.godlife.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godlife.designsystem.theme.GodLifeTheme
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

            Column(modifier = Modifier.fillMaxWidth()
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

@Preview(showBackground = true)
@Composable
fun TextFiledPreview(){
    GodLifeTheme {
        Column(modifier = Modifier.fillMaxSize()
            .background(PurpleMain)) {
            var text = "Hi"
            GodLifeTextField(text = text, onTextChanged = { it -> text })
        }
    }
}