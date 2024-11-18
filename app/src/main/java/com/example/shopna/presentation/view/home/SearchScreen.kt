package com.example.shopna.presentation.view.home

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.shopna.R
import com.example.shopna.data.model.Products
import com.example.shopna.presentation.view_model.CartViewModel
import com.example.shopna.presentation.view_model.FavoriteViewModel
import com.example.shopna.presentation.view_model.HomeViewModel
import com.example.shopna.ui.theme.kPrimaryColor

class SearchScreen(
    val products: List<Products>?,
    private val homeViewModel: HomeViewModel,
    private val favoriteViewModel: FavoriteViewModel,
    private val cartViewModel: CartViewModel
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val sharedPreferences =context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val langCode = sharedPreferences.getString("langCode", "en")

        var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

        val filteredProducts = remember(searchQuery.text) {
            products?.filter { product ->
                product.name?.contains(searchQuery.text, ignoreCase = true) == true
            } ?: listOf()
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .systemBarsPadding()
                ) {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(
                            imageVector = if (langCode == "ar") Icons.Default.KeyboardArrowRight else Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color.Black.copy(0.7f),
                            modifier = Modifier.size(35.dp)
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.search),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f),
                    )
                }

                SearchBar(searchQuery) { query ->
                    searchQuery = query
                }

                Spacer(modifier = Modifier.height(5.dp))




                if (homeViewModel.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = kPrimaryColor)
                    }
                } else {
                    if (filteredProducts.isNotEmpty()) {
                        ProductGrid(
                            filteredProducts,
                            favoriteViewModel,
                            Modifier
                                .fillMaxWidth()
                                .height(LocalConfiguration.current.screenHeightDp.dp),
                            cartViewModel
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 50.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Text("No products found", fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    searchQuery: TextFieldValue,
    onSearchChanged: (TextFieldValue) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.LightGray.copy(alpha = 0.2f), MaterialTheme.shapes.medium)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            BasicTextField(
                value = searchQuery,
                onValueChange = onSearchChanged,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions.Default,
                modifier = Modifier.weight(1f),
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            CustomIcon(painterResource(id = R.drawable.magnifyingglass),{})
        }
    }
}