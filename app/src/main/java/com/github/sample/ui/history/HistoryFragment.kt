package com.github.sample.ui.history

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.github.sample.R
import com.github.sample.databinding.HistoryFragmentBinding
import com.github.sample.ui.App
import com.github.sample.ui.util.autoCleared
import com.github.sample.ui.util.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import javax.inject.Inject

class HistoryFragment : Fragment(R.layout.history_fragment) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: HistoryViewModel by viewModels { factory }
    private val b by viewBinding(HistoryFragmentBinding::bind)

    private var adapter by autoCleared<HistoryAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.component().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpRecyclerView()
        bindToViewModel()
    }

    private fun setUpToolbar() {
        // TODO abuse the old actionbar here for the sake of simplicity - use a separate Toolbar
        // for each fragment in real apps
        val actionbar = (requireActivity() as AppCompatActivity).supportActionBar!!
        actionbar.setTitle(R.string.fragment_history_title)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    private fun setUpRecyclerView() {
        adapter = HistoryAdapter {
            startActivity(Intent(ACTION_VIEW, Uri.parse(it.url)))
        }
        b.recycler.adapter = adapter
    }

    private fun bindToViewModel() {
        viewModel.items.observe(viewLifecycleOwner) {
            adapter.swapData(it)
        }
        viewModel.event.observe(viewLifecycleOwner) {
            when (it.getContentIfNotHandled()) {
                is Event.Error -> {
                    Snackbar.make(b.root, R.string.auth_error_message, LENGTH_LONG).show()
                }
            }
        }
    }
}
