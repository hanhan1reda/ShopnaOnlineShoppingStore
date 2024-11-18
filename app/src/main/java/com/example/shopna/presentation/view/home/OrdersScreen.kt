package com.example.shopna.presentation.view.home

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.FontWeight
import cafe.adriel.voyager.core.screen.Screen
import com.example.shopna.data.model.DataXXXXXXXXXXXXXXXXXX
import com.example.shopna.presentation.view_model.CartViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.shopna.R
import com.example.shopna.ui.theme.kPrimaryColor

class OrdersScreen(private val cartViewModel: CartViewModel) : Screen {

    @Composable
    override fun Content() {
        val ordersState by cartViewModel.orders.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val sharedPreferences =context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val langCode = sharedPreferences.getString("langCode", "en")

        Column(
            modifier = Modifier
                .fillMaxSize().padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .systemBarsPadding()
            ) {
                IconButton(
                    onClick = { navigator.pop() },
                ) {
                    Icon(
                        imageVector = if (langCode == "ar") Icons.Default.KeyboardArrowRight else Icons.Default.KeyboardArrowLeft,
                        contentDescription = stringResource(id = R.string.back_to_home),
                        tint = kPrimaryColor,
                        modifier = Modifier.size(35.dp)
                    )
                }

                Text(
                    text = stringResource(id = R.string.orders),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.interregular))
                    ),
                    textAlign = TextAlign.Center
                )
            }

            if (ordersState?.data?.data.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.no_orders_found),
                        style = TextStyle(fontSize = 20.sp, color = Color.Gray),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(ordersState?.data?.data?.size ?: 0) { index ->
                        OrderCard(ordersState?.data?.data?.get(index)!!, cartViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: DataXXXXXXXXXXXXXXXXXX, cartViewModel: CartViewModel) {
    val navigator = LocalNavigator.currentOrThrow
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable {
                cartViewModel.orderDetails(order.id)
                navigator.push(OrderDetailsScreen(cartViewModel, order.id))
            },
        colors = CardDefaults.cardColors(containerColor = kPrimaryColor.copy(alpha = 0.1f)),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.order_id, order.id.toString()),
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.total_amount, order.total.toString()),
                style = TextStyle(fontSize = 16.sp)
            )
            Text(
                text = stringResource(id = R.string.order_date, order.date),
                style = TextStyle(fontSize = 14.sp)
            )
            Text(
                text = stringResource(id = R.string.order_status, order.status),
                style = TextStyle(fontSize = 14.sp, color = kPrimaryColor)
            )
        }
    }
}
