package com.github.sample.ui.search.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.squareup.picasso.Picasso
import com.github.sample.databinding.RepositoryListItemBinding
import com.github.sample.domain.model.Repo
import com.github.sample.domain.model.WithId
import com.github.sample.ui.search.adapter.RepositoryAdapter.RepositoryViewHolder

class RepositoryAdapter(private val callback: (Repo) -> Unit) :
    PagingDataAdapter<Repo, RepositoryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val b = RepositoryListItemBinding.inflate(inflater, parent, false)
        return RepositoryViewHolder(b) { callback.invoke(getItem(it)!!) }
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    class RepositoryViewHolder(
        private val b: RepositoryListItemBinding,
        callback: (Int) -> Unit
    ) : ViewHolder(b.root) {

        init {
            b.root.setOnClickListener {
                callback.invoke(bindingAdapterPosition)
            }
        }

        fun bind(repo: Repo) {
            b.name.text = repo.fullName
            b.starCount.text = repo.stars.toString()
            // TODO can be abstracted in a real app
            Picasso.get().load(repo.owner.avatarUrl).into(b.image)
        }
    }

    class DiffCallback<T : WithId> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }
    }
}
