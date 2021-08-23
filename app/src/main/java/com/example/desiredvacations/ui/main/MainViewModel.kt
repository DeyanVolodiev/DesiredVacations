package com.example.desiredvacations.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.desiredvacations.DatabaseHandler
import com.example.desiredvacations.Vacation

class MainViewModel(application: Application) : AndroidViewModel(application) {

  private val context = getApplication<Application>().applicationContext
  private val databaseHandler = DatabaseHandler(context)

  private var lastId: Int

  private val _vacations = MutableLiveData<MutableList<Vacation>>()
  val vacations: LiveData<MutableList<Vacation>> = _vacations

  private val _currentVacation = MutableLiveData<Vacation>()
  val currentVacation: LiveData<Vacation> = _currentVacation

  init {
    val vacationsInDB = databaseHandler.viewVacation()
    _vacations.value = vacationsInDB
    lastId = vacationsInDB.lastIndex
  }

  fun setCurrentVacation(id: Int) {
    _currentVacation.value = _vacations.value?.filter { vacation -> vacation.id == id }?.get(0)
  }

  fun addVacation(name: String, hotelName: String, location: String, moneyNeeded: String) {
    lastId = ++lastId
    val newVacation = Vacation(lastId + 1, name, hotelName, location, moneyNeeded)

    val status = databaseHandler.createVacation(newVacation)
    if (status > -1) {
      _vacations.value?.add(newVacation)
    }
  }

  fun editVacation(
    newName: String,
    newHotelName: String,
    newLocation: String,
    newDescription: String,
    newMoneyNeeded: String
  ) {
    val newVacation = _currentVacation.value?.id?.let {
      Vacation(
        it,
        newName,
        newHotelName,
        newLocation,
        newMoneyNeeded,
        newDescription
      )
    }

    val status =
      _currentVacation.value?.id?.let {
        databaseHandler.updateVacations(
          it,
          newName,
          newHotelName,
          newLocation,
          newMoneyNeeded,
          newDescription
        )
      }

    if (status != null) {
      if (status > -1) {
        _vacations.value?.forEach{ vacation: Vacation ->
          if (vacation.id == _currentVacation.value?.id) {
            vacation.name = newName
            vacation.hotelName = newHotelName
            vacation.location = newLocation
            vacation.moneyNeeded = newMoneyNeeded
            vacation.description = newDescription
          }
        }
        _currentVacation.value = newVacation!!
      }
    }
  }

  fun deleteVacation(id: Int) {
    if (lastId == id) {
      lastId = _vacations.value?.get(_vacations.value!!.lastIndex - 1)?.id ?: -1
    }

    val status = databaseHandler.deleteVacation(id)
    if (status > -1) {
      _vacations.value =
        _vacations.value?.filter { vacation -> vacation.id != id } as MutableList<Vacation>
    }
  }
}