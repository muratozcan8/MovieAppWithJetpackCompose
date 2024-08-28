package com.obss.firstapp.view.detail

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.HorizontalPager
import com.obss.firstapp.R
import com.obss.firstapp.data.local.FavoriteMovie
import com.obss.firstapp.data.model.actor.Actor
import com.obss.firstapp.data.model.movie.Movie
import com.obss.firstapp.data.model.movieDetail.MovieDetail
import com.obss.firstapp.utils.Constants.IMAGE_BASE_URL
import com.obss.firstapp.utils.Constants.YOUTUBE_APP
import com.obss.firstapp.utils.Constants.YOUTUBE_BASE_URL
import com.obss.firstapp.utils.ext.formatAndCalculateAge
import com.obss.firstapp.utils.ext.roundToSingleDecimal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    movieId: Int,
    detailViewModel: DetailViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    detailViewModel.getMovieDetails(movieId)
    detailViewModel.getMovieImages(movieId)
    detailViewModel.getMovieCasts(movieId)
    detailViewModel.getRecommendationMovies(movieId)
    detailViewModel.getReviews(movieId)
    detailViewModel.getFavoriteMovie(movieId)
    detailViewModel.getVideos(movieId)

    val movie = detailViewModel.movie.collectAsState().value
    val movieImages = detailViewModel.movieImages.collectAsState().value
    val movieCasts = detailViewModel.movieCasts.collectAsState().value
    val recommendationMovies = detailViewModel.recommendationMovies.collectAsState().value
    val reviews = detailViewModel.reviews.collectAsState().value
    val actor = detailViewModel.actor.collectAsState().value
    val isLoadings = detailViewModel.isLoadings.collectAsState().value
    val videos = detailViewModel.videos.collectAsState().value
    val errorMessage = detailViewModel.errorMessage.collectAsState().value

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet = remember { mutableStateOf(false) }
    var actorId = remember { mutableIntStateOf(0) }

    val isDialogVisible = remember { mutableStateOf(true) }

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
                    if (movieImages.isNotEmpty()) {
                        HorizontalPager(
                            count = movieImages.size,
                            modifier = Modifier.fillMaxSize(),
                        ) { page ->
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
                        if (videos.isNotEmpty()) {
                            for (video in videos) {
                                if (video.site == YOUTUBE && video.type == TRAILER && video.official == true) {
                                    IconButton(onClick = {
                                        val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("$YOUTUBE_APP${video.key}"))
                                        val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse("$YOUTUBE_BASE_URL${video.key}"))
                                        try {
                                            context.startActivity(intentApp)
                                        } catch (e: ActivityNotFoundException) {
                                            context.startActivity(intentBrowser)
                                        }
                                    }) {
                                        Icon(
                                            painter =
                                                painterResource(
                                                    id = R.drawable.play_circle_outline_24,
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
                                    break
                                }
                            }
                        }
                        if (movie != null) {
                            val isFavorite = remember { mutableStateOf(movie.isFavorite) }

                            IconButton(onClick = {
                                onFavClick(movieId, movie, detailViewModel, context)
                                isFavorite.value = !isFavorite.value
                            }) {
                                Icon(
                                    painter =
                                        painterResource(
                                            id = if (isFavorite.value) R.drawable.favorite_24 else R.drawable.favorite_border_24,
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
                            text = movie?.title ?: TEMP,
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

                                if (movie != null) {
                                    Text(
                                        text = movie.voteAverage?.roundToSingleDecimal().toString(),
                                        style =
                                            TextStyle(
                                                color = Color.White,
                                                fontSize = dimensionResource(id = R.dimen.movie_info_text_size).value.sp,
                                                fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
                                            ),
                                    )
                                }
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

                                if (movie != null) {
                                    Text(
                                        text = movie.runtime.toInt().toString(),
                                        style =
                                            TextStyle(
                                                color = Color.White,
                                                fontSize = dimensionResource(id = R.dimen.movie_info_text_size).value.sp,
                                                fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
                                            ),
                                    )
                                }
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

                if (movie != null) {
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
                        text = movie.overview ?: TEMP,
                        style =
                            TextStyle(
                                color = Color.White,
                                fontSize = dimensionResource(id = R.dimen.movie_detail_text_size).value.sp,
                                fontFamily = FontFamily(Font(R.font.ubuntu_light)),
                            ),
                        modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 6.dp),
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (reviews.isNotEmpty()) {
                    Button(
                        onClick = { navController.navigate("review/$movieId/${movie?.title}") },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    ) {
                        Text(text = "Reviews (${reviews.size})", color = Color.White, fontSize = 16.sp)
                    }
                }

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
                        text = movieCasts.take(3).joinToString(SEPARATOR) { it.name.toString() },
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
                                contentDescription = ACTOR_IMAGE_DESC,
                                modifier =
                                    Modifier
                                        .size(84.dp)
                                        .padding(horizontal = 8.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            detailViewModel.getActorDetails(actor.id!!)
                                            showBottomSheet.value = true
                                            actorId.intValue = actor.id
                                        },
                            )
                        }
                    }
                }
                if (showBottomSheet.value) {
                    if (actor != null) {
                        ActorDetailBottomSheet(
                            showBottomSheet = showBottomSheet,
                            scope = scope,
                            sheetState = sheetState,
                            actor = actor,
                            context = context,
                        )
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
        if (errorMessage.isNotEmpty() && isDialogVisible.value) {
            AlertDialogExample(
                onDismissRequest = { },
                onClose = { isDialogVisible.value = false },
                dialogTitle = ERROR,
                dialogText = errorMessage,
                icon = painterResource(id = R.drawable.error_24),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActorDetailBottomSheet(
    showBottomSheet: MutableState<Boolean>,
    scope: CoroutineScope,
    sheetState: SheetState,
    actor: Actor,
    context: Context,
) {
    ModalBottomSheet(
        onDismissRequest = {
            showBottomSheet.value = false
        },
        sheetState = sheetState,
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = "${IMAGE_BASE_URL}${actor.profilePath}",
                contentDescription = null,
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .size(120.dp),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
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
                        tint = Color(0xFF000000),
                        modifier = Modifier.size(24.dp),
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    actor.birthday?.let {
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = it.formatAndCalculateAge(),
                            style =
                                TextStyle(
                                    color = Color.Black,
                                    fontSize = dimensionResource(id = R.dimen.movie_info_text_size).value.sp,
                                    fontFamily = FontFamily(Font(R.font.ubuntu_medium)),
                                ),
                        )
                    }
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
                        painter = painterResource(id = R.drawable.home_24),
                        contentDescription = null,
                        tint = Color(0xFF000000),
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    actor.placeOfBirth?.let {
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = it,
                            style =
                                TextStyle(
                                    color = Color.Black,
                                    fontSize = dimensionResource(id = R.dimen.movie_info_text_size).value.sp,
                                    fontFamily = FontFamily(Font(R.font.ubuntu_medium)),
                                ),
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (actor.homepage != null) {
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(actor.homepage.toString()))
                            context.startActivity(intent)
                        },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.webpage_24),
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

        Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
            Text(
                text = "Biography",
                style = TextStyle(color = Color.Black, fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.ubuntu_bold))),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = actor.biography.toString(),
                style = TextStyle(color = Color.Black, fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.ubuntu_light))),
            )
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp),
                onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet.value = false
                        }
                    }
                },
            ) {
                Text("Close")
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
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

fun onFavClick(
    movieId: Int,
    movie: MovieDetail?,
    viewModel: DetailViewModel,
    context: Context,
) {
    if (movie?.isFavorite == true) {
        movie.id?.let { viewModel.getFavoriteMovie(it) }
        viewModel.viewModelScope.launch {
            viewModel.favoriteMovie.collect {
                if (it != null) {
                    viewModel.removeFavoriteMovie(it)
                    movie.isFavorite = false
                }
            }
        }
        showCustomToast(context, REMOVED_MESSAGE)
    } else {
        movie?.isFavorite = true
        viewModel.addFavoriteMovie(
            favoriteMovie = FavoriteMovie(0, movieId, movie?.title.toString(), movie?.posterPath.toString(), movie?.voteAverage),
        )
        showCustomToast(context, ADDED_MESSAGE)
    }
}

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onClose: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: Painter,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = EXAMPLE_ICON_DESC)
        },
        title = {
            Text(
                text = dialogTitle,
                color = Color.Black,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.ubuntu_bold)),
            )
        },
        text = {
            Text(
                text = dialogText,
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.ubuntu_light)),
                textAlign = TextAlign.Center,
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onClose()
                },
            ) {
                Text(CLOSE)
            }
        },
    )
}

fun showCustomToast(
    context: Context,
    message: String,
) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

private const val YOUTUBE = "YouTube"
private const val TRAILER = "Trailer"
private const val SEPARATOR = ", "
private const val ACTOR_IMAGE_DESC = "Actor Image"
private const val ERROR = "Error"
private const val TEMP = ""
private const val REMOVED_MESSAGE = "Removed from favorites"
private const val ADDED_MESSAGE = "Added to favorites"
private const val EXAMPLE_ICON_DESC = "Example Icon"
private const val CLOSE = "Close"
