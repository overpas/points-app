package com.example.pointsapp.main

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.pointsapp.R
import com.example.pointsapp.points.PointsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val letsGoButton: Button by lazy { findViewById(R.id.letsGoButton) }
    private val pointsCountEditText: EditText by lazy { findViewById(R.id.pointsCountEditText) }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupInputs()
        onSubscribe()
    }

    private fun setupInputs() {
        letsGoButton.setOnClickListener {
            viewModel.onLetsGoClicked()
        }
        pointsCountEditText.doAfterTextChanged { editable ->
            if (editable != null) {
                val text = editable.toString()
                if (text.startsWith('0')) {
                    if (text.length == 1) {
                        editable.clear()
                    } else if (text.length > 1) {
                        editable.replace(0, text.length, text.dropWhile { it == '0' })
                    }
                }
            }
        }
        pointsCountEditText.doOnTextChanged { charSequence, _, _, _ ->
            val textWithoutLeadingZeros = charSequence?.toString()?.dropWhile { it == '0' }
            viewModel.onCountChanged(
                if (textWithoutLeadingZeros.isNullOrBlank()) {
                    null
                } else {
                    textWithoutLeadingZeros.toInt()
                },
            )
        }
    }

    private fun onSubscribe() {
        viewModel.letsGoEnabled
            .flowWithLifecycle(lifecycle)
            .onEach(letsGoButton::setEnabled)
            .launchIn(lifecycleScope)

        viewModel.events
            .flowWithLifecycle(lifecycle)
            .onEach(::handleEvent)
            .launchIn(lifecycleScope)
    }

    private fun handleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.NavigateToPoints -> {
                startActivity(PointsActivity.getIntent(this, event.count))
            }
        }
    }
}
