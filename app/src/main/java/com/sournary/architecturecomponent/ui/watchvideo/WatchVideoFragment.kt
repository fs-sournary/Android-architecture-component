package com.sournary.architecturecomponent.ui.watchvideo

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.sournary.architecturecomponent.BuildConfig
import com.sournary.architecturecomponent.R
import com.sournary.architecturecomponent.databinding.FragmentWatchVideoBinding
import com.sournary.architecturecomponent.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_watch_video.*

/**
 * The class represent the screen that allow us watch trailer or clip video of a movie.
 */
class WatchVideoFragment : BaseFragment<FragmentWatchVideoBinding, WatchVideoViewModel>() {

    private var youtubeKey: String = ""

    override val viewModel: WatchVideoViewModel by viewModels()

    override val layoutId: Int = R.layout.fragment_watch_video

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val safeArgs: WatchVideoFragmentArgs by navArgs()
        youtubeKey = safeArgs.key
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupVideo()
        YouTubePlayerSupportFragment.newInstance()
    }

    private fun setupVideo() {
        val initializedListener = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                youtubePlayer: YouTubePlayer?,
                b: Boolean
            ) {
                youtubePlayer?.loadVideo(youtubeKey)
            }

            override fun onInitializationFailure(
                provider: YouTubePlayer.Provider?,
                result: YouTubeInitializationResult?
            ) {
                // Ignored
            }
        }
        video_player.initialize(BuildConfig.YOUTUBE_API_KEY, initializedListener)
    }

}
