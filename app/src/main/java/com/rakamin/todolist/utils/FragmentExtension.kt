package com.rakamin.todolist.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rakamin.todolist.R

object FragmentExtension {
    fun Fragment.setToolbar(
        toolbar: Toolbar,
        isBackVisible: Boolean,
        onBack: () -> Unit = { if (!findNavController().popBackStack()) requireActivity().finish() }
    ) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(isBackVisible)
        if (isBackVisible) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_left)
            toolbar.setNavigationOnClickListener {
                onBack()
            }
        } else toolbar.updatePadding(32, 0, 0, 0)
    }
}
