package com.godlife.community_page.report

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.godlife.designsystem.component.GodLifeButtonWhite
import com.godlife.designsystem.component.GodLifeTextFieldGray
import com.godlife.designsystem.theme.GodLifeTypography
import com.godlife.designsystem.theme.GrayWhite
import com.godlife.designsystem.theme.GrayWhite3
import com.godlife.designsystem.theme.OrangeMain
import com.godlife.designsystem.view.GodLifeErrorScreen
import com.godlife.designsystem.view.GodLifeLoadingScreen
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    modifier: Modifier = Modifier,
    postId:String,
    userNickname: String,
    userId:String,
    category:String,
    title: String,
    navController: NavController,
    viewModel: ReportViewModel = hiltViewModel()
){

    Log.e("asdf", LocalDateTime.now().toString())
    Log.e("asdf", LocalTime.now().toString())
    Log.e("asdf", LocalDate.now().toString())

    viewModel.init(postId, userNickname, userId, category)

    val reportContent by remember { viewModel.reportContent }
    var isCheck by remember { mutableStateOf(false) }
    var isDialogVisble by remember { mutableStateOf(false) }

    val reportedNickname = viewModel.reportedNickname.collectAsState()
    val category = viewModel.category.collectAsState()
    val articleTitle  = viewModel.articleTitle.collectAsState()

    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()

    val reportOptions = listOf(
        "비속어, 폭언, 비하, 음란 등의 내용",
        "갈등 조장 및 허위 사실 유포",
        "광고성 내용",
        "기타(내용을 상세히 적어주세요.)"
    )
    var expanded by remember { mutableStateOf(false) }
    val reason  = viewModel.reportReason.collectAsState()

    Box(){

        if(uiState is ReportUiState.Loading ||
            uiState is ReportUiState.Success){

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .padding(10.dp)
            ) {

                item{
                    Column(
                        modifier = modifier
                            .statusBarsPadding()
                    ){
                        Text(text = "신고하기", style = GodLifeTypography.titleMedium)
                        Spacer(modifier.size(20.dp))
                    }
                }

                item{
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                            .padding(10.dp)
                    ) {

                        Row {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "",
                                tint = OrangeMain,
                            )
                            Spacer(modifier.size(5.dp))

                            Text(
                                text = "신고 전, 주의사항을 확인해주세요.",
                                style = TextStyle(
                                    color = OrangeMain,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier.size(10.dp))

                        Column(
                            modifier = modifier
                                .padding(start = 10.dp)
                        ){

                            Text(
                                text = "1. 신고 대상이 정확한지 확인해주세요.",
                                style = TextStyle(
                                    color = GrayWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            )

                            Text(
                                text = "2. 신고 사유를 정확히 적어주세요.",
                                style = TextStyle(
                                    color = GrayWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            )

                            Text(
                                text = "3. 신고 내역을 신중히 검토하고 처리하기 때문에\n" +
                                        "     처리하는데 시간이 걸릴 수 있어요.",
                                style = TextStyle(
                                    color = GrayWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            )

                            Text(
                                text = "4. 부적절한 신고 및 허위 신고의 경우 운영원칙에 따라\n" +
                                        "     제재를 받을 수 있어요.",
                                style = TextStyle(
                                    color = GrayWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            )

                        }

                    }

                    Spacer(modifier.size(20.dp))
                }


                item{

                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                    ) {

                        Row(
                            modifier = modifier,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = "신고 대상 닉네임 :  ",
                                style = TextStyle(
                                    color = OrangeMain,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            //신고 대상 닉네임
                            Text(
                                text = reportedNickname.value,
                                style = TextStyle(
                                    color = GrayWhite,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                        }
                        Spacer(modifier.size(10.dp))
                        HorizontalDivider()
                        Spacer(modifier.size(10.dp))

                        Row(
                            modifier = modifier,
                            verticalAlignment = Alignment.CenterVertically
                        ){

                            //게시물 신고면 "신고 게시물 제목", 댓글 신고면 "신고 댓글 내용"
                            Text(
                                text = if(category.value == "board") "신고 게시물 제목 :  " else "신고 댓글 내용 :  ",
                                style = TextStyle(
                                    color = OrangeMain,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            //신고 대상 게시물 제목 or 댓글 내용
                            Text(
                                text = if(category.value == "board") articleTitle.value else title,
                                style = TextStyle(
                                    color = GrayWhite,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier.size(10.dp))
                        HorizontalDivider()
                        Spacer(modifier.size(10.dp))

                        Text(
                            text = "신고 사유",
                            style = TextStyle(
                                color = OrangeMain,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier.size(10.dp))

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                        ) {
                            TextField(
                                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                value = reason.value,
                                onValueChange = {},
                                readOnly = true,
                                singleLine = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(
                                    focusedContainerColor = GrayWhite3,
                                    unfocusedContainerColor = GrayWhite3,
                                    focusedIndicatorColor = OrangeMain,
                                    unfocusedIndicatorColor = OrangeMain
                                ),
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                            ) {
                                reportOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option, style = MaterialTheme.typography.bodyLarge) },
                                        onClick = {
                                            viewModel.updateReason(option)
                                            expanded = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        }


                        Spacer(modifier.size(10.dp))
                        HorizontalDivider()
                        Spacer(modifier.size(10.dp))



                        Text(
                            text = "신고 내용",
                            style = TextStyle(
                                color = OrangeMain,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier.size(10.dp))

                        Box(
                            modifier
                                .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                                .heightIn(min = 200.dp)
                                .padding(5.dp)
                        ){

                            GodLifeTextFieldGray(
                                text = reportContent,
                                onTextChanged = { viewModel.updateText(it) },
                                hint = "신고 내용을 상세하게 작성해주세요.",
                                singleLine = false
                            )

                        }

                        Text(
                            modifier = modifier
                                .fillMaxWidth(),
                            text = "${reportContent.length}/500",
                            style = TextStyle(
                                color = GrayWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            textAlign = TextAlign.End
                        )

                        Spacer(modifier.size(10.dp))
                        HorizontalDivider()
                        Spacer(modifier.size(10.dp))

                        Row(
                            modifier = modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "주의사항을 숙지하셨나요?",
                                style = TextStyle(
                                    color = OrangeMain,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            )

                            Checkbox(
                                checked = isCheck,
                                onCheckedChange = { isCheck = !isCheck},
                                colors = CheckboxDefaults.colors(
                                    checkedColor = OrangeMain
                                )
                            )
                        }

                        Spacer(modifier.size(10.dp))

                        Box(
                            modifier = modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ){

                            GodLifeButtonWhite(
                                enabled = isCheck,
                                onClick = {
                                    if(reason.value == "신고 사유를 선택해주세요."){
                                        Toast.makeText(context, "신고 사유를 선택해주세요.", Toast.LENGTH_SHORT).show()
                                    }
                                    else if(reportContent.isEmpty() || reportContent == " "){
                                        Toast.makeText(context, "신고 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                                    }
                                    else{
                                        isDialogVisble = !isDialogVisble
                                    }
                                          },
                                text = {
                                    Text(
                                        text = "신고하기",
                                        style = TextStyle(
                                            color = if(isCheck) OrangeMain else GrayWhite,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            )
                        }

                    }

                }
            }

        }

        when(uiState){
            is ReportUiState.Loading -> {
                LoadingReportPageScreen()
            }
            is ReportUiState.Success -> {

            }
            is ReportUiState.SendSuccess -> {
                SendSuccessReportPageScreen(
                    navController = navController
                )
            }
            is ReportUiState.Error -> {
                ErrorReportPageScreen(
                    errorMsg = (uiState as ReportUiState.Error).error,
                    navController = navController
                )
            }
        }

        if(isDialogVisble){
            AlertDialog(
                containerColor = Color.White,
                onDismissRequest = { isDialogVisble = !isDialogVisble },
                title = {
                    Text(text = "신고하시겠어요?", style = TextStyle(color = OrangeMain, fontSize = 18.sp, fontWeight = FontWeight.Bold))
                },
                text = {
                    Text(text = "신고 내역은 수정하실 수 없어요."
                        ,
                        style = TextStyle(color = GrayWhite, fontSize = 15.sp, fontWeight = FontWeight.Normal))
                },
                confirmButton = {
                    GodLifeButtonWhite(
                        onClick = {

                            viewModel.sendReport()

                            isDialogVisble = !isDialogVisble

                        },
                        text = { Text(text = "신고하기", style = TextStyle(color = OrangeMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                    )
                },
                dismissButton = {
                    GodLifeButtonWhite(
                        onClick = { isDialogVisble = !isDialogVisble },
                        text = { Text(text = "취소", style = TextStyle(color = OrangeMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)) }
                    )
                }
            )
        }


    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ReportPageScreenPreview(
    modifier: Modifier = Modifier
){
    var text by remember { mutableStateOf("") }
    var isCheck by remember { mutableStateOf(false) }

    val options = listOf(
        "비속어, 폭언, 비하, 음란 등의 내용",
        "갈등 조장 및 허위 사실 유포",
        "광고성 게시물",
        "보상 목적 게시물(무성의한 게시물)",
        "기타(내용을 상세히 적어주세요.)"
    )
    var expanded by remember { mutableStateOf(false) }
    var reason by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(10.dp)
    ) {

        item{
            Column(
                modifier = modifier
                    .statusBarsPadding()
            ){
                Text(text = "신고하기", style = GodLifeTypography.titleMedium)
                Spacer(modifier.size(20.dp))
            }
        }

        item{
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                    .padding(10.dp)
            ) {

                Row {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "",
                        tint = OrangeMain,
                    )
                    Spacer(modifier.size(5.dp))

                    Text(
                        text = "신고 전, 주의사항을 확인해주세요.",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier.size(10.dp))

                Column(
                    modifier = modifier
                        .padding(start = 10.dp)
                ){

                    Text(
                        text = "1. 신고 대상이 정확한지 확인해주세요.",
                        style = TextStyle(
                            color = GrayWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Text(
                        text = "2. 신고 사유를 정확히 적어주세요.",
                        style = TextStyle(
                            color = GrayWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Text(
                        text = "3. 신고 내역을 신중히 검토하고 처리하기 때문에\n" +
                                "     처리하는데 시간이 걸릴 수 있어요.",
                        style = TextStyle(
                            color = GrayWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Text(
                        text = "4. 부적절한 신고 및 허위 신고의 경우 운영원칙에 따라\n" +
                                "     제재를 받을 수 있어요.",
                        style = TextStyle(
                            color = GrayWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )

                }

            }

            Spacer(modifier.size(20.dp))
        }


        item{

            Column(
                modifier = modifier
                    .fillMaxWidth()
            ) {

                Row(
                    modifier = modifier,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "신고 대상 닉네임 :  ",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    //신고 대상 닉네임
                    Text(
                        text = "Nickname",
                        style = TextStyle(
                            color = GrayWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                Spacer(modifier.size(10.dp))
                HorizontalDivider()
                Spacer(modifier.size(10.dp))

                Row(
                    modifier = modifier,
                    verticalAlignment = Alignment.CenterVertically
                ){

                    //게시물 신고면 "신고 게시물 제목", 댓글 신고면 "신고 댓글 내용"
                    Text(
                        text = "신고 게시물 제목 :  ",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    //신고 대상 게시물 제목 or 댓글 내용
                    Text(
                        text = "BlaBla~",
                        style = TextStyle(
                            color = GrayWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier.size(10.dp))
                HorizontalDivider()
                Spacer(modifier.size(10.dp))


                Text(
                    text = "신고 사유",
                    style = TextStyle(
                        color = OrangeMain,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier.size(10.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        value = reason,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(
                            focusedContainerColor = GrayWhite3,
                            unfocusedContainerColor = GrayWhite3,
                            focusedIndicatorColor = OrangeMain,
                            unfocusedIndicatorColor = OrangeMain
                        ),
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, style = MaterialTheme.typography.bodyLarge) },
                                onClick = {
                                    reason = option
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }

                Spacer(modifier.size(10.dp))
                HorizontalDivider()
                Spacer(modifier.size(10.dp))

                Text(
                    text = "신고 내용",
                    style = TextStyle(
                        color = OrangeMain,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier.size(10.dp))

                Box(
                    modifier
                        .background(color = GrayWhite3, shape = RoundedCornerShape(16.dp))
                        .heightIn(min = 200.dp)
                        .padding(5.dp)
                ){

                    GodLifeTextFieldGray(
                        text = text,
                        onTextChanged = {  },
                        hint = "신고 내용을 상세하게 작성해주세요.",
                        singleLine = false
                    )

                }

                Text(
                    modifier = modifier
                        .fillMaxWidth(),
                    text = "0/500",
                    style = TextStyle(
                        color = GrayWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.End
                )

                Spacer(modifier.size(10.dp))
                HorizontalDivider()
                Spacer(modifier.size(10.dp))

                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "주의사항을 숙지하셨나요?",
                        style = TextStyle(
                            color = OrangeMain,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Checkbox(
                        checked = isCheck,
                        onCheckedChange = { isCheck = !isCheck},
                        colors = CheckboxDefaults.colors(
                            checkedColor = OrangeMain
                        )
                    )
                }

                Spacer(modifier.size(10.dp))

                Box(
                    modifier = modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ){

                    GodLifeButtonWhite(
                        enabled = isCheck,
                        onClick = { /*TODO*/ },
                        text = {
                            Text(
                                text = "신고하기",
                                style = TextStyle(
                                    color = if(isCheck) OrangeMain else GrayWhite,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    )
                }

            }

        }
    }
}

@Preview
@Composable
fun LoadingReportPageScreen(
    text: String = "신고 정보를 가져오고 있어요."
){
    GodLifeLoadingScreen(text = text)
}

@Preview
@Composable
fun SendSuccessReportPageScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    buttonText: String = "메인으로 돌아가기"
){

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            modifier = modifier
                .size(40.dp),
            imageVector = Icons.Outlined.Info,
            contentDescription = "",
            tint = OrangeMain
        )

        Spacer(modifier.size(10.dp))

        Text(
            text = "신고 접수가 완료되었어요.",
            style = TextStyle(
                color = OrangeMain,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier.size(20.dp))

        Text(
            text = "접수 내역을 최대한 빠르게 해결해드릴게요.\n" +
                    "클린한 굿생 서비스를 만들기 위해 \n" +
                    "더욱 노력하겠습니다!",
            style = TextStyle(
                color = GrayWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier.size(20.dp))

        GodLifeButtonWhite(
            onClick = { navController?.popBackStack() },
            text = { Text(text = buttonText, style = TextStyle(color = OrangeMain, fontSize = 15.sp, fontWeight = FontWeight.Bold)) }
        )



    }
}

@Preview
@Composable
fun ErrorReportPageScreen(
    errorMsg: String = "",
    navController: NavController? = null
){
    GodLifeErrorScreen(
        errorMessage = errorMsg,
        buttonEvent = {navController?.popBackStack()}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ReportCategoryPreview() {
    val options = listOf(
        "비속어, 폭언, 비하, 음란 등의 내용",
        "갈등 조장 및 허위 사실 유포",
        "광고성 게시물",
        "보상 목적 게시물(무성의한 게시물)",
        "기타(내용을 상세히 적어주세요.)"
    )
    var expanded by remember { mutableStateOf(false) }
    var reason by remember { mutableStateOf(options[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            TextField(
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                value = reason,
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, style = MaterialTheme.typography.bodyLarge) },
                        onClick = {
                            reason = option
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }


}