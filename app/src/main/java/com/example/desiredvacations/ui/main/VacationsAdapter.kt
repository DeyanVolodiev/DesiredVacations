package com.example.desiredvacations.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.desiredvacations.R

class VacationsAdapter(
  private val vacations: LiveData<MutableList<Vacation>>,
  private val viewModel: MainViewModel
) :
  RecyclerView.Adapter<VacationsAdapter.VacationsViewHolder>() {

  class VacationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvVacationName: TextView = itemView.findViewById(R.id.tvVacationName)
    val tvVacationPlace: TextView = itemView.findViewById(R.id.tvVacationPlace)
    val btnVacationsCardDetailsBtn: Button = itemView.findViewById(R.id.btnVacationsCardDetailsBtn)
  }

  override fun onCreateViewHolder(view: ViewGroup, viewType: Int): VacationsViewHolder {
    return VacationsViewHolder(
      LayoutInflater.from(view.context)
        .inflate(R.layout.item_vacation, view, false)
    )
  }

  override fun onBindViewHolder(holder: VacationsViewHolder, position: Int) {
    val currentVacation = vacations.value?.get(position)

    holder.apply {
      tvVacationName.text = currentVacation?.name
      tvVacationPlace.text = "${currentVacation?.hotelName}, ${currentVacation?.location}"

      btnVacationsCardDetailsBtn.setOnClickListener {
        currentVacation?.id?.let { it ->
          navigateToDetailedVacation(it, holder.itemView)
        }
      }
    }
  }

  override fun getItemCount(): Int {
    return vacations.value?.size ?: 0
  }

  private fun navigateToDetailedVacation(vacationId: Int, view: View) {
    viewModel.setCurrentVacation(vacationId)

    findNavController(view).navigate(R.id.action_mainFragment_to_detailedVacationFragment)
  }
}