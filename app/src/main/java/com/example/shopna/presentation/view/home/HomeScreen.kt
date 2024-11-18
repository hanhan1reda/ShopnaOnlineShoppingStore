package com.example.shopna.presentation.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.shopna.presentation.view_model.HomeViewModel
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.shopna.R
import com.example.shopna.data.model.Banners
import com.example.shopna.data.model.DataXXXXXXXXX
import com.example.shopna.data.model.Products
import com.example.shopna.data.model.GetUserResponse
import com.example.shopna.presentation.view_model.CartViewModel
import com.example.shopna.presentation.view_model.FavoriteViewModel
import com.example.shopna.ui.theme.greyColor
import com.example.shopna.ui.theme.kPrimaryColor
import com.example.shopna.ui.theme.lightGreyColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
@Composable
  fun HomeScreen(
    homeViewModel: HomeViewModel,
     favoriteViewModel: FavoriteViewModel,
     cartViewModel: CartViewModel,
     user: StateFlow<GetUserResponse?>
) {



        homeViewModel.getHomeData()
        homeViewModel.getCategories()
        val homeData by homeViewModel.products.collectAsState()
        val categories by homeViewModel.categories.collectAsState()
        val navigator= LocalNavigator.currentOrThrow
        val userData =user.collectAsState()








        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            item {
                Column (
                    modifier = Modifier
                        .fillMaxSize()

                ){

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text(text = stringResource(id = R.string.welcome_back)+"!", style = TextStyle(
                                fontSize = 18.sp,
                                color = greyColor,
                                fontFamily = FontFamily(Font(R.font.interregular)),
                                fontWeight = FontWeight.W200
                            ))
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(text = userData.value?.data?.name?:"", style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.interregular)),
                                fontWeight = FontWeight.Bold

                            ))
                        }
                        Row {
                            CustomIcon(icon =painterResource(id = R.drawable.magnifyingglass) , onClick = {
                                navigator.push(SearchScreen(homeData?.dataa?.products,homeViewModel,favoriteViewModel,cartViewModel))
                            })
                            Spacer(modifier = Modifier.width(10.dp))
                            CustomIcon(icon = painterResource(id = R.drawable.notification) , onClick = {})

                        }




                    }
                    Spacer(modifier = Modifier.height(15.dp))

                    homeData?.dataa?.banners?.let { AutoScrollingBannerSection(banners = it) }
                }
                Spacer(modifier = Modifier.height(4.dp))

                CustomText(text = stringResource(id = R.string.categories), clickable = {
                    navigator.push(SeeAllCategories(categories?.data?.data!!,homeViewModel,favoriteViewModel,cartViewModel))
                })
                Spacer(modifier = Modifier.height(4.dp))
                if (homeViewModel.categories == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = kPrimaryColor)
                    }
                }else{ categories?.data?.data?.let { categoriesData ->
                    CategorySection(categoriesData,homeViewModel,favoriteViewModel,cartViewModel)
                }}
                Spacer(modifier = Modifier.height(8.dp))

            }

            item{
                CustomText(text = stringResource(id = R.string.recommendations), clickable = {

                    homeData?.dataa?.products?.let { products ->
                        navigator.push(SeeAllProducts(products,homeViewModel,favoriteViewModel,cartViewModel))
                    }

                })



                Spacer(modifier = Modifier.height(4.dp))

                if (homeViewModel.isLoading || homeData==null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = kPrimaryColor)
                    }
                }else{ homeData?.dataa?.products?.let { products ->
                    ProductGrid(products, favoriteViewModel, Modifier
                        .fillMaxWidth()
                        .height(LocalConfiguration.current.screenHeightDp.dp/2),cartViewModel)
                }}
            }
        }


}

@Composable
fun CashBackCard(){
    Box {
        Card (modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .wrapContentHeight(),
            colors = CardDefaults.cardColors(kPrimaryColor)
        )
        {

            Column (  modifier = Modifier.padding(16.dp)){
                Text(
                    stringResource(id = R.string.new_collection),
                    style = TextStyle(color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.interregular)),
                        fontWeight = FontWeight.Bold),


                    )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    stringResource(id = R.string.discount_offer),
                    style = TextStyle(color = Color.White.copy(alpha = 0.6f),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.interregular)),
                        fontWeight = FontWeight.W100),
                    lineHeight = 18.sp

                )
                Box(modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = 10.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(Color.White)) {
                    Text(
                        stringResource(id = R.string.shop_now),modifier = Modifier
                            .align(Alignment.Center)
                            .padding(8.dp),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily =  FontFamily(Font(R.font.interregular)),
                            color = Color.Black

                        ))
                }


            }


        }
        Box(modifier = Modifier.align(Alignment.BottomEnd))   {

            Image(
                painter = painterResource(id = R.drawable.headset),
                contentDescription ="",
                modifier = Modifier.size(130.dp) ,
                alignment = Alignment.CenterEnd
            )
        }

    }
}


@Composable
private fun CustomText(text:String,clickable:()->Unit={}) {
    Row (modifier =
    Modifier
        .padding(vertical = 8.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text,
            style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.interregular)),
                fontWeight = FontWeight.Bold
            ),
        )
        Text(
            stringResource(id = R.string.see_all),
            modifier = Modifier.clickable {

                clickable()
            },
            style = TextStyle(
                color = kPrimaryColor,
                fontSize = 13.sp,
                fontFamily = FontFamily(Font(R.font.interregular)),
                fontWeight = FontWeight.W400,
                textAlign = TextAlign.Center
            ),
        )

    }

}

@Composable
 fun CustomIcon(icon: Painter,onClick:()->Unit) {
    Box(modifier = Modifier
        .size(35.dp)
        .clip(CircleShape)
        .background(lightGreyColor.copy(alpha = 0.4f))) {
        IconButton(onClick = onClick) {
            Icon(
                painter = icon,
                contentDescription = "",
                tint = Color.Black,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(20.dp),

            )
        }


    }
}







@Composable
fun CategorySection(
    categories: List<DataXXXXXXXXX>,
    homeViewModel: HomeViewModel,
    favoriteViewModel: FavoriteViewModel,
    cartViewModel: CartViewModel) {
    val navigator = LocalNavigator.currentOrThrow
    val categoriesPhotos= mutableListOf(
        R.drawable.chip
        ,R.drawable.cor
        ,R.drawable.sports
        ,R.drawable.led
        ,R.drawable.clothes
        ,R.drawable.img
    )


        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
           items(categories.size) { index ->
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable {
                            homeViewModel.getProductsByCategory(categories[index].id)
                            navigator.push(CategoryProductsScreen(homeViewModel.categoryProducts,categories[index].name,favoriteViewModel,homeViewModel, cartViewModel ))
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = categoriesPhotos[index],
                        contentDescription = categories[index].name,
                        modifier = Modifier
                            .padding(5.dp)
                            .border(1.dp, lightGreyColor, RoundedCornerShape(10.dp))
                            .size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = (categories[index].name.substring(0, 5) + ".."),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.interregular)),
                            fontWeight = FontWeight.W400,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

}

@Composable
fun ProductGrid(products: List<Products>, favoriteViewModel: FavoriteViewModel,modifier: Modifier=Modifier,cartViewModel: CartViewModel) {
    LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            modifier = modifier,
        ) {
            items(products) { product ->
                ProductItem(product, favoriteViewModel,products.indexOf(product),cartViewModel )
            }
        }

}



@Composable
fun ProductItem(product: Products, favoriteViewModel: FavoriteViewModel, indexOf: Int,cartViewModel: CartViewModel) {
    val navigator = LocalNavigator.currentOrThrow
    var isFavorite by remember { mutableStateOf(product.inFavorites) }
    var inCart by remember { mutableStateOf(product.inCart) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    val isLoading by cartViewModel.isLoading.collectAsState()
    val favoriteIsLoading by favoriteViewModel.isLoading.collectAsState()

    Box(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Card(
            modifier = Modifier
                .height(195.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { navigator.push(DetailsScreen(
                    product,
                    favoriteViewModel,
                    cartViewModel,
                )) },
            colors = CardDefaults.cardColors(containerColor = Color.White)

        ) {
            AsyncImage(
                model = product.image,
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(14.dp),
                contentScale = ContentScale.Fit
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = product.name ?: "",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontFamily = FontFamily(Font(R.font.interregular)),
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "${product.price} "+ stringResource(id = R.string.currency_egp),
                        style = TextStyle(
                            color = kPrimaryColor,
                            fontSize = 10.sp,
                            fontFamily = FontFamily(Font(R.font.interregular)),
                            fontWeight = FontWeight.W400,
                        ),
                    )

                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(kPrimaryColor.copy(alpha = 0.4f))
                    ) {
                        IconButton(
                            onClick = {

                                selectedIndex = indexOf
                                cartViewModel.addOrDeleteCart(product.id!!)
                                inCart = !inCart!!
                                cartViewModel.fetchCartData(true)
                                product.inCart = inCart
                            },
                        ) {
                            if (isLoading && selectedIndex == indexOf)
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(13.dp)
                                ) else Icon(
                                imageVector = if (inCart == true) Icons.Default.Check else Icons.Outlined.Add,
                                contentDescription = "Add To Cart",
                                tint = Color.White
                            )
                        }
                    }
                }

                if (product.oldPrice != null && product.oldPrice != product.price) {
                    Column {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "${product.oldPrice} "+stringResource(id = R.string.currency_egp),
                            style = TextStyle(
                                color = kPrimaryColor,
                                fontSize = 10.sp,
                                fontFamily = FontFamily(Font(R.font.interregular)),
                                fontWeight = FontWeight.W400,
                            ),
                            color = greyColor,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
            }
        }

        if (product.discount != 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(kPrimaryColor.copy(alpha = 0.6f))
                    .padding(horizontal = 5.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "${product.discount}"+stringResource(id = R.string.percent_off),
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Box(
            modifier = Modifier
                .size(30.dp)
                .padding(5.dp)
                .align(Alignment.TopStart)) {
            IconButton(
                onClick = {
                    selectedIndex = indexOf
                    favoriteViewModel.addOrDeleteFavorite(product.id!!)
                    isFavorite = !isFavorite!!
                    favoriteViewModel.fetchFavorites()
                    product.inFavorites = isFavorite


                },
                modifier = Modifier.size(18.dp)
            ) {
                if (favoriteIsLoading&& selectedIndex == indexOf)
                    CircularProgressIndicator(
                        color = kPrimaryColor,
                        modifier = Modifier.size(13.dp)
                    ) else Icon(
                    imageVector = if (isFavorite == true) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = kPrimaryColor.copy(alpha = 0.6f)
                )
            }
        }

    }


}





@Composable
fun AutoScrollingBannerSection(banners: List<Banners>) {
    val bannerCount = banners.size
    val pagerState = rememberPagerState(
        pageCount = { bannerCount },
        initialPage = 0,

        )
    LaunchedEffect(pagerState.currentPage) {
        while (true) {
            delay(4000)
            val nextPage = if(pagerState.currentPage+1 == pagerState.pageCount) 0 else pagerState.currentPage + 1
            pagerState.scrollToPage(nextPage)
        }
    }





    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//        Spacer(modifier = Modifier.height(36.dp))
        HorizontalPager(
            state =pagerState ,
            modifier = Modifier
                .wrapContentSize()
        ) {index->
            AsyncImage(
                model = banners[index].image,
                contentDescription = "banner $index",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(LocalConfiguration.current.screenHeightDp.dp / 3)
            )


        }

//         PageIndicator(items = banners, currentPage = pagerState.currentPage, kPrimaryColor)
    }
}
//Box {
//    Card (modifier = Modifier
//        .fillMaxWidth()
//        .clip(RoundedCornerShape(12.dp))
//        .wrapContentHeight(),
//        colors = CardDefaults.cardColors(kPrimaryColor)
//    )
//    {
//
//        Column (  modifier = Modifier.padding(16.dp)){
//            Text(
//                stringResource(id = R.string.new_collection),
//                style = TextStyle(color = Color.White,
//                    fontSize = 20.sp,
//                    fontFamily = FontFamily(Font(R.font.interregular)),
//                    fontWeight = FontWeight.Bold),
//
//
//                )
//            Spacer(modifier = Modifier.height(3.dp))
//            Text(
//                stringResource(id = R.string.discount_offer),
//                style = TextStyle(color = Color.White.copy(alpha = 0.6f),
//                    fontSize = 14.sp,
//                    fontFamily = FontFamily(Font(R.font.interregular)),
//                    fontWeight = FontWeight.W100),
//                lineHeight = 18.sp
//
//            )
//            Box(modifier = Modifier
//                .wrapContentSize()
//                .padding(vertical = 10.dp)
//                .clip(shape = RoundedCornerShape(8.dp))
//                .background(Color.White)) {
//                Text(
//                    stringResource(id = R.string.shop_now),modifier = Modifier
//                        .align(Alignment.Center)
//                        .padding(8.dp),
//                    style = TextStyle(
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Bold,
//                        fontFamily =  FontFamily(Font(R.font.interregular)),
//                        color = Color.Black
//
//                    ))
//            }
//
//
//        }
//
//
//    }
//    Box(modifier = Modifier.align(Alignment.BottomEnd))   {
//
//        Image(
//            painter = painterResource(id = R.drawable.headset),
//            contentDescription ="",
//            modifier = Modifier.size(130.dp) ,
//            alignment = Alignment.CenterEnd
//        )
//    }
//
//}
