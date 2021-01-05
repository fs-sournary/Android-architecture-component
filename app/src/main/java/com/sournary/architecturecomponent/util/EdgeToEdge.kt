package com.sournary.architecturecomponent.util

import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import androidx.core.view.updatePadding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import com.sournary.architecturecomponent.R

/**
 * The class setup edge to edge implementation.
 * By default, only material layouts like DrawerLayout, CoordinatorLayout...can make your child
 * pass window insets. Otherwise, regular layouts like FrameLayout, LinearLayout, RelativeLayout,
 * CoordinatorLayout...do not pass window insets to their children.
 */
object EdgeToEdge {

    /**
     * The method configures a root view of an Activity in edge-to-edge display.
     * @param root A root view of a Activity.
     */
    fun setupRoot(root: ViewGroup) {
        root.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    /**
     * The method configures an app bar and tool bar in edge-to-edge display.
     * @param appbar An [AppBarLayout].
     * @param toolbar An [Toolbar].
     */
    fun setupAppBar(appbar: AppBarLayout, toolbar: Toolbar) {
        val toolbarPadding = toolbar.resources.getDimensionPixelSize(R.dimen.dp_16)
        appbar.setOnApplyWindowInsetsListener { _, insets ->
            appbar.updatePadding(top = insets.systemWindowInsetTop)
            toolbar.updatePadding(
                left = toolbarPadding + insets.systemWindowInsetLeft,
                right = insets.systemWindowInsetRight
            )
            insets
        }
    }

    /**
     * The method configures a scrolling content in edge-to-edge display.
     * @param scrollingContent A scrolling ViewGroup. This is typically a RecyclerView or a
     * ScrollView. It should be as wide as the screen and should touch the bottom edge of the
     * screen.
     */
    fun setupScrollingContent(scrollingContent: ViewGroup) {
        val originalLeftPadding = scrollingContent.paddingLeft
        val originalRightPadding = scrollingContent.paddingRight
        val originalBottomPadding = scrollingContent.paddingBottom
        scrollingContent.setOnApplyWindowInsetsListener { _, insets ->
            scrollingContent.updatePadding(
                left = originalLeftPadding + insets.systemWindowInsetLeft,
                right = originalRightPadding + insets.systemWindowInsetRight,
                bottom = originalBottomPadding + insets.systemWindowInsetBottom
            )
            insets
        }
    }

    /**
     * The method configures a NavigationView of a DrawerLayout in edge-to-edge display in the case
     * that navigation view doesn't have header layout.
     * @param navigation A [NavigationView] of a DrawerLayout. It should be fit entire screen, has
     * a padding to top edge and touch the bottom edge of the screen.
     */
    fun setupNavigationView(navigation: NavigationView) {
        navigation.setOnApplyWindowInsetsListener { _, insets ->
            navigation.updatePadding(
                top = insets.systemWindowInsetTop,
                left = insets.systemWindowInsetLeft
            )
            insets
        }
    }

    /**
     * The method makes regular layouts can pass window insets to their children.
     * @param viewGroup A root view of regular layouts like FrameLayout, LinearLayout, RelativeLayout,
     * ConstraintLayout.
     * @param insets the [WindowInsets] that we need to pass children of regular layouts.
     */
    fun passWindowInsetsToChildrenRegularLayout(viewGroup: ViewGroup, insets: WindowInsets) {
        if (viewGroup is ConstraintLayout ||
            viewGroup is FrameLayout ||
            viewGroup is LinearLayout ||
            viewGroup is RelativeLayout
        ) {
            viewGroup.forEach { childrenView ->
                childrenView.dispatchApplyWindowInsets(insets)
            }
        }
    }

}
