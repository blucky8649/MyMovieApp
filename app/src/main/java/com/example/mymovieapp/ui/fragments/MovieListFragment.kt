package com.example.mymovieapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuItemCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.util.Constants.DELEY_500L
import com.example.mymovieapp.R
import com.example.mymovieapp.adapter.LoaderStateAdapter
import com.example.mymovieapp.adapter.MovieListAdapter
import com.example.mymovieapp.adapter.SearchKeywordAdapter
import com.example.mymovieapp.databinding.FragmentMovieListBinding
import com.example.mymovieapp.model.Keyword
import com.example.mymovieapp.ui.MovieActivity
import com.example.mymovieapp.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class MovieListFragment: Fragment(R.layout.fragment_movie_list)
{
    private var _binding: FragmentMovieListBinding? = null
    val binding get() = _binding!!

    var list = ArrayList<Keyword>()

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

    private lateinit var mSearchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        setupRecyclerView()
        fetchMovieList()
        observeIsSearchQueryChanged()
        setupSwitchState()
        observeIsSaveOptionEnabled()
        observeIsDataChanged()
        setupToolbar()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchViewState.collectLatest {
                binding.linearSearchHistoryView.isVisible = it
            }
        }
        return binding.root
    }
    fun setupToolbar() {
        binding.clearSearchHistoryButtton.setOnClickListener {
            viewModel.clearKeywords()
        }

        binding.toolbar.inflateMenu(R.menu.top_menu)
        binding.toolbar.menu.findItem(R.id.search_menu_item)
            .setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
                override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                    viewModel.setSearchViewVisibility(true)
                    return true
                }

                override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                    viewModel.setSearchViewVisibility(false)
                    return true
                }

            })
        mSearchView = binding.toolbar.menu.findItem(R.id.search_menu_item)?.actionView as SearchView
        mSearchView.apply {
            queryHint = "검색어를 입력해주세요."
            background = ResourcesCompat.getDrawable(resources,R.drawable.layout_rounded, null)

            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        if (list.size == 10) viewModel.deleteKeyword(list[list.size - 1])
                        sendRequest(it)
                        viewModel.addSearchData(Keyword(keyword =  query))
                    }
                    viewModel.setSearchViewVisibility(false)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }
    }
    fun observeIsSearchQueryChanged() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchQuery.collectLatest { query ->
                binding.toolbar.title = query
            }
        }
    }
    fun setupSwitchState() {
        binding.searchHistoryModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setSaveMode(isChecked)
        }
    }
    fun observeIsDataChanged() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getSavedKeywords().collectLatest {
                list = it.toMutableList() as ArrayList<Keyword>
                Log.d("ListTest","$it")
                searchKeywordAdapter.submitList(it.toMutableList())
            }
        }
    }
    fun observeIsSaveOptionEnabled() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isSaveEnabled.collectLatest { isEnabled ->
                binding.searchHistoryModeSwitch.isChecked = isEnabled
            }
        }
    }

    fun fetchMovieList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.result.collectLatest { movie ->
                    binding.recyclerView.scrollToPosition(0)
                    movieAdapter.submitData(viewLifecycleOwner.lifecycle, movie)
                }
        }
    }
    fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = movieAdapter.withLoadStateFooter(loadStateAdapter)
            layoutManager = context?.let { LinearLayoutManager(it) }
            addItemDecoration(DividerItemDecoration(context, 1))
        }
        binding.searchHistoryRecyclerView.apply {
            adapter = searchKeywordAdapter
            scrollToPosition(0)
            itemAnimator = null
            layoutManager = context?.let { GridLayoutManager(it, 2) }
        }

        movieAdapter.setOnItemClickListener { item ->
            val action = MovieListFragmentDirections.actionMovieListFragmentToMovieWebViewFragment(item)
            findNavController().navigate(action)
        }

        searchKeywordAdapter.apply {
            setOnDeletedBtnClickListener { item ->
                //context?.let { Toast.makeText(it, "삭제됨 ${item.keyword}", Toast.LENGTH_SHORT).show() }
                viewModel.deleteKeyword(item)

            }
            setOnSearchKeywordClickListener { item ->
                //context?.let { Toast.makeText(it, "키워드 눌림 ${item.keyword}", Toast.LENGTH_SHORT).show() }
                sendRequest(item.keyword)
            }
            registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    binding.searchHistoryRecyclerView.smoothScrollToPosition(0)
                }
            })
        }
    }
    fun sendRequest(query: String) {
        viewModel.postKeyword(query)
        binding.toolbar.apply {
            title = query
            collapseActionView()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
