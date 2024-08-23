package com.obss.firstapp.view.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.obss.firstapp.R
import com.obss.firstapp.data.model.movie.Movie
import com.obss.firstapp.utils.Constants.IMAGE_BASE_URL
import com.obss.firstapp.utils.ext.roundToSingleDecimal

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val popularMovies = homeViewModel.popularMovieList.collectAsLazyPagingItems()
    val topRatedMovies = homeViewModel.topRatedMovieList.collectAsLazyPagingItems()
    val nowPlayingMovies = homeViewModel.nowPlayingMovieList.collectAsLazyPagingItems()
    val errorMessage = homeViewModel.errorMessage.collectAsState().value

    var selectedOption by remember { mutableStateOf(POPULAR) }
    var isGridView by remember { mutableStateOf(isGridLayout) }

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
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, start = 4.dp, end = 4.dp)
                        .height(dimensionResource(id = R.dimen.top_bar_height)),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.movie_app_icon),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .padding(5.dp)
                            .size(dimensionResource(id = R.dimen.movie_app_icon_size))
                            .align(Alignment.CenterStart),
                )

                Text(
                    text = stringResource(id = R.string.movie_app),
                    color = Color.White,
                    fontSize = dimensionResource(id = R.dimen.movie_app_text_size).value.sp,
                    fontFamily = FontFamily(Font(R.font.eduhand_bold)),
                    modifier = Modifier.align(Alignment.Center),
                )

                IconButton(
                    onClick = {
                        isGridView = !isGridView
                        isGridLayout = isGridView
                    },
                    modifier =
                        Modifier
                            .size(dimensionResource(id = R.dimen.layout_button_size))
                            .align(Alignment.CenterEnd)
                            .background(color = colorResource(id = R.color.gray), shape = CircleShape),
                ) {
                    Icon(
                        painter =
                            painterResource(
                                id = if (isGridView) R.drawable.grid_view_24 else R.drawable.linear_view_24,
                            ),
                        contentDescription = null,
                    )
                }
            }
            SegmentedButton(
                selectedOption = selectedOption,
                onOptionSelected = { option ->
                    selectedOption = option
                },
            )
            when (selectedOption) {
                POPULAR -> {
                    popularMovies.refresh()
                    DisplayMovies(
                        movies = popularMovies,
                        navController = navController,
                        errorMessage = errorMessage,
                        isGridLayoutSelected = isGridView,
                    )
                }
                TOP_RATED -> {
                    topRatedMovies.refresh()
                    DisplayMovies(
                        movies = topRatedMovies,
                        navController = navController,
                        errorMessage = errorMessage,
                        isGridLayoutSelected = isGridView,
                    )
                }
                NOW_PLAYING -> {
                    nowPlayingMovies.refresh()
                    DisplayMovies(
                        movies = nowPlayingMovies,
                        navController = navController,
                        errorMessage = errorMessage,
                        isGridLayoutSelected = isGridView,
                    )
                }
            }
        }
    }
}

@Composable
fun DisplayMovies(
    movies: LazyPagingItems<Movie>,
    navController: NavController,
    errorMessage: String = "",
    isGridLayoutSelected: Boolean,
) {
    if (isGridLayoutSelected) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(120.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(movies.itemCount) { index ->
                movies[index]?.let { movie ->
                    MovieGridItem(movie = movie, navController = navController)
                }
            }

            when {
                movies.loadState.refresh is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
                movies.loadState.append is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
                errorMessage.isNotEmpty() -> {
                    item {
                        Text(
                            "Error: $errorMessage",
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(movies.itemCount) { index ->
                movies[index]?.let { movie ->
                    MovieLinearItem(movie = movie, navController = navController)
                }
            }

            when {
                movies.loadState.refresh is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
                movies.loadState.append is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
                errorMessage.isNotEmpty() -> {
                    item {
                        Text(
                            "Error: $errorMessage",
                            modifier = Modifier.fillMaxWidth(),
                        )
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

@Composable
fun MovieLinearItem(
    movie: Movie,
    navController: NavController,
) {
    val backgroundColor = colorResource(id = R.color.gray_background)
    val cornerRadius = 10.dp
    val strokeWidth = 3.dp
    val cardElevation = 20.dp

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.movie_linear_item_height))
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            AsyncImage(
                model = "${IMAGE_BASE_URL}${movie.posterPath}",
                contentDescription = null,
                modifier =
                    Modifier
                        .width(dimensionResource(id = R.dimen.movie_linear_item_width))
                        .height(dimensionResource(id = R.dimen.movie_linear_item_height))
                        .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = movie.title.toString(),
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
                    fontSize = dimensionResource(id = R.dimen.movie_linear_item_text_size).value.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Column(
                modifier = Modifier.padding(8.dp).fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.star_24),
                    contentDescription = null,
                    tint = colorResource(id = R.color.gold),
                    modifier =
                        Modifier
                            .size(dimensionResource(id = R.dimen.movie_linear_icon_size)),
                )
                Spacer(modifier = Modifier.width(6.dp))
                movie.voteAverage?.let {
                    Text(
                        text = it.roundToSingleDecimal(),
                        color = Color.White,
                        fontSize = dimensionResource(id = R.dimen.movie_linear_item_text_size).value.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter =
                        painterResource(
                            id = if (movie.isFavorite) R.drawable.favorite_24 else R.drawable.favorite_border_24,
                        ),
                    contentDescription = null,
                    tint = Color.White,
                    modifier =
                        Modifier
                            .size(dimensionResource(id = R.dimen.movie_linear_icon_size)),
                )
            }
        }
    }
}

@Composable
fun SegmentedButton(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    val options = listOf(POPULAR, TOP_RATED, NOW_PLAYING)

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp)
                .padding(horizontal = 6.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        options.forEach { option ->
            val isSelected = option == selectedOption
            Button(
                onClick = { onOptionSelected(option) },
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color.Red else Color.Gray,
                        contentColor = Color.White,
                    ),
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(4.dp),
                shape = RoundedCornerShape(50),
            ) {
                Text(text = option, fontSize = 14.sp, overflow = TextOverflow.Ellipsis, maxLines = 1)
            }
        }
    }
}

@Composable
fun TopBar() {
    var isGridView by remember { mutableStateOf(true) }
}

private var isGridLayout = true
private const val POPULAR = "Popular"
private const val TOP_RATED = "Top Rated"
private const val NOW_PLAYING = "Now Playing"
