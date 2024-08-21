package com.obss.firstapp.view.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.HorizontalPager
import com.obss.firstapp.R
import com.obss.firstapp.data.model.movie.Movie
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
        Column {
            Column(
                modifier =
                    Modifier
                        .verticalScroll(rememberScrollState()),
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.77f),
                ) {
                    HorizontalPager(
                        count = 5,
                        modifier = Modifier.fillMaxSize(),
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
                                .fillMaxSize()
                                .background(
                                    brush =
                                        Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, Color.Black),
                                            startY = 0f,
                                            endY = Float.POSITIVE_INFINITY,
                                        ),
                                ),
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .align(Alignment.TopStart),
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
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomStart),
                    ) {
                        Text(
                            text = movie?.title ?: "",
                            style =
                                TextStyle(
                                    color = Color.White,
                                    fontSize = dimensionResource(id = R.dimen.movie_name_text_size).value.sp,
                                    fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
                                ),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        )
                        Row(
                            modifier =
                                Modifier
                                    .padding(horizontal = 12.dp)
                                    .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier =
                                    Modifier
                                        .background(color = Color.Transparent)
                                        .padding(4.dp),
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.star_24),
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(24.dp),
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = movie?.voteAverage?.roundToSingleDecimal().toString(),
                                    style =
                                        TextStyle(
                                            color = Color.White,
                                            fontSize = dimensionResource(id = R.dimen.movie_info_text_size).value.sp,
                                            fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
                                        ),
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier =
                                    Modifier
                                        .background(color = Color.Transparent)
                                        .padding(4.dp),
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.time_24),
                                    contentDescription = null,
                                    tint = Color(0xFFFFFFFF),
                                    modifier = Modifier.size(24.dp),
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = movie?.runtime?.toInt().toString(),
                                    style =
                                        TextStyle(
                                            color = Color.White,
                                            fontSize = dimensionResource(id = R.dimen.movie_info_text_size).value.sp,
                                            fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
                                        ),
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            movie?.releaseDate?.take(4)?.let {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier =
                                        Modifier
                                            .background(color = Color.Transparent)
                                            .padding(4.dp),
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.date_24),
                                        contentDescription = null,
                                        tint = Color(0xFFFFFFFF),
                                        modifier = Modifier.size(24.dp),
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Text(
                                        text = movie.releaseDate.take(4),
                                        style =
                                            TextStyle(
                                                color = Color.White,
                                                fontSize = dimensionResource(id = R.dimen.movie_info_text_size).value.sp,
                                                fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
                                            ),
                                    )
                                }
                            }
                        }
                    }
                }

                LazyRow(
                    modifier =
                        Modifier
                            .padding(vertical = 4.dp, horizontal = 16.dp)
                            .fillMaxWidth(),
                ) {
                    movie?.genres?.let {
                        items(it.size) { index ->
                            Box(
                                modifier =
                                    Modifier
                                        .padding(6.dp)
                                        .background(color = Color.Gray, shape = RoundedCornerShape(6.dp)),
                            ) {
                                Text(
                                    text = it[index].name.toString(),
                                    style =
                                        TextStyle(
                                            fontFamily = FontFamily(Font(R.font.ubuntu_medium)),
                                            color = Color.White,
                                            fontSize = 16.sp,
                                        ),
                                    modifier =
                                        Modifier
                                            .padding(6.dp),
                                )
                            }
                        }
                    }
                }

                Text(
                    text = stringResource(id = R.string.summary),
                    style =
                        TextStyle(
                            color = Color.LightGray,
                            fontSize = dimensionResource(id = R.dimen.movie_detail_title).value.sp,
                            fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
                        ),
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp),
                )

                Text(
                    text = movie?.overview ?: "",
                    style =
                        TextStyle(
                            color = Color.White,
                            fontSize = dimensionResource(id = R.dimen.movie_detail_text_size).value.sp,
                            fontFamily = FontFamily(Font(R.font.ubuntu_light)),
                        ),
                    modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 6.dp),
                )

                if (movieCasts.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.actors),
                        style =
                            TextStyle(
                                color = Color.LightGray,
                                fontSize = dimensionResource(id = R.dimen.movie_detail_title).value.sp,
                                fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
                            ),
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp),
                    )

                    Text(
                        text = movieCasts.take(3).joinToString(", ") { it.name.toString() },
                        style =
                            TextStyle(
                                color = Color.White,
                                fontSize = dimensionResource(id = R.dimen.movie_detail_text_size).value.sp,
                                fontFamily = FontFamily(Font(R.font.ubuntu_medium)),
                            ),
                        modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 6.dp),
                    )

                    LazyRow(
                        modifier =
                            Modifier
                                .padding(vertical = 16.dp, horizontal = 12.dp)
                                .padding(bottom = 16.dp)
                                .fillMaxWidth(),
                    ) {
                        items(movieCasts.take(3)) { actor ->
                            AsyncImage(
                                model = "${IMAGE_BASE_URL}${actor.profilePath}",
                                contentDescription = "Actor Image",
                                modifier =
                                    Modifier
                                        .size(84.dp)
                                        .padding(horizontal = 8.dp)
                                        .clip(CircleShape),
                            )
                        }
                    }
                }

                if (recommendationMovies.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.recommendations),
                        style =
                            TextStyle(
                                color = Color.LightGray,
                                fontSize = dimensionResource(id = R.dimen.movie_detail_title).value.sp,
                                fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
                            ),
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp),
                    )

                    LazyRow(
                        modifier =
                            Modifier
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .fillMaxWidth(),
                    ) {
                        items(recommendationMovies) { recommendation ->
                            MovieGridItem(movie = recommendation, navController = navController)
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieGridItem(
    movie: Movie,
    navController: NavController,
) {
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
                .height(dimensionResource(id = R.dimen.movie_grid_item_height) + 100.dp)
                .padding(
                    horizontal = 3.dp,
                    vertical = dimensionResource(id = R.dimen.movie_grid_item_margin_bottom),
                ).clickable {
                    navController.navigate("detail/${movie.id}")
                },
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(strokeWidth, Color.Gray),
    ) {
        Column(modifier = Modifier.fillMaxHeight()) {
            AsyncImage(
                model = "${IMAGE_BASE_URL}${movie.posterPath}",
                contentDescription = "Movie Image",
                modifier = imageModifier,
                contentScale = ContentScale.Fit,
            )
            Text(
                text = movie.title.toString(),
                fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
                fontSize = dimensionResource(id = R.dimen.movie_grid_item_text_size).value.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier =
                    Modifier
                        .padding(top = 2.dp)
                        .padding(horizontal = dimensionResource(id = R.dimen.movie_grid_item_text_margin_horizontal))
                        .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier
                        .padding(vertical = dimensionResource(id = R.dimen.movie_grid_item_margin_bottom))
                        .fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.star_24),
                    contentDescription = "Movie Score",
                    modifier =
                        Modifier
                            .padding(start = dimensionResource(id = R.dimen.movie_grid_item_icon_margin_bottom)),
                    colorFilter = ColorFilter.tint(Color(0xFFFFD700)),
                )
                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    text = movie.voteAverage?.roundToSingleDecimal().toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.movie_grid_item_text_size).value.sp,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.movie_grid_item_icon_margin_bottom)),
                    painter = painterResource(id = if (movie.isFavorite) R.drawable.favorite_24 else R.drawable.favorite_border_24),
                    contentDescription = "Favorite Icon",
                    tint = Color.White,
                )
            }
        }
    }
}

fun onBackClick(navController: NavController) {
    navController.popBackStack()
}

fun onFavClick(movieId: Int) {
}
