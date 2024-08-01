package com.godlife.designsystem.component

import android.content.Context
import android.graphics.BlurMaskFilter
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.PurpleMain

@Composable
fun GodLifeTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    hint: String = "",
    singleLine: Boolean = false,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = text,
        onValueChange = { newText ->
            if(singleLine){
                // 줄바꿈 문자를 제거하고 한 줄로 제한합니다.
                val singleLineText = newText.replace("\n", "")
                onTextChanged(singleLineText)
            }
            else{
                onTextChanged(newText)
            }

        },
        keyboardOptions = keyboardOptions,
        keyboardActions = if(singleLine) KeyboardActions(onDone = { keyboardController?.hide() }) else KeyboardActions.Default,
        cursorBrush = SolidColor(Color.White),
        singleLine = singleLine,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                innerTextField()
                if (text.isEmpty()) {
                    Text(
                        text = hint,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    )
                }
            }
        }
    )
}

@Composable
fun GodLifeTextFieldGray(
    text: String,
    onTextChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
    hint: String = "",
    singleLine: Boolean = false,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = text,
        onValueChange = { newText ->
            if(singleLine){
                // 줄바꿈 문자를 제거하고 한 줄로 제한합니다.
                val singleLineText = newText.replace("\n", "")
                onTextChanged(singleLineText)
            }
            else{
                onTextChanged(newText)
            }

        },
        keyboardOptions = if(singleLine)keyboardOptions else KeyboardOptions.Default,
        keyboardActions = if(singleLine) KeyboardActions(onDone = { keyboardController?.hide() }) else KeyboardActions.Default,
        cursorBrush = SolidColor(Color.Black),
        singleLine = singleLine,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                innerTextField()
                if (text.isEmpty()) {
                    Text(
                        text = hint,
                        style = TextStyle(
                            color = GrayWhite,
                            fontSize = 15.sp
                        )
                    )
                }
            }
        }
    )
}

@Composable
fun GodLifeCreateCommentBar(
    modifier: Modifier = Modifier,
    comment: String = "",
    hint: String = "댓글을 입력해주세요.",
    onTextChanged: (String) -> Unit = {},
    containerColor: Color = Color.White,
    onPostClicked: () -> Unit =  {},
    context: Context
) {

    Row(
        modifier = modifier
            .coloredShadow(
                color = OpaqueDark,
                borderRadius = 0.dp,
                blurRadius = 5.dp,
                offsetY = (-1).dp,
                offsetX = 0.dp,
                spread = 0f
            )
            .fillMaxWidth()
            .height(40.dp)
            .background(color = containerColor)
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.size(15.dp))


        BasicTextField(
            modifier = modifier.weight(0.8f),
            value = comment,
            onValueChange = onTextChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Send),
            singleLine = true,
            decorationBox = {innerTextField ->

                innerTextField()
                if(comment.isEmpty()){

                    Text(text = hint,
                        style = TextStyle(
                            color = GrayWhite,
                            fontSize = 15.sp
                        )

                    )

                }


            }
        )

        Icon(
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    if(comment.isNotBlank()) onPostClicked() else Toast.makeText(context, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show()
                           },
            imageVector = Icons.AutoMirrored.Outlined.Send,
            contentDescription = null,
            tint = PurpleMain
        )


    }

}

@Preview(showBackground = true)
@Composable
fun GodLifeSearchBar(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(40.dp),
    searchText: String = "",
    hint: String = "검색어를 입력해주세요.",
    onTextChanged: (String) -> Unit = {},
    containerColor: Color = OpaqueDark,
    onSearchClicked: () -> Unit =  {}
    ) {

    Row(
        modifier = modifier
            .background(color = containerColor, shape = RoundedCornerShape(20.dp))
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.size(15.dp))

        Icon(
            modifier = Modifier
                .size(30.dp)
                .clickable { onSearchClicked() },
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = Color.White
        )

        Spacer(modifier = Modifier.size(15.dp))

        BasicTextField(
            value = searchText,
            onValueChange = onTextChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearchClicked() }),
            singleLine = true,
            decorationBox = {

                Text(text = if (searchText.isEmpty()) hint else searchText, style = TextStyle(
                    color = Color.White,
                    fontSize = 15.sp)
                )

            }
        )

        Spacer(modifier = Modifier.size(15.dp))

    }

}


@Preview(showBackground = true)
@Composable
fun GodLifeCreateCommentBarPreview(
    modifier: Modifier = Modifier,
    comment: String = "",
    hint: String = "댓글을 입력해주세요.",
    onTextChanged: (String) -> Unit = {},
    containerColor: Color = Color.White,
    onPostClicked: () -> Unit =  {}
) {

    Row(
        modifier = modifier
            .coloredShadow(
                color = OpaqueDark,
                borderRadius = 0.dp,
                blurRadius = 5.dp,
                offsetY = (-1).dp,
                offsetX = 0.dp,
                spread = 0f
            )
            .fillMaxWidth()
            .height(40.dp)
            .background(color = containerColor)
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.size(15.dp))


        BasicTextField(
            modifier = modifier.weight(0.8f),
            value = comment,
            onValueChange = onTextChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Send),
            singleLine = true,
            decorationBox = {innerTextField ->
                innerTextField()
                if(comment.isEmpty()){

                    Text(text = hint,
                        style = TextStyle(
                            color = GrayWhite,
                            fontSize = 15.sp
                        )

                    )

                }

            }
        )

        Icon(
            modifier = Modifier
                .size(30.dp)
                .clickable { onPostClicked() },
            imageVector = Icons.AutoMirrored.Outlined.Send,
            contentDescription = null,
            tint = PurpleMain
        )


    }

}

@Preview(showBackground = true)
@Composable
fun TextFiledPreview(){
    GodLifeTheme {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            ) {
            var text = "Hi"

            Box(modifier = Modifier.background(PurpleMain)){

                GodLifeTextField(text = text, onTextChanged = { it -> text })
            }
            
            Spacer(modifier = Modifier.size(20.dp))

            GodLifeTextFieldGray(
                text = text,
                onTextChanged = {it -> text},
                hint = "제목을 입력해주세요."
            )

            Spacer(modifier = Modifier.size(20.dp))

            Box(modifier = Modifier.background(Color.White)){
                
                //GodLifeSearchTextField(text = text, onTextChanged = {it -> text})
                GodLifeSearchBar(searchText = "", onTextChanged = {it -> text})
            }

            Spacer(modifier = Modifier.size(20.dp))

            GodLifeCreateCommentBarPreview()


        }


    }
}

internal fun Modifier.coloredShadow(
    color: Color = Color.Black,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Float = 0f,
    modifier: Modifier = Modifier,
) = this.then(
    modifier.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spread.dp.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = (this.size.width + spreadPixel)
            val bottomPixel =  (this.size.height + spreadPixel)

            if (blurRadius != 0.dp) {
                /*
                    The feature maskFilter used below to apply the blur effect only works
                    with hardware acceleration disabled.
                 */
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }

            frameworkPaint.color = color.toArgb()
            it.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = borderRadius.toPx(),
                radiusY = borderRadius.toPx(),
                paint
            )
        }
    }
)