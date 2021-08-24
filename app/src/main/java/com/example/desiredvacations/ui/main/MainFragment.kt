package com.example.desiredvacations.ui.main

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desiredvacations.R
import com.example.desiredvacations.databinding.MainFragmentBinding

class MainFragment() : Fragment() {

    private var binding: MainFragmentBinding? = null
    private val sharedViewModel: MainViewModel by activityViewModels()
    private lateinit var vacationsAdapter: VacationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

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

        vacationsAdapter =
            VacationsAdapter(requireContext(), sharedViewModel.vacations, sharedViewModel)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_notify -> {
                // navigate to notification fragment
                findNavController().navigate(R.id.action_mainFragment_to_notificationsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun openAddVacationDialog() {
        Log.e("vacs", "${sharedViewModel.vacations.value}")

        val addDialog = Dialog(requireContext())

        addDialog.setContentView(R.layout.fragment_add_vacation)
        /**
         * QUESTION:
         * Here I'm declaring variables for every view inside the layout, because it couldn't find them.
         * Aren't the views supposed to be accessible from the updateDialog object after I use the setContentView() function?
         */
        val addVacationDialogNameEditText =
            addDialog.findViewById<EditText>(R.id.addVacationDialogNameEditText)
        val addVacationDialogHotelNameEditText =
            addDialog.findViewById<EditText>(R.id.addVacationDialogHotelNameEditText)
        val addVacationDialogLocationEditText =
            addDialog.findViewById<EditText>(R.id.addVacationDialogLocationEditText)
        val addVacationDialogPriceEditText =
            addDialog.findViewById<EditText>(R.id.addVacationDialogPriceEditText)
        val btnCreateVacation = addDialog.findViewById<Button>(R.id.btnCreateVacation)

        addDialog.show()

        btnCreateVacation.setOnClickListener {
            val isInputValid: Boolean =
                addVacationDialogNameEditText.text.isNotEmpty()
                    && addVacationDialogHotelNameEditText.text.isNotEmpty()
                    && addVacationDialogLocationEditText.text.isNotEmpty()
                    && addVacationDialogPriceEditText.text.isNotEmpty()

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
                Toast.makeText(requireContext(), "Vacation Added!", Toast.LENGTH_SHORT).show()
                // Hiding informational textView when vacations size > 0
                if (sharedViewModel.vacations.value?.size!! > 0) {
                    binding?.tvNoVacationsYet?.visibility = View.GONE
                }
                addDialog.dismiss()
            } else {
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
            }
        }
    }
}