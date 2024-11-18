package com.example.shopna.presentation.view.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.shopna.ui.theme.kPrimaryColor
import com.example.shopna.ui.theme.lightGreyColor
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.shopna.R
import com.example.shopna.presentation.view_model.AuthViewModel
import com.example.shopna.presentation.view_model.CartViewModel
import com.example.shopna.presentation.view_model.FavoriteViewModel
import com.example.shopna.ui.theme.backgroundColor


class MainScreen(private val authViewModel: AuthViewModel) :Screen {
    @Composable
    override fun Content() {
        val navigator=LocalNavigator.currentOrThrow
        val context= LocalContext.current
        var selectedIndex by remember {mutableIntStateOf(0)}
        val favoriteViewModel: FavoriteViewModel = remember {
            FavoriteViewModel(navigator,context)
        }
        val cartViewModel: CartViewModel = remember {
            CartViewModel(navigator,context)
        }
        Scaffold(
            containerColor = backgroundColor,
            content ={
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(it)) {

                    when (selectedIndex) {
                        0 -> {

                           return@Column HomeScreen(authViewModel.homeViewModel,favoriteViewModel,user=authViewModel.user,cartViewModel=cartViewModel)
                        }
                        1 -> {


                            return@Column FavoriteScreen(favoriteViewModel,authViewModel.homeViewModel,cartViewModel)
                        }
                        2 -> {

                            return@Column CartScreen(cartViewModel=cartViewModel,homeViewModel=authViewModel.homeViewModel,favoriteViewModel=favoriteViewModel)
                        }
                        3 -> {

                            return@Column ProfileScreen(authViewModel,cartViewModel)
                        }
                    }





                }
            } ,
            bottomBar = {  NavigationBar(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .wrapContentHeight(),
                containerColor = Color.White,

                ) {
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = lightGreyColor,
                        selectedIconColor = Color.White,
                        indicatorColor = Color.White,


                    ),

                    label = {
                        if(selectedIndex==0) HorizontalDivider(color = kPrimaryColor, thickness = 4.dp, modifier = Modifier.width(30.dp))
                    },


                    selected =false ,
                    onClick = { selectedIndex=0
                        println("Categories data:=================================================== ${authViewModel.homeViewModel.categories.value}")



                    },
                    icon = {
                        Image(painter = painterResource(id = R.drawable.homebutton), contentDescription ="" )
                    }
                )
                NavigationBarItem(
                    selected = false,
                    label = {
                        if(selectedIndex==1) HorizontalDivider(color = kPrimaryColor, thickness = 4.dp, modifier = Modifier.width(30.dp))
                    },
                    onClick = {
                        selectedIndex=1


                              },
                    icon = {

                            Image(painter = painterResource(id = R.drawable.heart), contentDescription ="" )

                    }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { selectedIndex=2},
                    label = {
                        if(selectedIndex==2) HorizontalDivider(color = kPrimaryColor, thickness = 4.dp, modifier = Modifier.width(30.dp))
                    },
                    icon = {

                        Image(painter = painterResource(id = R.drawable.trolley), contentDescription ="" )

                    }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { selectedIndex=3 },
                    label = {
                        if(selectedIndex==3) HorizontalDivider(color = kPrimaryColor, thickness = 4.dp, modifier = Modifier.width(30.dp))
                    },
                    icon = {

                        Image(painter = painterResource(id = R.drawable.user), contentDescription ="" )

                    }
                )
            } }

        )


    }
}










