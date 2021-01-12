package com.example.jetpackcomposedemo.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcomposedemo.R
import com.example.jetpackcomposedemo.common.ViewPager
import com.example.jetpackcomposedemo.data.Flowers
import com.example.jetpackcomposedemo.data.FlowersData

@Composable
fun HomeScreen() {
    ScrollableColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SlidingBanner()
        CategoryView()
        Row(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Popular Items",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = fontFamily(font(R.font.helvetica_neue_bold))
                ),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "View All",
                style = MaterialTheme.typography.subtitle2.copy(color = colorPrimary)
            )
        }
        PopularFlowersList()
    }
}

@Composable
private fun SlidingBanner() {
    var currentIndex by savedInstanceState { 0 }
    ViewPager(
        Modifier.fillMaxSize(),
        range = IntRange(0, 4),
        onPageChange = {
            Log.d("SlidingBanner", "currentPageIndex >> $it")
            currentIndex = it
        },
    ) {
        Image(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            bitmap = imageResource(id = R.drawable.ic_sale_banner),
            contentScale = ContentScale.FillWidth,
        )
    }
    PageIndicator(pagesCount = 3, currentPageIndex = currentIndex)
}

@Composable
private fun PageIndicator(
    modifier: Modifier = Modifier,
    pagesCount: Int,
    currentPageIndex: Int,
) {
    Log.d("PageIndicator", "currentPageIndex >> $currentPageIndex")

    Row(modifier = modifier.wrapContentSize(align = Alignment.Center)) {
        Log.d("PageIndicator >> ", "pagesCount >> $pagesCount")
        for (pageIndex in 0 until pagesCount) {
            val tintColor = if (currentPageIndex == pageIndex) {
                Color.DarkGray
            } else {
                Color.LightGray
            }
            Icon(
                Icons.Filled.FiberManualRecord,
                tint = tintColor
            )
        }
    }
}

@Composable
private fun CategoryView() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(20.dp)) {
        RoundedCornerIconButton(
            modifier = Modifier.weight(1f),
            R.drawable.ic_chinese_plum_flower
        )
        Spacer(modifier = Modifier.size(10.dp))
        RoundedCornerIconButton(
            modifier = Modifier.weight(1f),
            R.drawable.ic_flat_flower
        )
        Spacer(modifier = Modifier.size(10.dp))
        RoundedCornerIconButton(
            modifier = Modifier.weight(1f),
            R.drawable.ic_giftbox
        )
        Spacer(modifier = Modifier.size(10.dp))
        RoundedCornerIconButton(
            modifier = Modifier.weight(1f),
            R.drawable.ic_wedding_arch
        )
    }
}

@Composable
fun RoundedCornerIconButton(modifier: Modifier, icon: Int) {
    Box(
        modifier = modifier
            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
    ) {
        IconButton(onClick = { }, modifier = Modifier.align(Alignment.Center).padding(14.dp)) {
            Image(
                bitmap = imageResource(id = icon)
            )
        }
    }
}

@Composable
private fun PopularFlowersList() {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(FlowersData.list) { item ->
            FlowerCard(item)
        }
    }
}

@Composable
private fun FlowerCard(flower: Flowers) {
    Card(
        shape = RoundedCornerShape(14.dp),
        backgroundColor = Color.White,
        modifier = Modifier.padding(10.dp).width(180.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
        ) {

            Image(
                modifier = Modifier.size(140.dp),
                bitmap = imageResource(id = flower.image)
            )
            Row(modifier = Modifier.padding(top = 20.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = flower.name,
                        style = TextStyle(
                            color = gray,
                            fontSize = 16.sp,
                            fontFamily = fontFamily(font(R.font.helvetica_neue_medium))
                        )
                    )
                    Text(
                        text = flower.price,
                        style = TextStyle(
                            color = colorPrimary,
                            fontSize = 16.sp,
                            fontFamily = fontFamily(font(R.font.helvetica_neue_medium))
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .background(
                            color = colorPrimary,
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Icon(Icons.Default.Add, tint = Color.White, modifier = Modifier.padding(10.dp))
                }
            }
        }
    }
}