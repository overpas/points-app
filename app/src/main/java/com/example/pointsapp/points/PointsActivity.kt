package com.example.pointsapp.points

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pointsapp.R
import com.example.pointsapp.domain.model.Point
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PointsActivity : AppCompatActivity() {

    private val progressBar: ProgressBar by lazy { findViewById(R.id.progressBar) }
    private val errorLayout: LinearLayout by lazy { findViewById(R.id.errorLayout) }
    private val pointsLayout: LinearLayout by lazy { findViewById(R.id.pointsLayout) }
    private val pointsTableView: RecyclerView by lazy { findViewById(R.id.pointsTableView) }
    private val goBackButton: Button by lazy { findViewById(R.id.goBackButton) }
    private val chartView: PointsChartView by lazy { findViewById(R.id.chartView) }

    private val viewModel by viewModels<PointsViewModel>(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback { factory: PointsViewModel.Factory ->
                factory.create(
                    count = intent.getIntExtra(POINTS_COUNT, 0),
                )
            }
        },
    )

    private val pointsAdapter = PointsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_points)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        pointsTableView.layoutManager = LinearLayoutManager(this)
        pointsTableView.adapter = pointsAdapter
        setupListeners()
        onSubscribe()
    }

    private fun setupListeners() {
        goBackButton.setOnClickListener {
            viewModel.onGoBackClicked()
        }
    }

    private fun onSubscribe() {
        viewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach(::handleState)
            .launchIn(lifecycleScope)

        viewModel.events
            .flowWithLifecycle(lifecycle)
            .onEach(::handleEvent)
            .launchIn(lifecycleScope)
    }

    private fun handleState(pointsState: PointsState) {
        when (pointsState) {
            is PointsState.Initial -> {
                handleInitial()
            }

            is PointsState.Loading -> {
                handleLoading()
            }

            is PointsState.Error -> {
                handleError()
            }

            is PointsState.Content -> {
                handleContent(pointsState.points)
            }
        }
    }

    private fun handleInitial() {
        progressBar.isVisible = false
        errorLayout.isVisible = false
        pointsLayout.isVisible = false
    }

    private fun handleLoading() {
        progressBar.isVisible = true
        errorLayout.isVisible = false
        pointsLayout.isVisible = false
    }

    private fun handleError() {
        progressBar.isVisible = false
        errorLayout.isVisible = true
        pointsLayout.isVisible = false
    }

    private fun handleContent(points: List<Point>) {
        progressBar.isVisible = false
        errorLayout.isVisible = false
        pointsLayout.isVisible = true
        pointsAdapter.submitList(points)
        chartView.setPoints(points)
    }

    private fun handleEvent(event: PointsEvent) {
        when (event) {
            is PointsEvent.NavigateBack -> {
                finish()
            }
        }
    }

    companion object {

        private const val POINTS_COUNT = "POINTS_COUNT"

        fun getIntent(context: Context, count: Int): Intent {
            return Intent(context, PointsActivity::class.java)
                .putExtra(POINTS_COUNT, count)
        }
    }
}
