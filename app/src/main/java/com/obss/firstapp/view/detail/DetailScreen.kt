package com.obss.firstapp.view.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.HorizontalPager
import com.obss.firstapp.R
import com.obss.firstapp.utils.Constants.IMAGE_BASE_URL
import com.obss.firstapp.utils.ext.roundToSingleDecimal

@Composable
fun DetailScreen(
    navController: NavController,
    movieId: Int,
    homeViewModel: DetailViewModel = hiltViewModel(),
) {
    homeViewModel.getMovieDetails(movieId)
    homeViewModel.getMovieImages(movieId)
    homeViewModel.getMovieCasts(movieId)
    homeViewModel.getRecommendationMovies(movieId)
    homeViewModel.getReviews(movieId)
    val movie = homeViewModel.movie.collectAsState().value
    val movieImages = homeViewModel.movieImages.collectAsState().value
    val movieCasts = homeViewModel.movieCasts.collectAsState().value
    val recommendationMovies = homeViewModel.recommendationMovies.collectAsState().value
    val reviews = homeViewModel.reviews.collectAsState().value
    val favoriteMovie = homeViewModel.favoriteMovie.collectAsState().value
    val isLoadings = homeViewModel.isLoadings.collectAsState().value
    val videos = homeViewModel.videos.collectAsState().value
    val errorMessage = homeViewModel.errorMessage.collectAsState().value

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
        when (isLoadings) {
            true -> {
                CircularProgressIndicator()
            }
            false -> {
                Column {
                    Column(
                        modifier =
                            Modifier
                                .verticalScroll(rememberScrollState()),
                    ) {
                        HorizontalPager(
                            count = 5,
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1.77f),
                        ) { page ->
                            if (movieImages.isNotEmpty()) {
                                AsyncImage(
                                    model = "${IMAGE_BASE_URL}${movieImages[page].filePath}",
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }
                        }

                        Box(
                            modifier =
                                Modifier
                                    .height(125.dp)
                                    .fillMaxWidth()
                                    .background(brush = Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black))),
                        )

                        Box(
                            modifier =
                                Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                IconButton(onClick = { onBackClick() }) {
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

                                Spacer(modifier = Modifier.weight(1f))

                                IconButton(onClick = { onFavClick(movieId) }) {
                                    if (movie != null) {
                                        Icon(
                                            painter =
                                                painterResource(
                                                    id = if (movie.isFavorite) R.drawable.favorite_24 else R.drawable.favorite_border_24,
                                                ),
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier =
                                                Modifier
                                                    .size(dimensionResource(id = R.dimen.top_bar_icon_size))
                                                    .background(color = Color.Gray, shape = CircleShape)
                                                    .padding(8.dp),
                                        )
                                    }
                                }
                            }
                        }

                        Text(
                            text = movie?.title ?: "",
                            style =
                                TextStyle(
                                    color = Color.White,
                                    fontSize = dimensionResource(id = R.dimen.movie_name_text_size).value.sp,
                                    fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
                                ),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        )

                        Row(
                            modifier =
                                Modifier
                                    .padding(horizontal = 24.dp)
                                    .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = movie?.voteAverage?.roundToSingleDecimal().toString(),
                                style =
                                    TextStyle(
                                        color = Color.White,
                                        fontSize = dimensionResource(id = R.dimen.movie_info_text_size).value.sp,
                                        fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
                                    ),
                                modifier =
                                    Modifier
                                        .background(color = Color.Gray, shape = RoundedCornerShape(4.dp))
                                        .padding(8.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = movie?.runtime.toString(),
                                style =
                                    TextStyle(
                                        color = Color.White,
                                        fontSize = dimensionResource(id = R.dimen.movie_info_text_size).value.sp,
                                        fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
                                    ),
                                modifier =
                                    Modifier
                                        .background(color = Color.Gray, shape = RoundedCornerShape(4.dp))
                                        .padding(8.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            movie?.releaseDate?.take(4)?.let {
                                Text(
                                    text = it,
                                    style =
                                        TextStyle(
                                            color = Color.White,
                                            fontSize = dimensionResource(id = R.dimen.movie_info_text_size).value.sp,
                                            fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
                                        ),
                                    modifier =
                                        Modifier
                                            .background(color = Color.Gray, shape = RoundedCornerShape(4.dp))
                                            .padding(8.dp),
                                )
                            }
                        }

                        // Category RecyclerView equivalent in Compose
                        LazyRow(
                            modifier =
                                Modifier
                                    .padding(vertical = 16.dp)
                                    .fillMaxWidth(),
                        ) {
                            items(listOf("Category1", "Category2", "Category3")) { category ->
                                Text(
                                    text = category,
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    color = Color.White,
                                )
                            }
                        }

                        Text(
                            text = "Summary",
                            style =
                                TextStyle(
                                    color = Color.Gray,
                                    fontSize = dimensionResource(id = R.dimen.movie_detail_title).value.sp,
                                    fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
                                ),
                            modifier = Modifier.padding(horizontal = 24.dp),
                        )

                        Text(
                            text = movie?.overview ?: "",
                            style =
                                TextStyle(
                                    color = Color.White,
                                    fontSize = dimensionResource(id = R.dimen.movie_detail_text_size).value.sp,
                                    fontFamily = FontFamily(Font(R.font.ubuntu_light)),
                                ),
                            modifier = Modifier.padding(horizontal = 24.dp),
                        )

                        LazyRow(
                            modifier =
                                Modifier
                                    .padding(vertical = 16.dp)
                                    .fillMaxWidth(),
                        ) {
                            items(listOf("Actor1", "Actor2", "Actor3")) { actor ->
                                Text(
                                    text = actor,
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    color = Color.White,
                                )
                            }
                        }

                        LazyRow(
                            modifier =
                                Modifier
                                    .padding(vertical = 16.dp)
                                    .fillMaxWidth(),
                        ) {
                            items(listOf("Recommendation1", "Recommendation2", "Recommendation3")) { recommendation ->
                                Text(
                                    text = recommendation,
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun onBackClick() {
}

fun onFavClick(movieId: Int) {
}
