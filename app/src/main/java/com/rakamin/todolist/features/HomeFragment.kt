package com.rakamin.todolist.features

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.rakamin.todolist.R
import com.rakamin.todolist.databinding.FragmentHomeBinding
import com.rakamin.todolist.utils.FragmentExtension.setToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()

        binding.fab.setOnClickListener {
            Snackbar.make(requireView(), "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()

            val modal = BottomSheetFragment()
            parentFragmentManager.let { modal.show(it, BottomSheetFragment.TAG) }
        }
    }

    private fun initToolbar() = with(binding) {
        setToolbar(toolbarMain.toolbar, false)
    }
}
