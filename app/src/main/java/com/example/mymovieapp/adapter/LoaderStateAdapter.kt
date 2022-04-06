package com.example.mymovieapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.databinding.ItemMovieLoaderBinding

class LoaderStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoaderStateAdapter.LoaderViewHolder>() {

    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder {
        return LoaderViewHolder(
            ItemMovieLoaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), retry
        )

    }

    class LoaderViewHolder(private val binding: ItemMovieLoaderBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener {
                retry()
            }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Loading) {
                binding.mlLoader.transitionToEnd()
            } else {
                binding.mlLoader.transitionToStart()
            }
        }
    }
}