package com.example.shopna.presentation.view.home

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.rememberAsyncImagePainter
import com.example.shopna.R
import com.example.shopna.presentation.view_model.AuthViewModel
import com.example.shopna.presentation.view_model.CartViewModel
import com.example.shopna.ui.theme.backgroundColor
import com.example.shopna.ui.theme.kPrimaryColor
import com.example.shopna.ui.theme.lightGreyColor

@Composable
fun ProfileScreen(authViewModel: AuthViewModel,cartViewModel: CartViewModel) {
    val userData = authViewModel.user.collectAsState()
    var isClicked by remember { mutableStateOf(false) }
    val imageUrl = rememberSaveable { mutableStateOf(userData.value?.data?.image ?: "") }
    val painter = rememberAsyncImagePainter(imageUrl.value)


    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUrl.value = it.toString()
        }
    }
    val navigator = LocalNavigator.currentOrThrow

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                .background(kPrimaryColor.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.my_profile), // Use string resource
                        style = TextStyle(
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.interregular)),
                            color = Color.White
                        )
                    )

                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.settings),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.size(100.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter("https://student.valuxapps.com/storage/assets/defaults/user.jpg"),
                        contentDescription = stringResource(id = R.string.user_profile_picture), // Use string resource
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(5.dp, backgroundColor, CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.pen),
                        contentDescription = stringResource(id = R.string.edit_profile_picture), // Use string resource
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.BottomEnd)
                            .clickable {
                                launcher.launch("image/*")
                            }
                            .background(backgroundColor, shape = CircleShape)
                            .padding(5.dp),
                        tint = kPrimaryColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = userData.value?.data?.name ?: stringResource(id = R.string.default_user_name), // Use string resource
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
        )
        Text(
            text = userData.value?.data?.email ?: stringResource(id = R.string.default_email), // Use string resource
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Gray.copy(alpha = 0.4f)),
            modifier = Modifier.padding(top = 2.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Column {
//                OptionRow(
//                    optionText = stringResource(id = R.string.dark_mode), // Use string resource
//                    onClick = { isClicked = !isClicked },
//                    isToggle = true,
//                    isChecked = isClicked,
//                    icon = R.drawable.nightmode
//                )
                OptionRow(optionText = stringResource(id = R.string.edit_profile), onClick = {
                    navigator.push(EditProfile(authViewModel))
                }, icon = R.drawable.avatar)
                Spacer(modifier = Modifier.height(5.dp))
                OptionRow(optionText = stringResource(id = R.string.orders), onClick = {
                    cartViewModel.getOrders()
                    navigator.push(OrdersScreen(cartViewModel))}, icon = R.drawable.orderdelivery)
                Spacer(modifier = Modifier.height(5.dp))
                OptionRow(optionText = stringResource(id = R.string.language), onClick = {
                    navigator.push(LanguageScreen())
                }, icon = R.drawable.arabic)
                Spacer(modifier = Modifier.height(5.dp))
                OptionRow(optionText = stringResource(id = R.string.logout), onClick = {
                    authViewModel.logout()
                }, icon = R.drawable.logout)
            }
        }
    }
}

@Composable
fun OptionRow(
    optionText: String,
    onClick: () -> Unit,
    isToggle: Boolean = false,
    isChecked: Boolean = false,
    icon: Int
) {
    val context = LocalContext.current
    val sharedPreferences =context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    val langCode = sharedPreferences.getString("langCode", "en")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = icon), contentDescription = null, tint = kPrimaryColor.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = optionText,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal)
            )
        }

        if (isToggle) {
            Switch(checked = isChecked, onCheckedChange = { onClick() }, colors = SwitchDefaults.colors(
                checkedIconColor = kPrimaryColor,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = lightGreyColor,
                uncheckedBorderColor = kPrimaryColor.copy(alpha = 0.3f),
                checkedTrackColor = kPrimaryColor
            ))
        } else {
            Icon(
                imageVector = if (langCode == "ar") Icons.Default.KeyboardArrowLeft else Icons.Default.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.navigate), // Use string resource
                tint = Color.Gray
            )
        }
    }
}
