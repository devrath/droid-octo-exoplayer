package com.example.code.exoplayer.types.styled.ui.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment <VIEW_BINDING: ViewBinding>(
    private val inflate: InflateFragment<VIEW_BINDING>
) : Fragment(){

    var fragmentContext: Context? = null

    private var _binding: VIEW_BINDING? = null
    val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.fragmentContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.fragmentContext?.let {
            fragmentContext = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.fragmentContext?.let {
            fragmentContext = view.context
        }
    }

    override fun onDetach() {
        super.onDetach()

        this.fragmentContext = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
typealias InflateFragment<T> = (LayoutInflater, ViewGroup?, Boolean) -> T