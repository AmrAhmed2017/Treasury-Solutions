package com.example.core.base

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.core.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheet<V: ViewBinding> : BottomSheetDialogFragment() {

    open lateinit var binding: V

    abstract fun initBinding(): V
    abstract fun onBottomSheetCreated()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = initBinding()
        onBottomSheetCreated()
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            behavior.isFitToContents = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onStart() {
        super.onStart()
        setHeight()
    }

    open fun setHeight() {
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        sheetContainer.layoutParams.height = getHeight(displayMetrics)
    }

    open fun getHeight(displayMetrics: DisplayMetrics): Int =
        (displayMetrics.heightPixels * .95).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    private val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
}