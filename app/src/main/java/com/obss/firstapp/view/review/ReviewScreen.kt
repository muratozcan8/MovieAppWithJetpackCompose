package com.obss.firstapp.view.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.obss.firstapp.R
import com.obss.firstapp.data.model.review.ReviewResult
import com.obss.firstapp.utils.AlertDialogError
import com.obss.firstapp.utils.Constants.IMAGE_BASE_URL
import com.obss.firstapp.utils.ext.formatToReadableDate
import com.obss.firstapp.utils.ext.toAnnotatedString

@Composable
fun ReviewScreen(
    navController: NavController,
    movieId: Int,
    movieTitle: String,
    reviewViewModel: ReviewViewModel = hiltViewModel(),
) {
    reviewViewModel.getReviews(movieId)
    val reviews = reviewViewModel.reviewList.collectAsState()
    val errorMessage = reviewViewModel.errorMessage.collectAsState().value

    val isDialogVisible = remember { mutableStateOf(true) }

    val avgRating =
        if (reviews.value.isNotEmpty()) {
            reviews.value
                .map { it.authorDetails.rating * 10 }
                .average()
                .toInt()
                .toString()
        } else {
            "0"
        }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    brush =
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    Color(0xF27E002A),
                                    Color(0xFF040303),
                                ),
                        ),
                ),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TopBar(navController = navController, movieTitle = movieTitle, avgRating = avgRating.toInt())
            LazyColumn(
                modifier =
                    Modifier
                        .padding(vertical = 4.dp, horizontal = 16.dp)
                        .fillMaxWidth(),
            ) {
                items(reviews.value.size) { index ->
                    ReviewCard(review = reviews.value[index])
                }
            }
        }
    }
    if (errorMessage.isNotEmpty() && isDialogVisible.value) {
        AlertDialogError(
            onDismissRequest = { },
            onClose = { isDialogVisible.value = false },
            dialogTitle = "Error",
            dialogText = errorMessage,
            icon = painterResource(id = R.drawable.error_24),
        )
    }
}

@Composable
fun ReviewCard(review: ReviewResult) {
    var isExpanded = remember { mutableStateOf(false) }
    val maxLines = if (isExpanded.value) Int.MAX_VALUE else 10

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.review_card_margin)),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (review.authorDetails.avatarPath != null) {
                    AsyncImage(
                        model = "${IMAGE_BASE_URL}${review.authorDetails.avatarPath}",
                        contentDescription = "Author Image",
                        modifier =
                            Modifier
                                .size(60.dp)
                                .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_person_24),
                        contentDescription = "Author Image",
                        modifier =
                            Modifier
                                .size(60.dp)
                                .background(colorResource(id = R.color.gray), shape = CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = review.authorDetails.username,
                        color = Color.White,
                        fontSize = dimensionResource(id = R.dimen.review_text_size).value.sp,
                        fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.stars_24),
                            contentDescription = null,
                            tint = colorResource(id = R.color.gold),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${(review.authorDetails.rating * 10).toInt()}%",
                            color = Color.White,
                            fontSize = dimensionResource(id = R.dimen.review_text_size).value.sp,
                            fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = review.content.toAnnotatedString(),
                color = Color.White,
                maxLines = maxLines,
                fontSize = dimensionResource(id = R.dimen.review_text_size).value.sp,
                fontFamily = FontFamily(Font(R.font.ubuntu_light)),
                modifier = Modifier.padding(horizontal = 4.dp),
            )
            Spacer(modifier = Modifier.height(5.dp))
            if (review.content.length > 350 && !isExpanded.value) {
                Text(
                    text = stringResource(id = R.string.see_more),
                    color = colorResource(id = R.color.gray),
                    fontSize = dimensionResource(id = R.dimen.review_text_size).value.sp,
                    fontFamily = FontFamily(Font(R.font.ubuntu_light)),
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier
                            .padding(horizontal = 5.dp)
                            .align(Alignment.CenterHorizontally)
                            .clickable { isExpanded.value = !isExpanded.value },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            } else if (isExpanded.value) {
                Text(
                    text = stringResource(id = R.string.see_less),
                    color = colorResource(id = R.color.gray),
                    fontSize = dimensionResource(id = R.dimen.review_text_size).value.sp,
                    fontFamily = FontFamily(Font(R.font.ubuntu_light)),
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier
                            .padding(horizontal = 5.dp)
                            .align(Alignment.CenterHorizontally)
                            .clickable { isExpanded.value = !isExpanded.value },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = review.createdAt.formatToReadableDate(),
                color = Color.White,
                fontSize = dimensionResource(id = R.dimen.review_date_text_size).value.sp,
                fontFamily = FontFamily(Font(R.font.ubuntu_medium)),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                textAlign = TextAlign.End,
            )
        }
    }
}

@Composable
fun TopBar(
    navController: NavController,
    movieTitle: String,
    avgRating: Int,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = { onBackClick(navController) }) {
            Icon(
                painter = painterResource(id = R.drawable.back_24),
                contentDescription = null,
                tint = Color.White,
                modifier =
                    Modifier
                        .size(dimensionResource(id = R.dimen.top_bar_icon_size))
                        .background(color = Color.Gray, shape = CircleShape)
                        .padding(8.dp),
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = movieTitle,
            fontSize = dimensionResource(id = R.dimen.review_movie_name_text_size).value.sp,
            fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.wrapContentSize(),
        ) {
            CircularProgressIndicator(
                progress = { avgRating / 100f },
                modifier = Modifier.size(dimensionResource(id = R.dimen.review_indicator_size)),
                color =
                    if (avgRating > 80) {
                        Color.Green
                    } else if (avgRating > 60) {
                        Color.Yellow
                    } else {
                        Color.Red
                    },
                strokeWidth = 5.dp,
            )
            Text(
                text = avgRating.toString(),
                fontSize = dimensionResource(id = R.dimen.review_indicator_text_size).value.sp,
                fontFamily = FontFamily(Font(R.font.ubuntu_medium)),
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }
    }
}

fun onBackClick(navController: NavController) {
    navController.popBackStack()
}
