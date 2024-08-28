package com.obss.firstapp.view.review

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.obss.firstapp.data.model.review.AuthorDetails
import com.obss.firstapp.data.model.review.ReviewResult
import com.obss.firstapp.data.repository.MovieRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ReviewViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val movieRepository: MovieRepository = mock()
    private lateinit var reviewViewModel: ReviewViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        reviewViewModel = ReviewViewModel(movieRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGetReviewsUpdatesReviewListAndLoadingStateFlow() =
        runTest {
            val movieId = 16
            val reviews =
                listOf(
                    ReviewResult("Author 1", "Content 1", "1", AuthorDetails("a", 5.0, ""), ""),
                    ReviewResult("Author 2", "Content 2", "2", AuthorDetails("b", 5.0, ""), ""),
                )

            whenever(movieRepository.getMovieReviews(movieId, any(), any(), any())).thenAnswer {
                val reviewListFlow = it.getArgument<MutableStateFlow<List<ReviewResult>>>(1)

                reviewListFlow.value = reviews

                Unit
            }

            reviewViewModel.getReviews(movieId)

            assertEquals(reviews, reviewViewModel.reviewList.value)
        }
}
