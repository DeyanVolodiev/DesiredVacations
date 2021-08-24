package com.example.desiredvacations.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.desiredvacations.R
import com.example.desiredvacations.Vacation
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class VacationsAdapter(
    private val context: Context,
    private val vacations: LiveData<MutableList<Vacation>>,
    private val shearedViewModel: MainViewModel
) :
    RecyclerView.Adapter<VacationsAdapter.VacationsViewHolder>() {

    /**
     * QUESTION:
     * When I use LayoutInflater.inflate isn't it supposed to attach all view inside the xml to itemView,
     * why do I have to declare them like this inside VacationsViewHolder?
     */
    class VacationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvVacationName: TextView = itemView.findViewById(R.id.tvVacationName)
        val tvVacationPlace: TextView = itemView.findViewById(R.id.tvVacationPlace)
        val btnVacationsCardDetailsBtn: Button =
            itemView.findViewById(R.id.btnVacationsCardDetailsBtn)
        val cvVacationItem: CardView = itemView.findViewById(R.id.cvVacationItem)
        val ivVacationImage: ImageView = itemView.findViewById(R.id.ivVacationImage)
    }

    override fun onCreateViewHolder(view: ViewGroup, viewType: Int): VacationsViewHolder {
        return VacationsViewHolder(
            LayoutInflater.from(view.context)
                .inflate(R.layout.item_vacation, view, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VacationsViewHolder, position: Int) {
        val currentVacation = vacations.value?.get(position)

        holder.apply {
            ivVacationImage.setImageBitmap(currentVacation?.image)
            tvVacationName.text = currentVacation?.name
            tvVacationPlace.text = "${currentVacation?.hotelName}, ${currentVacation?.location}"

            btnVacationsCardDetailsBtn.setOnClickListener {
                currentVacation?.id?.let { it1 ->
                    navigateToDetailedVacation(it1, holder.itemView)
                }
            }

            cvVacationItem.setOnLongClickListener {
                MaterialAlertDialogBuilder(context)
                    .setTitle("Delete Item?")
                    .setPositiveButton("Delete") { _, _ ->
                        currentVacation?.id?.let { it1 -> shearedViewModel.deleteVacation(it1) }
                        notifyItemRemoved(position)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog?.cancel()
                    }
                    .show()
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return vacations.value?.size ?: 0
    }

    private fun navigateToDetailedVacation(vacationId: Int, view: View) {
        shearedViewModel.setCurrentVacation(vacationId)

        findNavController(view).navigate(R.id.action_mainFragment_to_detailedVacationFragment)
    }
}