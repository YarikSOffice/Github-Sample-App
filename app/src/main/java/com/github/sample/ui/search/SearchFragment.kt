package com.github.sample.ui.search

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import com.github.sample.R
import com.github.sample.databinding.SearchFragmentBinding
import com.github.sample.ui.App
import com.github.sample.ui.Navigator
import com.github.sample.ui.search.adapter.LoadStateAdapter
import com.github.sample.ui.search.adapter.RepositoryAdapter
import com.github.sample.ui.util.autoCleared
import com.github.sample.ui.util.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFragment : Fragment(R.layout.search_fragment) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: SearchViewModel by viewModels { factory }

    private val b by viewBinding(SearchFragmentBinding::bind)
    private var adapter by autoCleared<RepositoryAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        App.component().inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.history) {
            (requireActivity() as Navigator).toHistoryScreen()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpSearchView()
        setUpRecyclerView()
        bindToViewModel()
    }

    private fun setUpToolbar() {
        // TODO abuse the old actionbar here for the sake of simplicity - use a separate Toolbar
        // for each fragment in real apps
        val actionbar = (requireActivity() as AppCompatActivity).supportActionBar!!
        actionbar.setTitle(R.string.fragment_repositories_title)
        actionbar.setDisplayHomeAsUpEnabled(false)
    }

    private fun setUpSearchView() {
        b.searchView.doAfterTextChanged {
            viewModel.query(it.toString())
        }
    }

    private fun setUpRecyclerView() {
        adapter = RepositoryAdapter {
            startActivity(Intent(ACTION_VIEW, Uri.parse(it.url)))
            viewModel.recordVisitedRepo(it)
        }
        val concat = adapter.withLoadStateFooter(
            LoadStateAdapter(adapter::retry)
        )
        b.recycler.adapter = concat
    }

    private fun bindToViewModel() {
        viewModel.items.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                adapter.submitData(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                b.progress.isVisible = loadStates.refresh is LoadState.Loading
                if (loadStates.refresh is LoadState.Error) {
                    Snackbar.make(b.root, R.string.search_error_message, LENGTH_LONG).show()
                }
            }
        }
        viewModel.event.observe(viewLifecycleOwner) {
            if (it.getContentIfNotHandled() is Event.Error) {
                Snackbar.make(b.root, R.string.search_error_message, LENGTH_LONG).show()
            }
        }
    }
}
