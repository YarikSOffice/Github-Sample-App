package com.github.sample.ui.auth

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import com.github.sample.R
import com.github.sample.databinding.AuthFragmentBinding
import com.github.sample.ui.App
import com.github.sample.ui.IntentInterceptor
import com.github.sample.ui.Navigator
import com.github.sample.ui.util.viewBinding
import javax.inject.Inject

class AuthFragment : Fragment(R.layout.auth_fragment), IntentInterceptor {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: AuthViewModel by viewModels { factory }

    private val binding by viewBinding(AuthFragmentBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.component().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        bindToViewModel()

        binding.authButton.setOnClickListener {
            startActivity(Intent(ACTION_VIEW, viewModel.generateAuthUri()))
        }
    }

    private fun setUpToolbar() {
        // TODO abuse the old actionbar here for the sake of simplicity - use a separate Toolbar
        // for each fragment in real apps
        val actionbar = (requireActivity() as AppCompatActivity).supportActionBar!!
        actionbar.setTitle(R.string.fragment_repositories_title)
        actionbar.setDisplayHomeAsUpEnabled(false)
    }

    private fun bindToViewModel() {
        viewModel.state.observe(viewLifecycleOwner) {
            binding.authButton.isVisible = it == State.IDLE
            binding.progress.isVisible = it == State.LOADING
        }
        viewModel.event.observe(viewLifecycleOwner) {
            when (it.getContentIfNotHandled()) {
                is Event.Error -> {
                    Snackbar.make(binding.root, R.string.auth_error_message, LENGTH_LONG).show()
                }
                is Event.Authorization -> (requireActivity() as Navigator).toSearchScreen()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        intent.data?.let { viewModel.handleAuthRedirection(it) }
    }
}
