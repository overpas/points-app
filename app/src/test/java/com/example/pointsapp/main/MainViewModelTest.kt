package com.example.pointsapp.main

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val viewModel = MainViewModel()

    @Test
    fun `lets go is disabled initially`() {
        val actual = viewModel.letsGoEnabled.value

        assertFalse(actual)
    }

    @Test
    fun `lets go is enabled when count is set to positive number`() = runTest {
        viewModel.letsGoEnabled.test {
            viewModel.onCountChanged(10)
            awaitItem()

            assertTrue(awaitItem())
        }
    }

    @Test
    fun `lets go is disabled when count is set to null`() = runTest {
        viewModel.onCountChanged(null)

        viewModel.letsGoEnabled.test {
            assertFalse(awaitItem())
        }
    }

    @Test
    fun `lets go is disabled when count is set to 0`() = runTest {
        viewModel.onCountChanged(0)

        viewModel.letsGoEnabled.test {
            assertFalse(awaitItem())
        }
    }

    @Test
    fun `navigate to points emitted when lets go is clicked`() = runTest {
        viewModel.onCountChanged(5)

        viewModel.events.test {
            viewModel.onLetsGoClicked()

            assertEquals(MainEvent.NavigateToPoints(5), awaitItem())
        }
    }
}
