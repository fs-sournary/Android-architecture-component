package com.sournary.architecturecomponent.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.paging.LoadState
import com.google.android.material.chip.Chip
import com.sournary.architecturecomponent.R
import com.sournary.architecturecomponent.databinding.FragmentHomeBinding
import com.sournary.architecturecomponent.ext.autoCleared
import com.sournary.architecturecomponent.ext.hideKeyboard
import com.sournary.architecturecomponent.model.Genre
import com.sournary.architecturecomponent.ui.MainViewModel
import com.sournary.architecturecomponent.ui.common.BaseFragment
import com.sournary.architecturecomponent.ui.common.NetworkStateAdapter
import com.sournary.architecturecomponent.widget.MovieItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

/**
 * The Fragment represents home screen.
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    private var adapter by autoCleared<MovieListAdapter>()

    private val mainViewModel: MainViewModel by activityViewModels()
    override val viewModel: HomeViewModel by viewModels()

    override val layoutId: Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWindow(view)
        setupSearch()
        setupMovieList()
        setupViewModel()
    }

    private fun setupWindow(view: View) {
        view.setOnApplyWindowInsetsListener { _, insets ->
            view.updatePadding(top = insets.systemWindowInsetTop)
            binding.movieRecycler.updatePadding(bottom = insets.systemWindowInsetBottom)
            insets
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSearch() {
        binding.searchTextInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                getMoviesFromSearch()
                true
            } else {
                false
            }
        }
        binding.searchTextInput.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                getMoviesFromSearch()
                true
            } else {
                false
            }
        }
        binding.searchTextInput.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP &&
                event.x <= binding.searchTextInput.compoundDrawables[0].bounds.width()
            ) {
                mainViewModel.openNavigation()
                true
            } else {
                false
            }
        }
        binding.retryButton.setOnClickListener {
            viewModel.retryGetMovies()
        }
        binding.genreGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.checkId = checkedId
        }
    }

    private fun getMoviesFromSearch() {
        hideKeyboard()
        val searchText = binding.searchTextInput.text?.trim() ?: return
        viewModel.genres.value?.forEach { genre ->
            if (genre.name == searchText) {
                viewModel.showGenreMovies(genre)
            }
        }
    }

    private fun setupMovieList() {
        binding.movieSwipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }
        adapter = MovieListAdapter { movie ->
            val directions = HomeFragmentDirections.navigateToMovieDetail(movie.id)
            navController.navigate(directions)
        }
        binding.movieRecycler.adapter = adapter.withLoadStateHeaderAndFooter(
            header = NetworkStateAdapter { adapter.retry() },
            footer = NetworkStateAdapter { adapter.retry() }
        )
        val divider =
            ContextCompat.getDrawable(context ?: return, R.drawable.shape_movie_divider) ?: return
        val horizontalSpacing = resources.getDimensionPixelOffset(R.dimen.dp_16)
        val itemDecoration = MovieItemDecoration(horizontalSpacing, divider)
        binding.movieRecycler.addItemDecoration(itemDecoration)
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest {
                binding.movieSwipeRefresh.isRefreshing = it.refresh is LoadState.Loading
                binding.retryButton.isVisible = it.refresh is LoadState.Error
                binding.progress.isVisible = it.refresh is LoadState.Loading
                binding.movieRecycler.isVisible =
                    it.refresh !is LoadState.Error && it.refresh is LoadState.NotLoading
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collectLatest { binding.movieRecycler.scrollToPosition(0) }
        }
    }

    private fun setupViewModel() {
        mainViewModel.setLockNavigation(false)
        viewModel.apply {
            movies.observe(viewLifecycleOwner) {
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
            genres.observe(viewLifecycleOwner) {
                addGenres(it)
            }
        }
    }

    private fun addGenres(genres: List<Genre>) {
        binding.genreGroup.removeAllViews()
        genres.forEach { genre -> binding.genreGroup.addView(getChip(genre)) }
        binding.genreGroup.check(viewModel.checkId)
    }

    private fun getChip(genre: Genre): Chip {
        val chip = layoutInflater.inflate(
            R.layout.layout_genre_item, binding.genreGroup, false
        ) as Chip
        chip.text = genre.name
        chip.id = genre.id
        chip.setOnClickListener {
            hideKeyboard()
            chip.requestFocus()
            binding.searchTextInput.setText(genre.name)
            if (binding.searchTextInput.text?.trim() != genre.name) {
                viewModel.showGenreMovies(genre)
            }
        }
        return chip
    }
}
