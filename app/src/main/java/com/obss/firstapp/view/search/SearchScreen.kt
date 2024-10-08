package com.obss.firstapp.view.search

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.obss.firstapp.R
import com.obss.firstapp.data.model.movieSearch.MovieSearch
import com.obss.firstapp.utils.Constants.IMAGE_BASE_URL
import com.obss.firstapp.utils.ext.roundToSingleDecimal

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val query = viewModel.searchQuery.collectAsState().value
    val searchList = viewModel.searchMovieList.collectAsState()
    val loadingState = viewModel.loadingStateFlow.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState().value

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
            SearchBar(
                query = query,
                onQueryChanged = { viewModel.updateQuery(it) },
                onSearch = { viewModel.searchMovies(query) },
            )
            DisplayMovies(movies = searchList, isLoading = loadingState.value, navController)
        }
    }
    if (errorMessage.isNotEmpty() && isDialogVisible.value) {
        AlertDialogError(
            onDismissRequest = { },
            onClose = { isDialogVisible.value = false },
            dialogTitle = ERROR,
            dialogText = errorMessage,
            icon = painterResource(id = R.drawable.error_24),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
) {
    val keyboardAction = LocalSoftwareKeyboardController.current
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
        placeholder = { Text(text = SEARCH, color = Color.White) },
        singleLine = true,
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = SEARCH_ICON_DESC, tint = Color.White)
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChanged("") }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = CLEAR_ICON_DESC, tint = Color.White)
                }
            }
        },
        keyboardOptions =
            KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search,
            ),
        keyboardActions =
            KeyboardActions(
                onSearch = {
                    onSearch(query)
                    keyboardAction?.hide()
                },
            ),
        shape = RoundedCornerShape(12.dp),
        colors =
            TextFieldDefaults.textFieldColors(
                focusedTextColor = Color.White,
                disabledTextColor = Color.White,
                unfocusedTextColor = Color.White,
                errorTextColor = Color.White,
                containerColor = Color.Gray.copy(alpha = 0.5f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
    )
}

@Composable
fun DisplayMovies(
    movies: State<List<MovieSearch>>,
    isLoading: Boolean = false,
    navController: NavController,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(8.dp),
    ) {
        itemsIndexed(movies.value) { _, movie ->
            MovieGridItem(movie = movie, navController = navController)
        }

        when {
            isLoading -> {
                item {
                    CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
fun MovieGridItem(
    movie: MovieSearch,
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
                .clickable { navController.navigate("detail/${movie.id}") }
                .padding(
                    horizontal = 3.dp,
                    vertical = dimensionResource(id = R.dimen.movie_grid_item_margin_bottom),
                ),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(strokeWidth, Color.Gray),
    ) {
        Column(modifier = Modifier.fillMaxHeight()) {
            AsyncImage(
                model = "$IMAGE_BASE_URL${movie.posterPath}",
                contentDescription = MOVIE_IMAGE_DESC,
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
                    contentDescription = FAVORITE_ICON_DESC,
                    tint = Color.White,
                )
            }
        }
    }
}

@Composable
fun AlertDialogError(
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

private const val ERROR = "Error"
private const val CLOSE = "Close"
private const val SEARCH = "Search..."
private const val SEARCH_ICON_DESC = "Search Icon"
private const val CLEAR_ICON_DESC = "Clear Icon"
private const val MOVIE_IMAGE_DESC = "Movie Image"
private const val FAVORITE_ICON_DESC = "Favorite Icon"
private const val EXAMPLE_ICON_DESC = "Example Icon"
