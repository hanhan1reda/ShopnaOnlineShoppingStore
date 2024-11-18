package com.example.shopna.presentation.view.home

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import coil.compose.AsyncImage
import com.example.shopna.R
import com.example.shopna.data.model.DataXXXXXXXXX
import com.example.shopna.presentation.view_model.CartViewModel
import com.example.shopna.presentation.view_model.FavoriteViewModel
import com.example.shopna.presentation.view_model.HomeViewModel
import com.example.shopna.ui.theme.lightGreyColor

class SeeAllCategories(private val categories: List<DataXXXXXXXXX>, private val homeViewModel: HomeViewModel, private val favoriteViewModel: FavoriteViewModel, val cartViewModel: CartViewModel) : Screen {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val sharedPreferences =context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val langCode = sharedPreferences.getString("langCode", "en")
        val navigator = LocalNavigator.currentOrThrow
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .systemBarsPadding()
            ) {
                IconButton(
                    onClick = {
                        navigator.pop()
                    },
                ) {
                    Icon(
                        imageVector = if (langCode == "ar") Icons.Default.KeyboardArrowRight else Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Back to Home",
                        tint = Color.Black.copy(0.7f),
                        modifier = Modifier.size(35.dp)
                    )
                }

                Text(
                    text = stringResource(id = R.string.see_all_categories),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.interregular))
                    ),
                    textAlign = TextAlign.Center
                )
            }

            CategorySectionGrid(
                categories =categories ,
                homeViewModel = homeViewModel,
                favoriteViewModel =favoriteViewModel ,
                cartViewModel = cartViewModel
            )
        }

    }
}

@Composable
fun CategorySectionGrid(
    categories: List<DataXXXXXXXXX>,
    homeViewModel: HomeViewModel,
    favoriteViewModel: FavoriteViewModel,
    cartViewModel: CartViewModel
) {
    val navigator = LocalNavigator.currentOrThrow
    val categoriesPhotos = mutableListOf(
        R.drawable.chip,
        R.drawable.cor,
        R.drawable.sports,
        R.drawable.led,
        R.drawable.clothes,
        R.drawable.img
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories.size) { index ->
            Column(
                modifier = Modifier
                    .clickable {
                        homeViewModel.getProductsByCategory(categories[index].id)
                        navigator.push(
                            CategoryProductsScreen(
                                homeViewModel.categoryProducts,
                                categories[index].name,
                                favoriteViewModel,
                                homeViewModel,
                                cartViewModel
                            )
                        )
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = categoriesPhotos[index],
                    contentDescription = categories[index].name,
                    modifier = Modifier
                        .padding(5.dp)
                        .border(1.dp, lightGreyColor, RoundedCornerShape(10.dp))
                        .size(70.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = (categories[index].name.substring(0, 5) + ".."),
                    style = TextStyle(
                        fontSize = 14.sp,
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
