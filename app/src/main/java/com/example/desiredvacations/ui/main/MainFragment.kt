package com.example.desiredvacations.ui.main

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.desiredvacations.databinding.MainFragmentBinding
import androidx.fragment.app.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desiredvacations.R


class MainFragment() : Fragment() {

  private var binding: MainFragmentBinding? = null
  private val sharedViewModel: MainViewModel by activityViewModels()
  private lateinit var vacationsAdapter: VacationsAdapter

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val fragmentBinding = MainFragmentBinding.inflate(inflater, container, false)
    binding = fragmentBinding
    return fragmentBinding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding?.mainFragment = this

    vacationsAdapter = VacationsAdapter(requireContext() ,sharedViewModel.vacations, sharedViewModel)

    binding?.tvNoVacationsYet?.visibility =
      if (sharedViewModel.vacations.value?.size == 0 || sharedViewModel.vacations.value?.size == null) View.VISIBLE else View.GONE
    binding?.rvVacationsList?.layoutManager = LinearLayoutManager(requireContext())
    binding?.rvVacationsList?.adapter = vacationsAdapter

    binding?.apply {
      // Specify the fragment as the lifecycle owner
      lifecycleOwner = viewLifecycleOwner

      // Assign the view model to a property in the binding class
      viewModel = sharedViewModel
    }
  }

  /**
   * This fragment lifecycle method is called when the view hierarchy associated with the fragment
   * is being removed. As a result, clear out the binding object.
   */
  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
  }

  fun openAddVacationDialog() {
    Log.e("vacs", "${ sharedViewModel.vacations.value }")

    val updateDialog = Dialog(requireContext())

    updateDialog.setContentView(R.layout.fragment_add_vacation)
    /**
     * QUESTION:
     * Here I'm declaring variables for every view inside the layout, because it couldn't find them.
     * Aren't the views supposed to be accessible from the updateDialog object after I use the inflate() function?
     */
    val addVacationDialogNameEditText =
      updateDialog.findViewById<EditText>(R.id.addVacationDialogNameEditText)
    val addVacationDialogHotelNameEditText =
      updateDialog.findViewById<EditText>(R.id.addVacationDialogHotelNameEditText)
    val addVacationDialogLocationEditText =
      updateDialog.findViewById<EditText>(R.id.addVacationDialogLocationEditText)
    val addVacationDialogPriceEditText =
      updateDialog.findViewById<EditText>(R.id.addVacationDialogPriceEditText)
    val btnCreateVacation = updateDialog.findViewById<Button>(R.id.btnCreateVacation)

    btnCreateVacation.setOnClickListener {
      val isInputValid: Boolean =
        addVacationDialogNameEditText.text.isNotEmpty()
            && addVacationDialogHotelNameEditText.text.isNotEmpty()
            && addVacationDialogLocationEditText.text.isNotEmpty()
            && addVacationDialogPriceEditText.text.isNotEmpty()

      if (addVacationDialogNameEditText.text.isEmpty()) {
        addVacationDialogNameEditText.error = "Input Vacation Name Please"
      }
      if (addVacationDialogHotelNameEditText.text.isEmpty()) {
        addVacationDialogHotelNameEditText.error = "Input Hotel Name Please"
      }
      if (addVacationDialogLocationEditText.text.isEmpty()) {
        addVacationDialogLocationEditText.error = "Input Location Please"
      }
      if (addVacationDialogPriceEditText.text.isEmpty()) {
        addVacationDialogPriceEditText.error = "Input Price Please"
      }

      if (isInputValid) {
        sharedViewModel.addVacation(
          addVacationDialogNameEditText.text.toString(),
          addVacationDialogHotelNameEditText.text.toString(),
          addVacationDialogLocationEditText.text.toString(),
          addVacationDialogPriceEditText.text.toString()
        )
        sharedViewModel.vacations.value?.size?.let { it ->
          binding?.rvVacationsList?.adapter?.notifyItemInserted(
            it
          )
        }
        Toast.makeText(requireContext() ,"Vacation Added!",Toast.LENGTH_SHORT).show()
        updateDialog.dismiss()
      }
    }
    updateDialog.show()
  }
}