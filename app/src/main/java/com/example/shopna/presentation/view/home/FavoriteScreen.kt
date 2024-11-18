package com.example.shopna.presentation.view.home

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.example.shopna.presentation.view_model.CartViewModel
import com.example.shopna.presentation.view_model.FavoriteViewModel
import com.example.shopna.presentation.view_model.HomeViewModel
import com.example.shopna.ui.theme.greyColor
import com.example.shopna.ui.theme.kPrimaryColor

@Composable
fun FavoriteScreen(favoriteViewModel: FavoriteViewModel, homeViewModel: HomeViewModel, cartViewModel: CartViewModel) {
    favoriteViewModel.fetchFavorites()
    val navigator = LocalNavigator.currentOrThrow
    val favoriteProducts by favoriteViewModel.favoriteData.collectAsState()
    val homeData = homeViewModel.products.collectAsState()
    var selectedIndex by remember { mutableIntStateOf(0) }
    val favoriteIsLoading by favoriteViewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.favorite),
                style = TextStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.interregular))
                )
            )

            CustomIcon(
                icon = painterResource(id = R.drawable.magnifyingglass),
                onClick = { /*TODO*/ }
            )
        }
        if (favoriteProducts?.data?.data.isNullOrEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(id = R.string.no_favorites), color = greyColor, fontSize = 18.sp)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                favoriteProducts?.data?.data?.let {
                    items(it) { favoriteProduct ->
                        Box {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .shadow(elevation = 1.dp, RoundedCornerShape(12.dp))
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        homeData.value?.dataa?.products?.find { it.id == favoriteProduct.product.id }
                                            ?.let { it1 -> navigator.push(DetailsScreen(it1, favoriteViewModel, cartViewModel)) }
                                    },
                                colors = CardDefaults.cardColors(containerColor = Color(0xfff8f8f8))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(color = Color.White)
                                    ) {
                                        AsyncImage(
                                            model = favoriteProduct.product.image,
                                            contentDescription = favoriteProduct.product.description,
                                            modifier = Modifier
                                                .size(120.dp)
                                                .padding(8.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(color = Color.White),
                                            contentScale = ContentScale.Fit
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(5.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = favoriteProduct.product.name,
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
                                            text = favoriteProduct.product.description,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            style = TextStyle(
                                                fontSize = 10.sp,
                                                fontFamily = FontFamily(Font(R.font.interregular)),
                                                color = Color(0xff868889)
                                            ),
                                        )

                                        Spacer(modifier = Modifier.height(5.dp))

                                        Text(
                                            text = "${favoriteProduct.product.price} " + stringResource(id = R.string.currency_egp),
                                            style = TextStyle(
                                                color = kPrimaryColor,
                                                fontSize = 12.sp,
                                                fontFamily = FontFamily(Font(R.font.interregular)),
                                                fontWeight = FontWeight.Bold,
                                            ),
                                        )

                                        if (favoriteProduct.product.old_price != favoriteProduct.product.price) {
                                            Column {
                                                Spacer(modifier = Modifier.height(3.dp))
                                                Text(
                                                    text = "${favoriteProduct.product.old_price} " + stringResource(id = R.string.currency_egp),
                                                    style = TextStyle(
                                                        color = greyColor,
                                                        fontSize = 12.sp,
                                                        fontFamily = FontFamily(Font(R.font.interregular)),
                                                        fontWeight = FontWeight.Bold,
                                                    ),
                                                    textDecoration = TextDecoration.LineThrough
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 5.dp, vertical = 12.dp)
                                    .align(Alignment.TopStart)
                            ) {
                                var isFavorite by remember {
                                    mutableStateOf(homeData.value?.dataa?.products?.find { it.id == favoriteProduct.product.id }?.inFavorites)
                                }
                                IconButton(
                                    onClick = {
                                        favoriteViewModel.addOrDeleteFavorite(favoriteProduct.product.id)
                                        isFavorite = !isFavorite!!
                                        homeData.value?.dataa?.products?.find { it.id == favoriteProduct.product.id }?.inFavorites = isFavorite
                                        selectedIndex = favoriteProduct.product.id
                                        favoriteViewModel.fetchFavorites()
                                    },
                                    modifier = Modifier.size(22.dp)
                                ) {
                                    if (favoriteIsLoading && selectedIndex == favoriteProduct.product.id) {
                                        CircularProgressIndicator(
                                            color = kPrimaryColor,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            contentDescription = "Favorite",
                                            tint = kPrimaryColor.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}




