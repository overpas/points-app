package com.example.pointsapp.points

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.example.pointsapp.domain.GetPoints
import com.example.pointsapp.domain.model.Point
import com.example.pointsapp.domain.repository.PointsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class PointsViewModelTest {

    private val testCount = 5
    private val testSavedStateHandle = SavedStateHandle()
    private val testPoints = listOf(
        Point(1.0, 1.0),
        Point(2.0, 2.0),
    )

    private lateinit var viewModel: PointsViewModel

    @Test
    fun `initial state is Initial`() {
        viewModel = PointsViewModel(
            count = testCount,
            getPoints = GetPoints(
                object : PointsRepository {
                    override suspend fun get(count: Int): Result<List<Point>> {
                        TODO("Not yet implemented")
                    }
                }
            ),
            savedStateHandle = testSavedStateHandle,
        )

        val actual = viewModel.state.value

        assertEquals(PointsState.Initial, actual)
    }

    @Test
    fun `state is Initial, Loading, Content when points loaded successfully`() = runTest {
        viewModel = PointsViewModel(
            count = testCount,
            getPoints = GetPoints(
                object : PointsRepository {
                    override suspend fun get(count: Int): Result<List<Point>> {
                        delay(2000)
                        return Result.success(testPoints)
                    }
                }
            ),
            savedStateHandle = testSavedStateHandle,
        )

        turbineScope {
            with(viewModel.state.testIn(this)) {
                assertEquals(PointsState.Initial, awaitItem())
                assertEquals(PointsState.Loading, awaitItem())
                assertEquals(PointsState.Content(testPoints), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `state is Initial, Loading, Error when points loaded with error`() = runTest {
        viewModel = PointsViewModel(
            count = testCount,
            getPoints = GetPoints(
                object : PointsRepository {
                    override suspend fun get(count: Int): Result<List<Point>> {
                        delay(2000)
                        return runCatching { error("Test error") }
                    }
                }
            ),
            savedStateHandle = testSavedStateHandle,
        )

        turbineScope {
            with(viewModel.state.testIn(this)) {
                assertEquals(PointsState.Initial, awaitItem())
                assertEquals(PointsState.Loading, awaitItem())
                assertEquals(PointsState.Error, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `NavigateBack event is emitted when go back clicked`() = runTest {
        viewModel = PointsViewModel(
            count = testCount,
            getPoints = GetPoints(
                object : PointsRepository {
                    override suspend fun get(count: Int): Result<List<Point>> {
                        TODO("Not yet implemented")
                    }
                }
            ),
            savedStateHandle = testSavedStateHandle,
        )

        viewModel.events.test {
            viewModel.onGoBackClicked()

            assertEquals(PointsEvent.NavigateBack, awaitItem())
        }
    }
}
