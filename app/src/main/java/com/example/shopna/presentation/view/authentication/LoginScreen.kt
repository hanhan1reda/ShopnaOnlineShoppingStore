package com.example.shopna.presentation.view.authentication

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.shopna.R
import com.example.shopna.data.model.LoginRequest
import com.example.shopna.presentation.view.home.MainScreen
import com.example.shopna.presentation.view_model.AuthViewModel
import com.example.shopna.ui.theme.greyColor
import com.example.shopna.ui.theme.kPrimaryColor
import com.example.shopna.ui.theme.lightGreyColor
import com.example.shopna.ui.theme.lighterGreyColor



class LoginScreen(private val context: Context) : Screen{



    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current


        val authViewModel = remember {
            AuthViewModel(navigator, context)
        }

        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val emailError = remember { mutableStateOf(false) }
        val passwordError = remember { mutableStateOf(false) }
        val emailErrorMessage = remember { mutableStateOf("") }
        val passwordErrorMessage = remember { mutableStateOf("") }

        if(authViewModel.getAuthToken()==null)  LazyColumn {
            item {
                Column(
                    modifier = Modifier
                        .systemBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.welcome_back),
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.interbold)),
                            fontWeight = FontWeight.W800,
                            color = kPrimaryColor
                        )
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = stringResource(id = R.string.welcome_message),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W400,
                            fontFamily = FontFamily(Font(R.font.interregular)),
                            color = greyColor,
                            lineHeight = 20.sp
                        )
                    )
                    Spacer(Modifier.height(25.dp))

                    CustomTextField(
                        text = stringResource(id = R.string.email),
                        value = email,
                        error = emailError.value,
                        errorMessage = emailErrorMessage.value
                    )
                    Spacer(Modifier.height(2.dp))
                    CustomTextField(
                        text = stringResource(id = R.string.password),
                        value = password,
                        error = passwordError.value,
                        errorMessage = passwordErrorMessage.value,
                        isPassword = true
                    )

                    Spacer(Modifier.height(15.dp))

                    CustomForgetPasswordRow()
                    Spacer(Modifier.height(20.dp))

                    if (authViewModel.isLoading.collectAsState().value) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = kPrimaryColor)
                        }
                    } else {
                        CustomButton(text = stringResource(id = R.string.login), onClick = {
                            val isEmailValid = validateEmail(context,email.value, emailError, emailErrorMessage)
                            val isPasswordValid = validatePassword(context,password.value, passwordError, passwordErrorMessage)

                            if (isEmailValid && isPasswordValid) {
                                authViewModel.login(
                                    LoginRequest(
                                        email = email.value,
                                        password = password.value,
                                    )
                                )
                            }
                        })
                    }
                    Spacer(Modifier.height(25.dp))
                    CustomDivider(text = stringResource(id = R.string.sign_in))
                    Spacer(Modifier.height(20.dp))
                    CustomSignInMethodsRow(text = stringResource(id = R.string.logging))
                    CustomTextSpan(textOne = stringResource(id = R.string.already_have_account), textTwo =stringResource(id = R.string.sign_up), onClick = {
                        navigator.push(RegisterScreen())
                    } )
                }
            }
        } else Navigator(MainScreen(authViewModel))

    }


}


@Composable
fun CustomTextSpan(textOne:String,  textTwo:String, onClick:()->Unit ){

    Column(modifier = Modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Spacer(Modifier.height(20.dp))
        Text(
            text = buildAnnotatedString {

                append(textOne)

                withStyle(style = SpanStyle(color = kPrimaryColor)) {
                    append(" $textTwo")
                }


            },
            fontSize = LocalConfiguration.current.fontScale.times(20).sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W400,
            fontFamily = FontFamily(Font(R.font.interregular)),
            color = Color.Black,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .clickable {
                    onClick()

                }



        )
    }
}

@Composable
fun CustomSignInMethodsRow(text :String){

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            Image(painter = painterResource(id = R.drawable.google), contentDescription ="Google Icon",modifier = Modifier.size(30.dp) )
            Spacer(Modifier.width(20.dp))

            Image(painter = painterResource(id = R.drawable.facebook), contentDescription ="facebook Icon",modifier = Modifier.size(38.dp) )
            Spacer(Modifier.width(20.dp))

            Image(painter = painterResource(id = R.drawable.apple), contentDescription ="apple Icon",modifier = Modifier.size(38.dp) )

        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = buildAnnotatedString {

                append(stringResource(id = R.string.by_agree,text)+" ")

                withStyle(style = SpanStyle(color = kPrimaryColor)) {
                    append(stringResource(id = R.string.terms_conditions))
                }
                append(" "+ stringResource(id = R.string.and)+" ")

                withStyle(style = SpanStyle(color = kPrimaryColor)) {
                    append(stringResource(id = R.string.privacy_policy))
                }

            },
            fontSize = LocalConfiguration.current.fontScale.times(16).sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W400,
            fontFamily = FontFamily(Font(R.font.interregular)),
            color = Color(0xff9E9E9E),



            )

    }


}

@Composable
fun CustomDivider(text :String){

    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp),horizontalArrangement = Arrangement.Center,verticalAlignment = Alignment.CenterVertically){
        HorizontalDivider(color = Color(0xffE0E0E0),
            modifier = Modifier.width(LocalConfiguration.current.screenHeightDp.dp/6))
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(id = R.string.or_with,text),
            fontSize = 12.sp,
            color = Color(0xff9E9E9E),
            fontWeight = FontWeight.W400,
            fontFamily = FontFamily(Font(R.font.interregular)),
            modifier = Modifier.align(alignment = Alignment.CenterVertically)

        )
        Spacer(modifier = Modifier.width(4.dp))
        HorizontalDivider(color = Color(0xffE0E0E0),
            modifier = Modifier.width(LocalConfiguration.current.screenHeightDp.dp/6))

    }
}


@Composable
fun CustomForgetPasswordRow(){

    Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
        .fillMaxWidth())
    {
        Row {
            Box(modifier = Modifier
                .size(12.dp)
                .border(width = 1.dp, color = Color(0xffA9B2B9))
                .align(alignment = Alignment.CenterVertically))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(id = R.string.remember_me) , fontSize = 12.sp, color = Color(0xff9E9E9E),fontFamily = FontFamily(Font(R.font.interregular)))

        }
        Text(
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            text = stringResource(id = R.string.forget_password) ,
            style = TextStyle(textDecoration = TextDecoration.Underline, fontSize = 12.sp, color = kPrimaryColor, fontWeight = FontWeight.W400, fontFamily = FontFamily(Font(R.font.interregular))))
    }
}

@Composable
fun CustomButton(text:String,onClick:()->Unit,color:Color?=null){

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color ?: kPrimaryColor),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Text(text = text, fontSize = 20.sp)

    }
}



@Composable
fun CustomTextField(
    text: String,
    value: MutableState<String>,
    error: Boolean,
    errorMessage: String,
    isPassword: Boolean = false
) {
    val passwordVisible = remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            label = {
                Text(
                    text = text,
                    color = lighterGreyColor,
                    fontFamily = FontFamily(Font(R.font.interregular))
                )
            },
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            value = value.value,
            onValueChange = {
                value.value = it
            },
            shape = RoundedCornerShape(25),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = lightGreyColor,
                disabledBorderColor = lightGreyColor,
                unfocusedBorderColor = lightGreyColor,
                errorBorderColor = kPrimaryColor,
                errorTextColor = kPrimaryColor,
                errorCursorColor = kPrimaryColor,
                errorPlaceholderColor = kPrimaryColor,
                cursorColor = kPrimaryColor
            ),
            isError = error,
            visualTransformation = if (isPassword && !passwordVisible.value) PasswordVisualTransformation() else VisualTransformation.None, // إظهار أو إخفاء النص
            trailingIcon = {
                if (isPassword) {
                    val image = if (passwordVisible.value)
                         R.drawable.show
                    else
                         R.drawable.eye

                    IconButton(onClick = {
                        passwordVisible.value = !passwordVisible.value
                    }) {
                        Icon(painter = painterResource(id = image), contentDescription = null)
                    }
                }
            }
        )
        if (error) {
            Text(
                text = errorMessage,
                color = kPrimaryColor,
                style = TextStyle(fontSize = 12.sp)
            )
        }
    }
}



fun validateEmail(context: Context,email: String, errorState: MutableState<Boolean>, errorMessageState: MutableState<String>): Boolean {
    return if (email.isEmpty()) {
        errorState.value = true
        errorMessageState.value = context.getString(R.string.email_empty_error)
        false
    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        errorState.value = true
        errorMessageState.value = context.getString(R.string.invalid_email_error)
        false
    } else {
        errorState.value = false
        errorMessageState.value = ""
        true
    }
}


fun validatePassword(context: Context, password: String, errorState: MutableState<Boolean>, errorMessageState: MutableState<String>): Boolean {
    return if (password.isEmpty()) {
        errorState.value = true
        errorMessageState.value = context.getString(R.string.password_empty_error)
        false
    } else if (password.length < 6) {
        errorState.value = true
        errorMessageState.value =  context.getString(R.string.password_length_error)
        false
    } else {
        errorState.value = false
        errorMessageState.value = ""
        true
    }
}


