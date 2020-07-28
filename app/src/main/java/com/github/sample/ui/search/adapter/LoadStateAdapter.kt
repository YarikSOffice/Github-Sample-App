package com.github.sample.ui.search.adapter;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.github.sample.databinding.LoadStateListItemBinding
import com.github.sample.ui.search.adapter.LoadStateAdapter.LoadStateViewHolder

class LoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ): LoadStateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val b = LoadStateListItemBinding.inflate(inflater, parent, false)
        return LoadStateViewHolder(b, retry)
    }

    override fun onBindViewHolder(
        holder: LoadStateViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState)

    class LoadStateViewHolder(
        private val b: LoadStateListItemBinding,
        retry: () -> Unit
    ) : ViewHolder(b.root) {

        init {
            b.retry.setOnClickListener { retry() }
        }

        fun bind(loadState: LoadState) {
            b.progress.isVisible = loadState is LoadState.Loading
            b.retry.isVisible = loadState is LoadState.Error
        }
    }
}
