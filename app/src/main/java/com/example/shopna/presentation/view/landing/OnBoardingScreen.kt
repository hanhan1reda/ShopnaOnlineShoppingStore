package com.example.shopna.presentation.view.landing

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.shopna.OnBoardingData
import com.example.shopna.R
import com.example.shopna.presentation.view.authentication.LoginScreen
import com.example.shopna.presentation.view_model.AuthViewModel
import com.example.shopna.ui.theme.backgroundColor
import com.example.shopna.ui.theme.kPrimaryColor
import kotlinx.coroutines.launch

class OnBoardingScreen() : Screen{
    @Composable
    override fun Content() {
        Surface (modifier = Modifier.fillMaxSize(), color = backgroundColor){
            val items=ArrayList<OnBoardingData>()
            items.add(
                OnBoardingData(
                    R.raw.onee,
                    stringResource(id = R.string.onboarding_title1),
                    stringResource(id = R.string.onboarding_desc1)
                )
            )

            items.add(
                OnBoardingData(
                    R.raw.threee,
                    stringResource(id = R.string.onboarding_title2),
                    stringResource(id = R.string.onboarding_desc2)
                )
            )

            items.add(
                OnBoardingData(
                    R.raw.twoo,
                    stringResource(id = R.string.onboarding_title3),
                    stringResource(id = R.string.onboarding_desc3)
                )
            )

            val pagerState = rememberPagerState(
                pageCount = { items.size },
                initialPage = 0,

                )


            OnBoardingPager(
                item=items,
                pagerState=pagerState,
                modifier= Modifier.fillMaxSize(),
                context = LocalContext.current
            )




        }


    }
}

@Composable
fun <T> PageIndicator(items:List<T>,currentPage:Int,color:Color?=null){
    Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
        repeat(items.size){
            val width= animateDpAsState(targetValue =if( it==currentPage) 25.dp else 8.dp,
                label = "",
            )

            Box (modifier = Modifier
                .padding(3.dp)
                .height(5.dp)
                .width(width.value)
                .clip(CircleShape)
                .background(
                    if (it == currentPage) color ?: Color.White.copy(alpha = 0.7f) else Color.Gray
                ))
            {

            }
        }


    }
}

@Composable
fun OnBoardingPager(
    item: List<OnBoardingData>,
    pagerState: PagerState,
    modifier: Modifier,
    context: Context
) {
    val coroutineScope = rememberCoroutineScope()
    val navigator= LocalNavigator.currentOrThrow

    Box(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.systemBarsPadding()) {

            Text(
                text = stringResource(id = R.string.app_name),
                style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 16.sp, color = kPrimaryColor, fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 12.dp, vertical = 3.dp)
            )

            HorizontalPager(state = pagerState, modifier = Modifier.wrapContentSize()) { page ->
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(item[page].image))

                Column(
                    modifier = modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.size(LocalConfiguration.current.screenHeightDp.dp * 0.5f)
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(LocalConfiguration.current.screenHeightDp.dp * 0.5f),
                        colors = CardDefaults.cardColors(containerColor = kPrimaryColor),
                        shape = RoundedCornerShape(topStart = 80.dp)
                    ) {
                      LazyColumn {
                          item{
                              Column(
                                  modifier = Modifier
                                      .fillMaxSize()
                                      .padding(LocalConfiguration.current.screenWidthDp.dp*0.04f),
                                  horizontalAlignment = Alignment.CenterHorizontally,
                                  verticalArrangement = Arrangement.Center
                              ) {
                                  Spacer(modifier = Modifier.height(24.dp))

                                  Text(
                                      text = item[page].title,
                                      fontFamily = FontFamily.SansSerif,
                                      color = backgroundColor,
                                      fontSize = LocalConfiguration.current.fontScale.times(28).sp,
                                      fontWeight = FontWeight.Bold
                                  )
                                  Spacer(modifier = Modifier.height(10.dp))
                                  Text(
                                      text = item[page].description,
                                      modifier = Modifier
                                          .align(alignment = Alignment.CenterHorizontally)
                                          .padding(),
                                      textAlign = TextAlign.Center,
                                      fontWeight = FontWeight.Light,
                                      fontFamily = FontFamily.SansSerif,
                                      color = Color(0xffe0e0e0),
                                      fontSize = LocalConfiguration.current.fontScale.times(16).sp
                                  )
                                  Spacer(modifier = Modifier.height(20.dp))

                                  PageIndicator(items = item, currentPage = pagerState.currentPage)

                                  Spacer(modifier = Modifier.height(20.dp))

                                  Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                                      .fillMaxWidth()
                                      .padding(horizontal = 8.dp)) {
                                      TextButton(onClick = {
                                          val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                                          val editor = sharedPreferences.edit()
                                          editor.putBoolean("on_boarding_shown", true)
                                          editor.apply()
                                          navigator.push(LoginScreen(context = context))
                                      }){
                                          Text(text = stringResource(id = R.string.skip), style = TextStyle(
                                              color = Color.White,
                                              fontFamily = FontFamily.Monospace,
                                              fontSize = LocalConfiguration.current.fontScale.times(18).sp,
                                              fontWeight = FontWeight.Medium
                                          )
                                          )

                                      }
                                      Box{

                                          OutlinedButton(onClick = {
                                              coroutineScope.launch {
                                                  if(pagerState.currentPage==item.size-1){
                                                      val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                                                      val editor = sharedPreferences.edit()
                                                      editor.putBoolean("on_boarding_shown", true)
                                                      editor.apply()
                                                      navigator.push(LoginScreen(context=context))


                                                  }
                                                  else{
                                                      pagerState.animateScrollToPage(page = pagerState.currentPage +1)

                                                  }
                                              }
                                          } ,
                                              border = BorderStroke(
                                                  1.dp,
                                                  Color.White.copy(alpha = 0.6f)
                                              ),
                                              shape = RoundedCornerShape(if(pagerState.currentPage==item.size-1) LocalConfiguration.current.screenWidthDp.dp * 0.05f else  LocalConfiguration.current.screenWidthDp.dp * 0.06f),
                                              colors = ButtonDefaults.outlinedButtonColors(

                                                  contentColor = kPrimaryColor
                                              ),
                                              modifier =  if(pagerState.currentPage==item.size-1) Modifier.width(LocalConfiguration.current.screenWidthDp.dp * 0.4f)else Modifier.size(LocalConfiguration.current.screenWidthDp.dp * 0.12f)


                                          )  {


                                          }

                                          if(pagerState.currentPage==item.size-1){
                                              Text(text = stringResource(id = R.string.getStarted),
                                                  modifier = Modifier.align(Alignment.Center),
                                                  fontSize = LocalConfiguration.current.fontScale.times(18).sp,
                                                  color = Color.White,
                                                  fontWeight = FontWeight.Medium,
                                                  fontFamily = FontFamily.Serif)



                                          }
                                          else{
                                              Icon(
                                                  imageVector = Icons.Filled.ArrowForward ,
                                                  contentDescription ="next icon",
                                                  tint = Color.White.copy(alpha = 0.6f),
                                                  modifier = Modifier
                                                      .size(LocalConfiguration.current.screenWidthDp.dp * 0.06f)
                                                      .align(Alignment.Center)


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
    }
}
