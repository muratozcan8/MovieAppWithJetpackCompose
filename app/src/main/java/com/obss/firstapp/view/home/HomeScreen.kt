package com.obss.firstapp.view.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.obss.firstapp.R

@Composable
fun HomeScreen(navController: NavController) {
    Text(text = "home screen")
}

@Preview(showBackground = true)
@Composable
fun MovieGridItem() {
    val backgroundColor = colorResource(id = R.color.gray_background)
    val cornerRadius = 10.dp
    val strokeWidth = dimensionResource(id = R.dimen.movie_grid_item_stroke_width)
    val cardElevation = 20.dp
    val imageModifier =
        Modifier
            .width(dimensionResource(id = R.dimen.movie_grid_item_width))
            .height(dimensionResource(id = R.dimen.movie_grid_item_height))

    Card(
        modifier =
            Modifier
                .width(dimensionResource(id = R.dimen.movie_grid_item_width))
                .wrapContentSize()
                .padding(
                    horizontal = 3.dp,
                    vertical = dimensionResource(id = R.dimen.movie_grid_item_margin_bottom),
                ),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(strokeWidth, Color.Gray),
    ) {
        Image(
            painter = painterResource(id = R.drawable.image_not_supported_24),
            contentDescription = "Movie Image",
            modifier = imageModifier,
            contentScale = ContentScale.Fit,
        )
        Text(
            text = "Movie Name",
            fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
            fontSize = dimensionResource(id = R.dimen.movie_grid_item_text_size).value.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .padding(top = 6.dp)
                    .padding(horizontal = dimensionResource(id = R.dimen.movie_grid_item_text_margin_horizontal))
                    .fillMaxWidth(),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(id = R.dimen.movie_grid_item_icon_margin_bottom)),
        ) {
            Image(
                painter = painterResource(id = R.drawable.star_24),
                contentDescription = "Movie Score",
                modifier =
                    Modifier
                        .size(dimensionResource(id = R.dimen.movie_linear_icon_size))
                        .padding(start = dimensionResource(id = R.dimen.movie_grid_item_margin_end)),
                colorFilter = ColorFilter.tint(Color(0xFFFFD700)), // Gold color
            )
            Text(
                text = "8.7",
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(id = R.dimen.movie_grid_item_text_size).value.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 4.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { /* Handle favorite button click */ },
                modifier =
                    Modifier
                        .size(dimensionResource(id = R.dimen.movie_linear_icon_size))
                        .padding(end = dimensionResource(id = R.dimen.movie_grid_item_margin_end)),
            ) {
                Icon(
                    painter = painterResource(id = if (true) R.drawable.favorite_24 else R.drawable.favorite_border_24),
                    contentDescription = "Favorite Icon",
                    tint = Color.White,
                )
            }
        }
    }
}
