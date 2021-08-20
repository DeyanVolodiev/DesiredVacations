package com.example.desiredvacations.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.desiredvacations.R

class AddVacationDialog: DialogFragment() {

  /** The system calls this to get the DialogFragment's layout, regardless
  of whether it's being displayed as a dialog or an embedded fragment. */
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    // Inflate the layout to use as dialog or embedded fragment
    return inflater.inflate(R.layout.fragment_add_vacation, container, false)
  }
}