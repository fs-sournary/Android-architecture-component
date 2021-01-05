package com.sournary.architecturecomponent.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.sournary.architecturecomponent.BR
import com.sournary.architecturecomponent.ext.autoCleared

/**
 * The class is the base for app's Fragment.
 * The layout file's fragment should contain a `viewModel` variable in <layout> tag.
 *
 * @param B Binding object of layout file.
 * @param VM ViewModel is only used for this Fragment.
 */
abstract class BaseFragment<B : ViewDataBinding, VM : ViewModel> : Fragment() {

    protected val navController: NavController by lazy { findNavController() }
    protected var binding by autoCleared<B>()

    protected abstract val viewModel: VM

    @get:LayoutRes
    protected abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setVariable(BR.viewModel, viewModel)
            lifecycleOwner = viewLifecycleOwner
        }
    }
}
