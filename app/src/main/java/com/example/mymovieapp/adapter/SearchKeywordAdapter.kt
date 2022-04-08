package com.example.mymovieapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.databinding.ItemSearchKeywordBinding

class SearchKeywordAdapter: ListAdapter<String, SearchKeywordAdapter.SearchViewHolder>(diffUtil) {
    inner class SearchViewHolder(val binding: ItemSearchKeywordBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, position: Int) {
            binding.apply {
                searchTermText.text = item
                deleteSearchBtn.setOnClickListener {
                    onDeleteBtnClickListener?.let {
                        it(position)
                    }
                }
                constraintSearchItem.setOnClickListener {
                    onSearchKeywordClickListener?.let {
                        it(position)
                    }
                }

            }
        }

    }
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
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
    private var onDeleteBtnClickListener: ((Int) -> Unit)? = null
    private var onSearchKeywordClickListener: ((Int) -> Unit)? = null
    fun setOnDeletedBtnClickListener(listener: (Int) -> Unit) {
        onDeleteBtnClickListener = listener
    }
    fun setOnSearchKeywordClickListener(listener: (Int) -> Unit) {
        onSearchKeywordClickListener = listener
    }
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

}



