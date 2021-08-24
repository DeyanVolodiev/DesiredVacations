package com.example.desiredvacations.ui.main

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.desiredvacations.R
import com.example.desiredvacations.databinding.FragmentDetailedVacationBinding


class DetailedVacationFragment : Fragment() {

    private var binding: FragmentDetailedVacationBinding? = null
    private val sharedViewModel: MainViewModel by activityViewModels()

    companion object {
        const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 1001
        const val PICK_IMAGE_REQUEST_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentDetailedVacationBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        binding!!.ivCurrentVacationImage.setImageBitmap(sharedViewModel.currentVacation.value?.image)

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

    fun pickImage() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    pickImage()
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICK_IMAGE_REQUEST_CODE -> {
                if (resultCode != Activity.RESULT_OK) {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                val uri = data?.data
                if (uri != null) {
                    sharedViewModel.setVacationImage(uriToBitmap(uri))
                    binding!!.ivCurrentVacationImage.setImageBitmap(sharedViewModel.currentVacation.value?.image)
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    fun uriToBitmap(selectedPhotoUri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(
                requireActivity().contentResolver,
                selectedPhotoUri
            )
        } else {
            val source =
                ImageDecoder.createSource(requireActivity().contentResolver, selectedPhotoUri)
            ImageDecoder.decodeBitmap(source)
        }
    }
}