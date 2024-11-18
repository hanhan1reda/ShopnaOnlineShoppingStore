package com.example.shopna.presentation.view.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.rememberAsyncImagePainter
import com.example.shopna.R
import com.example.shopna.data.model.DataXXXXXXXXXXXXXXXXXXX
import com.example.shopna.data.model.ProductXXXXX
import com.example.shopna.presentation.view_model.CartViewModel
import com.example.shopna.ui.theme.kPrimaryColor

class OrderDetailsScreen(val cartViewModel: CartViewModel, private val orderId: Int) : Screen {
    @Composable
    override fun Content() {
        cartViewModel.orderDetails(orderId)
        val orderState by cartViewModel.orderDetails.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val sharedPreferences =context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val langCode = sharedPreferences.getString("langCode", "en")

        Column(
            modifier = Modifier
                .fillMaxSize()
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
                    text = stringResource(id = R.string.order_details),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.interregular))
                    ),
                    textAlign = TextAlign.Center
                )
            }

            if (orderState?.data == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.loading),
                        style = TextStyle(fontSize = 18.sp, color = Color.Gray)
                    )
                }
            } else {
                OrderSummary(orderState!!.data)
            }
        }
    }
}

@Composable
fun OrderSummary(order: DataXXXXXXXXXXXXXXXXXXX) {
    Column(Modifier.padding(16.dp)) {
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
            text = stringResource(id = R.string.discount_amount, order.discount.toString()),
            style = TextStyle(fontSize = 16.sp)
        )
        Text(
            text = stringResource(id = R.string.payment_method, order.payment_method),
            style = TextStyle(fontSize = 16.sp)
        )
        Text(
            text = stringResource(id = R.string.order_date, order.date),
            style = TextStyle(fontSize = 16.sp)
        )
        Text(
            text = stringResource(id = R.string.order_status, order.status),
            style = TextStyle(fontSize = 16.sp, color = kPrimaryColor)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.delivery_address),
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${order.address.name}, ${order.address.city}, ${order.address.region}",
            style = TextStyle(fontSize = 16.sp)
        )
        Text(text = stringResource(id = R.string.address_details, order.address.details), style = TextStyle(fontSize = 16.sp))
        Text(text = stringResource(id = R.string.notes, order.address.notes), style = TextStyle(fontSize = 16.sp))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.products),
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )
        LazyColumn {
            items(order.products) { product ->
                ProductCard(product)
            }
        }
    }
}

@Composable
fun ProductCard(product: ProductXXXXX) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = kPrimaryColor.copy(alpha = 0.4f)),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(product.image),
                contentDescription = product.name,
                modifier = Modifier.size(90.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = product.name,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
                Text(
                    text = stringResource(id = R.string.price, product.price.toString()),
                    style = TextStyle(fontSize = 14.sp)
                )
                Text(
                    text = stringResource(id = R.string.quantity, product.quantity.toString()),
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        }
    }
}
