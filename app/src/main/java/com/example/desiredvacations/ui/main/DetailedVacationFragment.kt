package com.example.desiredvacations.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.desiredvacations.databinding.FragmentDetailedVacationBinding


class DetailedVacationFragment : Fragment() {

  private var binding: FragmentDetailedVacationBinding? = null
  private val sharedViewModel: MainViewModel by activityViewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {

    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val fragmentBinding = FragmentDetailedVacationBinding.inflate(inflater, container, false)
    binding = fragmentBinding
    return fragmentBinding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding?.apply {
      // Specify the fragment as the lifecycle owner
      lifecycleOwner = viewLifecycleOwner

      // Assign the view model to a property in the binding class
      viewModel = sharedViewModel
    }
  }
}