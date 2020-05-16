package com.sournary.architecturecomponent.ui.moviedetail

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.sournary.architecturecomponent.R
import com.sournary.architecturecomponent.databinding.FragmentMovieDetailBinding
import com.sournary.architecturecomponent.ext.autoCleared
import com.sournary.architecturecomponent.ui.common.BaseFragment
import com.sournary.architecturecomponent.ui.common.MainViewModel
import com.sournary.architecturecomponent.ui.common.NetworkStateAdapter
import com.sournary.architecturecomponent.util.EdgeToEdge
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import kotlinx.android.synthetic.main.layout_network_state.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * The class represent movie detail screen.
 */
@FlowPreview
@ExperimentalCoroutinesApi
class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding, MovieDetailViewModel>() {

    private var movieId: Int = 0

    private var relatedMovieAdapter by autoCleared<RelatedMovieAdapter>()

    private val mainViewModel: MainViewModel by activityViewModels()
    override val viewModel: MovieDetailViewModel by viewModels { MovieDetailViewModelFactory(movieId) }

    override val layoutId: Int = R.layout.fragment_movie_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val safeArgs: MovieDetailFragmentArgs by navArgs()
        movieId = safeArgs.movieId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnApplyWindowInsetsListener { _, insets ->
            EdgeToEdge.passWindowInsetsToChildrenRegularLayout(view as ViewGroup, insets)
            close_image.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topMargin = insets.systemWindowInsetTop
            }
            network_state_layout.updatePadding(top = insets.systemWindowInsetTop)
            movie_detail_scroll.updatePadding(bottom = insets.systemWindowInsetBottom)
            insets
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupNavigation()
        setupRelatedMovieList()
        setupViewModel()
    }

    private fun setupNavigation() {
        close_image.setOnClickListener {
            navController.popBackStack()
        }
        error_close_image.setOnClickListener {
            navController.popBackStack()
        }
        retry_button.setOnClickListener {
            viewModel.retryGetMovie()
        }
        visit_site_button.setOnClickListener {
            val title = viewModel.movie.value?.title ?: return@setOnClickListener
            val url = viewModel.movie.value?.homepage ?: return@setOnClickListener
            if (url.isEmpty() || title.isEmpty()) return@setOnClickListener
            val directions = MovieDetailFragmentDirections.navigateToWebsite(title, url)
            navController.navigate(directions)
        }
    }

    private fun setupRelatedMovieList() {
        relatedMovieAdapter = RelatedMovieAdapter()
        related_movie_recycler.adapter = relatedMovieAdapter.withLoadStateHeaderAndFooter(
            header = NetworkStateAdapter { relatedMovieAdapter.retry() },
            footer = NetworkStateAdapter { relatedMovieAdapter.retry() }
        )
    }

    private fun setupViewModel() {
        mainViewModel.setLockNavigation(true)
        viewModel.apply {
            relatedMovies.observe(viewLifecycleOwner) {
                relatedMovieAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
    }

}
