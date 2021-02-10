package org.tridzen.mamafua.ui.home.launcher.history.news

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.tridzen.mamafua.data.local.entities.News
import org.tridzen.mamafua.databinding.RowNBinding
import org.tridzen.mamafua.utils.Constants.Companion.BASE_URL

class NewsAdapter : ListAdapter<News, NewsAdapter.NewsViewHolder>(NewsDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = RowNBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NewsViewHolder(private val binding: RowNBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: News) {
            binding.tvHeadline.text = message.title
            binding.tvDescription.text = message.content
            Glide.with(binding.root).load("${BASE_URL}/${message.imageUrl}").into(binding.ivThumbnail)
            Log.d("TAG", "bind: ${BASE_URL}/${message.imageUrl}")
        }
    }

    class NewsDiffCallBack : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }
}
