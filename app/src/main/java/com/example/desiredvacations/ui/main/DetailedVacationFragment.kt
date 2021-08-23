package com.example.desiredvacations.ui.main

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.desiredvacations.R
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
    binding?.detailedVacationFragment = this

    binding?.apply {
      // Specify the fragment as the lifecycle owner
      lifecycleOwner = viewLifecycleOwner

      // Assign the view model to a property in the binding class
      viewModel = sharedViewModel
    }
  }

  fun openEditDialog() {
    val updateDialog = Dialog(requireContext())

    updateDialog.setContentView(R.layout.fragment_edit_vacation)

    val editVacationDialogNameEditText =
      updateDialog.findViewById<EditText>(R.id.editVacationDialogNameEditText)
    val editVacationDialogHotelNameEditText =
      updateDialog.findViewById<EditText>(R.id.editVacationDialogHotelNameEditText)
    val editVacationDialogLocationEditText =
      updateDialog.findViewById<EditText>(R.id.editVacationDialogLocationEditText)
    val editVacationDialogDescriptionEditText =
      updateDialog.findViewById<EditText>(R.id.editVacationDialogDescriptionEditText)
    val editVacationDialogPriceEditText =
      updateDialog.findViewById<EditText>(R.id.editVacationDialogPriceEditText)
    val btnEditVacation = updateDialog.findViewById<Button>(R.id.btnEditVacation)

    editVacationDialogNameEditText.setText(sharedViewModel.currentVacation.value?.name)
    editVacationDialogHotelNameEditText.setText(sharedViewModel.currentVacation.value?.hotelName)
    editVacationDialogLocationEditText.setText(sharedViewModel.currentVacation.value?.location)
    editVacationDialogDescriptionEditText.setText(sharedViewModel.currentVacation.value?.description)
    editVacationDialogPriceEditText.setText(sharedViewModel.currentVacation.value?.moneyNeeded)

    updateDialog.show()

    btnEditVacation.setOnClickListener {
      val isInputValid: Boolean =
        editVacationDialogNameEditText.text.isNotEmpty()
            && editVacationDialogHotelNameEditText.text.isNotEmpty()
            && editVacationDialogLocationEditText.text.isNotEmpty()
            && editVacationDialogDescriptionEditText.text.isNotEmpty()
            && editVacationDialogPriceEditText.text.isNotEmpty()

      if (isInputValid) {
        sharedViewModel.editVacation(
          editVacationDialogNameEditText.text.toString(),
          editVacationDialogHotelNameEditText.text.toString(),
          editVacationDialogLocationEditText.text.toString(),
          editVacationDialogDescriptionEditText.text.toString(),
          editVacationDialogPriceEditText.text.toString()
        )
        Toast.makeText(requireContext(), "Vacation Edited!", Toast.LENGTH_SHORT).show()
        updateDialog.dismiss()
      } else {
        if (editVacationDialogNameEditText.text.isEmpty()) {
          editVacationDialogNameEditText.error = "Input Vacation Name Please"
        }
        if (editVacationDialogHotelNameEditText.text.isEmpty()) {
          editVacationDialogHotelNameEditText.error = "Input Hotel Name Please"
        }
        if (editVacationDialogLocationEditText.text.isEmpty()) {
          editVacationDialogLocationEditText.error = "Input Location Please"
        }
        if (editVacationDialogDescriptionEditText.text.isEmpty()) {
          editVacationDialogDescriptionEditText.error = "Input Price Please"
        }
        if (editVacationDialogPriceEditText.text.isEmpty()) {
          editVacationDialogPriceEditText.error = "Input Description Please"
        }
      }
    }
  }
}