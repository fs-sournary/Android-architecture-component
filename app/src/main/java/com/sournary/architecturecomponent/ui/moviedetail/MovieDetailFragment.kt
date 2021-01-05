package com.sournary.architecturecomponent.ui.moviedetail

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.sournary.architecturecomponent.R
import com.sournary.architecturecomponent.databinding.FragmentMovieDetailBinding
import com.sournary.architecturecomponent.ext.autoCleared
import com.sournary.architecturecomponent.ui.MainViewModel
import com.sournary.architecturecomponent.ui.common.BaseFragment
import com.sournary.architecturecomponent.ui.common.NetworkStateAdapter
import com.sournary.architecturecomponent.util.EdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import kotlinx.android.synthetic.main.layout_network_state.*

/**
 * The class represent movie detail screen.
 */
@AndroidEntryPoint
class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding, MovieDetailViewModel>() {

    private var movieId: Int = 0

    private var relatedMovieAdapter by autoCleared<RelatedMovieAdapter>()

    private val mainViewModel: MainViewModel by activityViewModels()
    override val viewModel: MovieDetailViewModel by viewModels()

    override val layoutId: Int = R.layout.fragment_movie_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val safeArgs: MovieDetailFragmentArgs by navArgs()
        movieId = safeArgs.movieId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWindow(view)
        setupNavigation()
        setupRelatedMovieList()
        setupViewModel()
    }

    private fun setupWindow(view: View){
        view.setOnApplyWindowInsetsListener { _, insets ->
            EdgeToEdge.passWindowInsetsToChildrenRegularLayout(view as ViewGroup, insets)
            binding.closeImage.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topMargin = insets.systemWindowInsetTop
            }
            binding.networkStateLayout.updatePadding(top = insets.systemWindowInsetTop)
            binding.movieDetailScroll.updatePadding(bottom = insets.systemWindowInsetBottom)
            insets
        }
    }

    private fun setupNavigation() {
        binding.closeImage.setOnClickListener {
            navController.popBackStack()
        }
        binding.layoutNetworkState.errorCloseImage.setOnClickListener {
            navController.popBackStack()
        }
        binding.layoutNetworkState.retryButton.setOnClickListener {
            viewModel.retryGetMovie(movieId)
        }
        binding.visitSiteButton.setOnClickListener {
            val title = viewModel.movie.value?.title ?: return@setOnClickListener
            val url = viewModel.movie.value?.homepage ?: return@setOnClickListener
            if (url.isEmpty() || title.isEmpty()) return@setOnClickListener
            val directions = MovieDetailFragmentDirections.navigateToWebsite(title, url)
            navController.navigate(directions)
        }
    }

    private fun setupRelatedMovieList() {
        relatedMovieAdapter = RelatedMovieAdapter()
        binding.relatedMovieRecycler.adapter = relatedMovieAdapter.withLoadStateHeaderAndFooter(
            header = NetworkStateAdapter { relatedMovieAdapter.retry() },
            footer = NetworkStateAdapter { relatedMovieAdapter.retry() }
        )
    }

    private fun setupViewModel() {
        viewModel.setMovieId(movieId)
        mainViewModel.setLockNavigation(true)
        // viewModel.relatedMovies.observe(viewLifecycleOwner) {
        //     relatedMovieAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        // }
    }

}
