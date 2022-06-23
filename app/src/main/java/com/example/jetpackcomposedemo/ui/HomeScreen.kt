package com.example.jetpackcomposedemo.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcomposedemo.R
import com.example.jetpackcomposedemo.common.ViewPager
import com.example.jetpackcomposedemo.data.Flowers
import com.example.jetpackcomposedemo.data.FlowersData

@Preview
@Composable
fun HomeScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            SlidingBanner()
        }
        item {
            CategoryView()
        }
        item {
            Row(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Popular Items",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.helvetica_neue_bold))
                    ),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "View All",
                    style = MaterialTheme.typography.subtitle2.copy(color = colorPrimary)
                )
            }
        }
        item {
            PopularFlowersList()
        }
    }
}

@Composable
private fun SlidingBanner() {
    var currentIndex by rememberSaveable { mutableStateOf(0) }
    ViewPager(
        Modifier.fillMaxSize(),
        range = IntRange(0, 4),
        onPageChange = {
            Log.d("SlidingBanner", "currentPageIndex >> $it")
            currentIndex = it
        },
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            bitmap = ImageBitmap.imageResource(id = R.drawable.ic_sale_banner),
            contentScale = ContentScale.FillWidth,
            contentDescription = "sliding_banner_image"
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
                tint = tintColor,
                contentDescription = "page_indicator_icon"
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
        IconButton(onClick = { }, modifier = Modifier
            .align(Alignment.Center)
            .padding(14.dp)) {
            Image(
                bitmap = ImageBitmap.imageResource(id = icon),
                contentDescription = "rounded_corner_icon_button"
            )
        }
    }
}

@Composable
private fun PopularFlowersList() {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(FlowersData.list.size) {
            FlowerCard(FlowersData.list[it])
        }
    }
}

@Composable
private fun FlowerCard(flower: Flowers) {
    Card(
        shape = RoundedCornerShape(14.dp),
        backgroundColor = Color.White,
        modifier = Modifier
            .padding(10.dp)
            .width(180.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {

            Image(
                modifier = Modifier.size(140.dp),
                bitmap = ImageBitmap.imageResource(id = flower.image),
                contentDescription = "flower_card"
            )
            Row(modifier = Modifier.padding(top = 20.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = flower.name,
                        style = TextStyle(
                            color = gray,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.helvetica_neue_medium))
                        )
                    )
                    Text(
                        text = flower.price,
                        style = TextStyle(
                            color = colorPrimary,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.helvetica_neue_medium))
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
                    Icon(
                        Icons.Default.Add,
                        tint = Color.White,
                        modifier = Modifier.padding(10.dp),
                        contentDescription = "flower_card_icon"
                    )
                }
            }
        }
    }
}