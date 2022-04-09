package com.example.mymovieapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.databinding.ItemSearchKeywordBinding
import com.example.mymovieapp.model.Keyword
import com.example.mymovieapp.model.MovieItem
import com.example.mymovieapp.util.DataParseUtil

class SearchKeywordAdapter: ListAdapter<Keyword, SearchKeywordAdapter.SearchViewHolder>(diffUtil) {
    inner class SearchViewHolder(val binding: ItemSearchKeywordBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Keyword) {
            binding.apply {
                searchTermText.text = item.keyword
                deleteSearchBtn.setOnClickListener {
                    onDeleteBtnClickListener?.let {
                        it(item)
                    }
                }
                constraintSearchItem.setOnClickListener {
                    onSearchKeywordClickListener?.let {
                        it(item)
                    }
                }

            }
        }

    }
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Keyword>() {
            override fun areItemsTheSame(oldItem: Keyword, newItem: Keyword): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Keyword, newItem: Keyword): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemSearchKeywordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    private var onDeleteBtnClickListener: ((Keyword) -> Unit)? = null
    private var onSearchKeywordClickListener: ((Keyword) -> Unit)? = null
    fun setOnDeletedBtnClickListener(listener: (Keyword) -> Unit) {
        onDeleteBtnClickListener = listener
    }
    fun setOnSearchKeywordClickListener(listener: (Keyword) -> Unit) {
        onSearchKeywordClickListener = listener
    }
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(holder.bindingAdapterPosition))
    }
}



