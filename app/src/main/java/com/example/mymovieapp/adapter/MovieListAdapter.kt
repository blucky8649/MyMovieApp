package com.example.mymovieapp.adapter

import android.content.Context
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymovieapp.R
import com.example.mymovieapp.databinding.ItemMovieListBinding
import com.example.mymovieapp.model.MovieItem
import com.example.mymovieapp.util.DataParseUtil

class MovieListAdapter(val mContext: Context?) : PagingDataAdapter<MovieItem, MovieListAdapter.MovieViewHolder>(differCallback) {
    companion object{
        private val differCallback = object : DiffUtil.ItemCallback<MovieItem>() {
            override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
                return oldItem.image == newItem.image
            }

            override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemMovieListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class MovieViewHolder(private val binding: ItemMovieListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieItem?){
            binding.apply {
                Glide.with(root)
                    .load(item?.image)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(ivTitleImage)
                itemView.setOnClickListener {
                    onItemClickListener?.let { it(item!!) }
                }
                tvTitle.text = "${DataParseUtil.removeTags(item?.title)}"
                tvRating.text = item?.userRating.toString()
                val text = mContext?.let { it.getString(R.string.directorAndDateInfo, item?.pubDate) }

                tvDirector.text = text
                ratingBar.rating = item?.userRating?.toFloat()!!.div(2)
            }
        }
    }

    private var onItemClickListener: ((MovieItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (MovieItem) -> Unit) {
        onItemClickListener = listener
    }
}