package com.example.desiredvacations.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
  private var lastIndex: Int = 0

  private val _vacations = MutableLiveData<MutableList<Vacation>>()
  val vacations: LiveData<MutableList<Vacation>> = _vacations

  private val _currentVacation = MutableLiveData<Vacation>()
  val currentVacation: LiveData<Vacation> = _currentVacation

  init {
    // Set initial values
    // TODO get vacations list from the db
    _vacations.value = mutableListOf(
      Vacation(
        lastIndex,
        "Vacation Name",
        "Some Hotel",
        "Some City",
        "0.0"
      )
    )
  }

  fun addVacation(name: String, hotelName: String, location: String, moneyNeeded: String) {
    lastIndex = ++lastIndex
    _vacations.value?.add(Vacation(lastIndex, name, hotelName, location, moneyNeeded))


    // TODO add to the new vacation to DB
  }

  fun setCurrentVacation(id: Int) {
    _currentVacation.value = _vacations.value?.get(id)
  }
}