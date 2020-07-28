package com.github.sample.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.squareup.picasso.Picasso
import com.github.sample.databinding.RepositoryListItemBinding
import com.github.sample.domain.model.Repo
import com.github.sample.ui.history.HistoryAdapter.RepositoryViewHolder

class HistoryAdapter(
    private val callback: (Repo) -> Unit
) :
    RecyclerView.Adapter<RepositoryViewHolder>() {

    private val items = mutableListOf<Repo>()

    fun swapData(data: List<Repo>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RepositoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val b = RepositoryListItemBinding.inflate(inflater, parent, false)
        return RepositoryViewHolder(b) { callback.invoke(items[it]) }
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

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

}
