package com.ksnredawew.unisocchecker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ksnredawew.unisocchecker.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val checker = Checker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.checkButton.setOnClickListener {
            runChecks()
        }

        binding.warningButton.setOnClickListener {
            showWarningDialog()
        }
    }

    private fun runChecks() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.progressBar.visibility = android.view.View.VISIBLE
            binding.resultTextView.text = getString(R.string.checking)

            val result = withContext(Dispatchers.IO) {
                checker.performAllChecks()
            }

            binding.progressBar.visibility = android.view.View.GONE
            binding.resultTextView.text = result.overallStatus
            binding.detailsTextView.text = result.details
            binding.warningButton.visibility = android.view.View.VISIBLE
        }
    }

    private fun showWarningDialog() {
        android.app.AlertDialog.Builder(this)
            .setTitle(getString(R.string.warning_title))
            .setMessage(getString(R.string.warning_message))
            .setPositiveButton(getString(R.string.understand), null)
            .show()
    }
}
