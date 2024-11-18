package com.example.shopna.presentation.view.authentication

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.shopna.R
import com.example.shopna.data.model.RegisterRequest
import com.example.shopna.presentation.view_model.AuthViewModel
import com.example.shopna.ui.theme.greyColor
import com.example.shopna.ui.theme.kPrimaryColor

class RegisterScreen : Screen{
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current

        val viewModel = remember {
            AuthViewModel(navigator, context)
        }
        val username = remember { mutableStateOf("") }
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val phoneNumber = remember { mutableStateOf("") }

        val usernameError = remember { mutableStateOf(false) }
        val emailError = remember { mutableStateOf(false) }
        val passwordError = remember { mutableStateOf(false) }
        val phoneNumberError = remember { mutableStateOf(false) }
        val usernameErrorMessage = remember { mutableStateOf("") }
        val emailErrorMessage = remember { mutableStateOf("") }
        val passwordErrorMessage = remember { mutableStateOf("") }
        val phoneNumberErrorMessage = remember { mutableStateOf("") }

        LazyColumn {
            item {
                Column(
                    modifier = Modifier
                        .systemBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.create_account),
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.interbold)),
                            fontWeight = FontWeight.W800,
                            color = kPrimaryColor
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = stringResource(id = R.string.signup_message),
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
                        text = stringResource(id = R.string.username),
                        value = username,
                        error = usernameError.value,
                        errorMessage = usernameErrorMessage.value
                    )
                    Spacer(Modifier.height(2.dp))

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
                    Spacer(Modifier.height(2.dp))
                    CustomTextField(
                        text = stringResource(id = R.string.phone_number),
                        value = phoneNumber,
                        error = phoneNumberError.value,
                        errorMessage = phoneNumberErrorMessage.value,

                    )

                    Spacer(Modifier.height(15.dp))



                    if (viewModel.isLoading.collectAsState().value) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = kPrimaryColor)
                        }
                    } else {
                        CustomButton(text = stringResource(id = R.string.create_account), onClick = {
                            val isUsernameValid = validateUsername(context,username.value, usernameError, usernameErrorMessage)
                            val isEmailValid = validateEmail(context,email.value, emailError, emailErrorMessage)
                            val isPasswordValid = validatePassword(context,password.value, passwordError, passwordErrorMessage)
                            val isPhoneNumberValid = validatePhoneNumber(context,phoneNumber.value, phoneNumberError, phoneNumberErrorMessage)

                            if (isEmailValid && isPasswordValid && isUsernameValid && isPhoneNumberValid) {
                                viewModel.register(
                                    RegisterRequest(
                                        email = email.value,
                                        password = password.value,
                                        name = username.value,
                                        phone = phoneNumber.value

                                    )
                                )
                            }
                        })
                    }
                    Spacer(Modifier.height(25.dp))
                    CustomDivider(text = stringResource(id = R.string.sign_up))
                    Spacer(Modifier.height(20.dp))
                    CustomSignInMethodsRow(text = stringResource(id = R.string.signing_up))
                    CustomTextSpan(textOne = stringResource(id = R.string.no_account_yet), textTwo =stringResource(id = R.string.login), onClick = {
                        navigator.pop()
                    } )
                }
            }
        }
    }
}

fun validateUsername(context: Context, username: String, errorState: MutableState<Boolean>, errorMessageState: MutableState<String>): Boolean {
    return if (username.isEmpty()) {
        errorState.value = true
        errorMessageState.value = context.getString(R.string.username_empty_error)
        false
    } else if (username.length < 3) {
        errorState.value = true
        errorMessageState.value = context.getString(R.string.username_length_error)
        false
    } else {
        errorState.value = false
        errorMessageState.value = ""
        true
    }
}

fun validatePhoneNumber(context:Context,phoneNumber: String, errorState: MutableState<Boolean>, errorMessageState: MutableState<String>): Boolean {
    return if (phoneNumber.isEmpty()) {
        errorState.value = true
        errorMessageState.value = context.getString(R.string.phone_number_empty_error)
        false
    } else if (!phoneNumber.all { it.isDigit() }) {
        errorState.value = true
        errorMessageState.value = context.getString(R.string.phone_number_digits_error)
        false
    } else if (phoneNumber.length < 10) {
        errorState.value = true
        errorMessageState.value = context.getString(R.string.phone_number_length_error)
        false
    } else {
        errorState.value = false
        errorMessageState.value = ""
        true
    }
}



