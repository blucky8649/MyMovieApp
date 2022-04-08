package com.example.mymovieapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovieapp.util.Constants.DELEY_500L
import com.example.mymovieapp.R
import com.example.mymovieapp.adapter.LoaderStateAdapter
import com.example.mymovieapp.adapter.MovieListAdapter
import com.example.mymovieapp.adapter.SearchKeywordAdapter
import com.example.mymovieapp.databinding.FragmentMovieListBinding
import com.example.mymovieapp.ui.MovieActivity
import com.example.mymovieapp.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class MovieListFragment: Fragment(R.layout.fragment_movie_list)
{
    private var _binding: FragmentMovieListBinding? = null
    val binding get() = _binding!!

    val movieAdapter: MovieListAdapter by lazy {
        MovieListAdapter()
    }
    val loadStateAdapter: LoaderStateAdapter by lazy {
        LoaderStateAdapter { movieAdapter.retry() }
    }

    val searchKeywordAdapter: SearchKeywordAdapter by lazy {
        SearchKeywordAdapter()
    }

    val viewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        setupRecyclerView()
        fetchMovieList()
        (activity as MovieActivity).setSupportActionBar(binding.toolbar)

        return binding.root
    }
    fun fetchMovieList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.result
                .collectLatest { movie ->
                    movieAdapter.submitData(viewLifecycleOwner.lifecycle, movie)
                }
        }
    }
    fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = movieAdapter.withLoadStateFooter(loadStateAdapter)
            layoutManager = context?.let { LinearLayoutManager(it) }
        }
        binding.searchHistoryRecyclerView.apply {
            adapter = searchKeywordAdapter
            layoutManager = context?.let { GridLayoutManager(it, 2) }
        }

        movieAdapter.setOnItemClickListener { item ->
            val action = MovieListFragmentDirections.actionMovieListFragmentToMovieWebViewFragment(item)
            findNavController().navigate(action)
        }
        searchKeywordAdapter.submitList(listOf("행인", "어벤져스"))
        searchKeywordAdapter.apply {
            setOnDeletedBtnClickListener { position ->
                context?.let { Toast.makeText(it, "삭제 버튼 눌림", Toast.LENGTH_SHORT).show() }
            }
            setOnSearchKeywordClickListener { position ->
                context?.let { Toast.makeText(it, "키워드 눌림", Toast.LENGTH_SHORT).show() }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
        val mySearchView = menu.findItem(R.id.search_menu_item)?.actionView as SearchView

        mySearchView.apply {
            queryHint = "검색어를 입력해주세요."
            background = resources.getDrawable(R.drawable.layout_rounded, null)

            setOnQueryTextFocusChangeListener{ _, isExpanded ->
                when (isExpanded) {
                    true -> {
                        Log.d("Lee", "서치뷰 열림")
                        binding.linearSearchHistoryView.isVisible = true
                    }
                    false -> {
                        Log.d("Lee", "서치뷰 닫힘")
                        binding.linearSearchHistoryView.isVisible = false
                    }
                }
            }
            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        viewModel.postKeyword(query)
                        binding.toolbar.apply {
                            title = query
                            collapseActionView()
                        }
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }


        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}