package com.example.shopna.presentation.view.home

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import com.example.shopna.R
import com.example.shopna.data.model.AddOrderRequest
import com.example.shopna.data.model.GetCartResponse
import com.example.shopna.presentation.view_model.CartViewModel
import com.example.shopna.presentation.view_model.FavoriteViewModel
import com.example.shopna.presentation.view_model.HomeViewModel
import com.example.shopna.ui.theme.backgroundColor
import com.example.shopna.ui.theme.greyColor
import com.example.shopna.ui.theme.kPrimaryColor
import java.util.Locale

@Composable
fun CartScreen(cartViewModel: CartViewModel, homeViewModel: HomeViewModel, favoriteViewModel: FavoriteViewModel) {

    cartViewModel.fetchCartData(true)

    val navigator = LocalNavigator.currentOrThrow
    val cartProducts by cartViewModel.cartData.collectAsState()
    val homeData = homeViewModel.products.collectAsState()
    val isLoading by cartViewModel.isLoading.collectAsState()
    val isUpdateCartLoading by cartViewModel.isUpdateCartLoading.collectAsState()

    val selectedIndex = remember { mutableIntStateOf(0) }
    var selectedQuantity by remember { mutableIntStateOf(0) }
    val promoCode = remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.my_cart),
                style = TextStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.interregular))
                )
            )

            CustomIcon(
                icon = painterResource(id = R.drawable.magnifyingglass),
                onClick = { }
            )
        }

        if (cartProducts?.data?.cart_items.isNullOrEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(id = R.string.empty_cart_message), color = greyColor, fontSize = 18.sp)
            }
        } else {
            Box(modifier = Modifier) {
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp)) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    val reversedItems = cartProducts?.data?.cart_items?.reversed()
                    cartProducts?.data?.cart_items?.let {
                        items(it.size) { index ->
                            val cartItem = it[index]
                            Box {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .shadow(elevation = 1.dp, RoundedCornerShape(12.dp))
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            homeData.value?.dataa?.products
                                                ?.find { product -> product.id == cartItem.product.id }
                                                ?.let { selectedProduct ->
                                                    navigator.push(
                                                        DetailsScreen(
                                                            selectedProduct,
                                                            favoriteViewModel,
                                                            cartViewModel,


                                                            )
                                                    )
                                                }
                                        },
                                    colors = CardDefaults.cardColors(containerColor = Color(0xfff8f8f8))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp)
                                            .padding(horizontal = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .height(90.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(color = Color.White)
                                        ) {
                                            AsyncImage(
                                                model = cartItem.product.image,
                                                contentDescription = cartItem.product.description,
                                                modifier = Modifier
                                                    .width(100.dp)
                                                    .height(90.dp)
                                                    .padding(8.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(color = Color.White),
                                                contentScale = ContentScale.Fit
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(5.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = cartItem.product.name,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                style = TextStyle(
                                                    fontSize = 13.sp,
                                                    fontFamily = FontFamily(Font(R.font.interregular)),
                                                    fontWeight = FontWeight.Bold,
                                                ),
                                            )

                                            Spacer(modifier = Modifier.height(5.dp))

                                            Text(
                                                text = cartItem.product.description,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis,
                                                style = TextStyle(
                                                    fontSize = 10.sp,
                                                    fontFamily = FontFamily(Font(R.font.interregular)),
                                                    color = Color(0xff868889)
                                                ),
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Row(
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(25.dp)
                                                            .clip(CircleShape)
                                                            .background(kPrimaryColor.copy(alpha = 0.2f))
                                                    ) {
                                                        Box(
                                                            modifier = Modifier
                                                                .width(6.dp)
                                                                .height(2.dp)
                                                                .align(Alignment.Center)
                                                                .background(Color.Black)
                                                                .clickable {
                                                                    selectedQuantity = index
                                                                    quantity = cartItem.quantity
                                                                    if (quantity > 1) {
                                                                        quantity--
                                                                        cartViewModel.updateCart(
                                                                            cartItem.id,
                                                                            quantity
                                                                        )
                                                                    }
                                                                }
                                                        )
                                                    }

                                                    Spacer(modifier = Modifier.width(8.dp))

                                                    if (isUpdateCartLoading && selectedQuantity == index) {
                                                        CircularProgressIndicator(
                                                            color = kPrimaryColor,
                                                            strokeWidth = 2.dp,
                                                            modifier = Modifier.size(10.dp)
                                                        )
                                                    } else {
                                                        Text(
                                                            text = cartItem.quantity.toString(),
                                                            fontSize = 15.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }

                                                    Spacer(modifier = Modifier.width(8.dp))

                                                    Box(
                                                        modifier = Modifier
                                                            .size(25.dp)
                                                            .clip(CircleShape)
                                                            .background(kPrimaryColor.copy(alpha = 0.2f))
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Add,
                                                            contentDescription = stringResource(id = R.string.add),
                                                            tint = Color.Black,
                                                            modifier = Modifier
                                                                .clickable {
                                                                    quantity = cartItem.quantity
                                                                    selectedQuantity = index
                                                                    quantity++
                                                                    cartViewModel.updateCart(
                                                                        cartItem.id,
                                                                        quantity
                                                                    )
                                                                }
                                                                .size(11.dp)
                                                                .align(Alignment.Center)
                                                        )
                                                    }
                                                }

                                                Column(verticalArrangement = Arrangement.Center) {
                                                    if (cartItem.product.old_price != cartItem.product.price) {
                                                        Text(
                                                            text = String.format(Locale.getDefault(), "%.1f", cartItem.product.old_price) + stringResource(id = R.string.currency_egp),
                                                            style = TextStyle(
                                                                color = greyColor,
                                                                fontSize = 11.sp,
                                                                fontFamily = FontFamily(Font(R.font.interregular)),
                                                                fontWeight = FontWeight.Bold,
                                                            ),
                                                            textDecoration = TextDecoration.LineThrough
                                                        )
                                                    }

                                                    Spacer(modifier = Modifier.width(8.dp))

                                                    Text(
                                                        text = String.format(Locale.getDefault(), "%.1f", cartItem.product.price) + stringResource(id = R.string.currency_egp),
                                                        style = TextStyle(
                                                            color = kPrimaryColor,
                                                            fontSize = 11.sp,
                                                            fontFamily = FontFamily(Font(R.font.interregular)),
                                                            fontWeight = FontWeight.Bold,
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                FavoriteIcon(
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp, vertical = 14.dp)
                                        .align(Alignment.TopStart),
                                    cartProducts = cartProducts,
                                    index = index,
                                    favoriteViewModel = favoriteViewModel,
                                    selectedIndex = selectedIndex
                                )

                                var inCart by remember { mutableStateOf(cartProducts!!.data.cart_items[index].product.in_cart) }
                                DeleteFromCart(
                                    modifier = Modifier
                                        .size(25.dp)
                                        .clip(CircleShape)
                                        .background(backgroundColor)
                                        .align(Alignment.TopEnd)
                                        .padding(5.dp),
                                    cartProducts = cartProducts,
                                    selectedIndex = selectedIndex,
                                    cartViewModel = cartViewModel,
                                    index = index,
                                    onClick = {
                                        cartViewModel.addOrDeleteCart(cartProducts!!.data.cart_items[index].product.id)
                                        inCart = !inCart
                                        cartProducts!!.data.cart_items[index].product.in_cart = inCart
                                        selectedIndex.value = cartProducts!!.data.cart_items[index].product.id
                                        cartViewModel.fetchCartData(true)
                                    }
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(LocalConfiguration.current.screenHeightDp.dp / 3))
                    }
                }

                CheckOutSection(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                        .height(
                            LocalConfiguration.current.screenHeightDp.dp / 4
                        )
                        .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                        .background(Color.White), promoCode = promoCode, cartProducts = cartProducts,cartViewModel)
            }
        }
    }
}



@Composable
fun FavoriteIcon(
    modifier: Modifier,
    cartProducts: GetCartResponse?,
    index: Int,
    favoriteViewModel: FavoriteViewModel,
    selectedIndex: MutableState<Int>
) {
    val favoriteIsLoading by favoriteViewModel.isLoading.collectAsState()
    Box(
        modifier = modifier
    ) {
        var isFavorite by remember { mutableStateOf(cartProducts!!.data.cart_items[index].product.in_favorites) }
        IconButton(
            onClick = {
                selectedIndex.value = index
                favoriteViewModel.addOrDeleteFavorite(cartProducts!!.data.cart_items[index].product.id)
                isFavorite = !isFavorite
                favoriteViewModel.fetchFavorites()
                cartProducts.data.cart_items[index].product.in_favorites = isFavorite
            },
            modifier = Modifier.size(22.dp)
        ) {
            if (favoriteIsLoading && selectedIndex.value == index)
                CircularProgressIndicator(
                    color = kPrimaryColor,
                    modifier = Modifier.size(13.dp)
                )
            else
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = stringResource(id = R.string.favorite),
                    tint = kPrimaryColor.copy(alpha = 0.8f)
                )
        }
    }
}

@Composable
fun DeleteFromCart(
    modifier: Modifier,
    cartProducts: GetCartResponse?,
    selectedIndex: MutableState<Int>,
    cartViewModel: CartViewModel,
    index: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        val isLoading by cartViewModel.isLoading.collectAsState()
        Box(
            modifier = Modifier
                .size(15.dp)
                .clip(CircleShape)
                .background(kPrimaryColor)
                .align(Alignment.Center)
        ) {
            IconButton(
                modifier = Modifier
                    .fillMaxSize()
                    .align(alignment = Alignment.Center),
                onClick = onClick,
            ) {
                if (isLoading && selectedIndex.value == cartProducts!!.data.cart_items[index].product.id) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(12.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "delete from cart",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}




@Composable
fun CheckOutSection(
    modifier: Modifier, promoCode: MutableState<String>,
    cartProducts: GetCartResponse?,
    cartViewModel: CartViewModel
) {
    val context = LocalContext.current
    val navigator = LocalNavigator.currentOrThrow
    val order= cartViewModel.addOrderResponse.collectAsState()
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, top = 35.dp)
        ) {
            PromoCodeField(
                promoCode = promoCode.value,
                onApplyClick = { /*TODO*/ },
                onPromoCodeChange = {
                    promoCode.value = it
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(5.dp),
                shape = RoundedCornerShape(5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row {

                        Text(
                            text = stringResource(id = R.string.total),
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color(0xff868889)
                            )
                        )
                        Text(
                            text = String.format(
                                Locale.getDefault(),
                                "%.1f",
                                cartProducts!!.data.total
                            ) + stringResource(id = R.string.currency_egp),
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = kPrimaryColor
                            )
                        )

                    }

                    Button(
                        onClick = {

                            cartViewModel.addOrder(AddOrderRequest(
                                address_id = 35,
                                payment_method =1 ,
                                use_points = false
                            ))

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = kPrimaryColor),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .width(LocalConfiguration.current.screenWidthDp.dp / 2)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.check_out),
                            fontSize = 12.sp
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun PromoCodeField(
    promoCode: String,
    onApplyClick: () -> Unit,
    onPromoCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val borderSize = 1.dp.toPx()
                val dashWidth = 10f
                val dashGap = 10f
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashWidth, dashGap))

                drawRoundRect(
                    color = Color.Gray,
                    size = size,
                    style = Stroke(width = borderSize, pathEffect = pathEffect),
                    cornerRadius = CornerRadius(10.dp.toPx())
                )
            }

            OutlinedTextField(
                value = promoCode,
                onValueChange = onPromoCodeChange,
                modifier = Modifier
                    .matchParentSize(),
                placeholder = { Text(stringResource(id = R.string.enter_promo_code), fontSize = 12.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                ),
                textStyle = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF9C6ADE))
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = { onApplyClick() },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp / 3)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEDE9FE))
        ) {
            Text(
                text = stringResource(id = R.string.applied),
                color = Color(0xFF4A4A4A),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}









