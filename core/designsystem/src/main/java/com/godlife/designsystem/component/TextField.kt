package com.godlife.designsystem.component

import android.content.Context
import android.graphics.BlurMaskFilter
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godlife.designsystem.theme.GodLifeTheme
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.OpaqueDark
import com.godlife.designsystem.theme.OpaqueLight
import com.godlife.designsystem.theme.OrangeLight
import com.godlife.designsystem.theme.PurpleMain
import kotlinx.coroutines.delay


@Composable
fun GodLifeSignUpTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    actionCallBack: () -> Unit = {},
    containerColor: Color = OpaqueLight,
    hint: String = "",
    singleLine: Boolean = true,
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
        keyboardActions = KeyboardActions(
            onDone = {
                actionCallBack()
                keyboardController?.hide()
            }
        ),
        cursorBrush = SolidColor(Color.Black),
        singleLine = singleLine,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .background(color = containerColor)
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
            .height(50.dp)
            .background(color = containerColor)
            //.padding(5.dp)
                ,
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

        Box(
            modifier = Modifier
                .size(50.dp)
                .background(color = PurpleMain)
                .clickable {
                    if (comment.isNotBlank()) onPostClicked() else Toast
                        .makeText(context, "댓글을 입력해주세요.", Toast.LENGTH_SHORT)
                        .show()
                },
            contentAlignment = Alignment.Center
        ) {

            Icon(
                modifier = Modifier
                    .size(30.dp),
                imageVector = Icons.Outlined.Create,
                contentDescription = null,
                tint = Color.White
            )

        }
    }

}

@Preview(showBackground = true)
@Composable
fun GodLifeSearchBar(
    modifier: Modifier = Modifier,
    searchText: String = "",
    hint: String = "검색어를 입력해주세요.",
    onTextChanged: (String) -> Unit = {},
    contentColor: Color = Color.White,
    containerColor: Color = OpaqueDark,
    onSearchClicked: () -> Unit =  {},
    sortBoolean: Boolean = true,
    onSortClicked: () -> Unit = {}
    ) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(color = containerColor, shape = RoundedCornerShape(20.dp))
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        BasicTextField(
            modifier = modifier
                .fillMaxWidth(if(sortBoolean)0.7f else 0.8f),
            cursorBrush = SolidColor(contentColor),
            textStyle = TextStyle(
                color = contentColor,
                fontSize = 15.sp,
                textAlign = TextAlign.Start
            ),
            value = searchText,
            onValueChange = onTextChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearchClicked() }),
            singleLine = true,
            decorationBox = {innerTextField ->

                innerTextField()

                if(searchText.isEmpty()){

                    Text(
                        text = hint,
                        style = TextStyle(
                            color = contentColor,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Start
                        )
                    )

                }

            }
        )

        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){

            if(sortBoolean){
                IconButton(
                    modifier = modifier
                        .size(30.dp),
                    onClick = { onSortClicked() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        tint = contentColor
                    )

                }
            }

            IconButton(
                modifier = modifier
                    .size(30.dp),
                onClick = { onSearchClicked() }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = contentColor
                )

            }


        }


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun GodLifeSearchBar2(
    modifier: Modifier = Modifier,
    searchText: String = "",
    hint: String = "검색어를 입력해주세요.",
    onTextChanged: (String) -> Unit = {},
    contentColor: Color = Color.White,
    containerColor: Color = OpaqueDark,
    onSearchClicked: () -> Unit =  {},
    sortBoolean: Boolean = true,
    onSortClicked: () -> Unit = {}
) {

    //var expanded by rememberSaveable { mutableStateOf(false) }
    SearchBar(
        inputField = {
            GodLifeInputField(
                modifier = modifier
                    .fillMaxSize(),
                query = searchText,
                onQueryChange = onTextChanged,
                onSearch = {
                    //expanded = false
                    onSearchClicked()
                },
                expanded = false,
                onExpandedChange = {
                    //expanded = it
                },
                placeholder = {
                    Text(
                        text = hint,
                        style = TextStyle(
                            color = contentColor,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Start
                        )
                    ) },
                leadingIcon = {
                    IconButton(
                        onClick = { onSearchClicked() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = contentColor
                        )
                    } },
                trailingIcon = {
                    if(sortBoolean){
                        IconButton(
                            onClick = { onSortClicked() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null,
                                tint = contentColor
                            )
                        }
                    }
                     },
                textColor = contentColor,
                cursorColor = contentColor,
            )
                     },
        colors = SearchBarDefaults.colors(containerColor = containerColor, dividerColor = containerColor),
        expanded = false,
        onExpandedChange = {}
    ) {

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
            .height(50.dp)
            .background(color = containerColor)
            //.padding(5.dp),
                ,
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

        Box(
            modifier = Modifier
                .size(50.dp)
                .background(color = PurpleMain)
                .clickable { onPostClicked() },
            contentAlignment = Alignment.Center
        ) {

            Icon(
                modifier = Modifier
                    .size(30.dp),
                imageVector = Icons.Outlined.Create,
                contentDescription = null,
                tint = Color.White
            )

        }
        

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

@ExperimentalMaterial3Api
@Composable
fun GodLifeInputField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textColor: Color,
    cursorColor: Color,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = SearchBarDefaults.inputFieldColors(),
    interactionSource: MutableInteractionSource? = null,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val focused = interactionSource.collectIsFocusedAsState().value
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .height(40.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { if (it.isFocused) onExpandedChange(true) }
            .semantics {

                if (expanded) {
                    //stateDescription = suggestionsAvailableSemantics
                }
                onClick {
                    focusRequester.requestFocus()
                    true
                }
            },
        enabled = enabled,
        singleLine = true,
        textStyle = LocalTextStyle.current.merge(TextStyle(color = textColor)),
        cursorBrush = SolidColor(cursorColor),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
        interactionSource = interactionSource,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = query,
                innerTextField = innerTextField,
                enabled = enabled,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                placeholder = placeholder,
                leadingIcon = leadingIcon?.let { leading -> {
                    Box(Modifier.offset(x = 4.dp)) { leading() }
                } },
                trailingIcon = trailingIcon?.let { trailing -> {
                    Box(Modifier.offset(x = (-4).dp)) { trailing() }
                } },
                shape = SearchBarDefaults.inputFieldShape,
                colors = colors,
                contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(top = 0.dp, bottom = 0.dp),
                container = {},
            )
        }
    )

    val shouldClearFocus = !expanded && focused
    LaunchedEffect(expanded) {
        if (shouldClearFocus) {
            // Not strictly needed according to the motion spec, but since the animation
            // already has a delay, this works around b/261632544.
            delay(100.0.toLong())
            focusManager.clearFocus()
        }
    }
}