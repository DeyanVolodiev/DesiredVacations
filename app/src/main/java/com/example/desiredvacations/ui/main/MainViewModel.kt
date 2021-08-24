package com.example.desiredvacations.ui.main

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.desiredvacations.DatabaseHandler
import com.example.desiredvacations.R
import com.example.desiredvacations.Vacation
import java.io.ByteArrayOutputStream


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val defaultDescription = "No description yet."
    private val defaultImage: Bitmap =
        drawableToBitmap(R.drawable.ic_baseline_default_image.toDrawable())

    private val databaseHandler = DatabaseHandler(context)

    private var lastId: Int

    private val _vacations = MutableLiveData<MutableList<Vacation>>()
    val vacations: LiveData<MutableList<Vacation>> = _vacations

    private val _currentVacation = MutableLiveData<Vacation>()
    val currentVacation: LiveData<Vacation> = _currentVacation

    init {
        val vacationsInDB = databaseHandler.viewVacation()
        _vacations.value = vacationsInDB
        lastId = _vacations.value?.lastIndex!!
    }

    fun setCurrentVacation(id: Int) {
        _currentVacation.value = _vacations.value?.filter { vacation -> vacation.id == id }?.get(0)
    }

    fun addVacation(name: String, hotelName: String, location: String, moneyNeeded: String) {
        lastId = ++lastId
        val newVacation =
            Vacation(
                lastId + 1,
                name,
                hotelName,
                location,
                moneyNeeded,
                defaultDescription,
                defaultImage
            )

        val status = databaseHandler.createVacation(newVacation, defaultImage.toByteArray())
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
        val newVacation = _currentVacation.value?.let {
            Vacation(
                it.id,
                newName,
                newHotelName,
                newLocation,
                newMoneyNeeded,
                newDescription,
                it.image
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
                _vacations.value?.forEach { vacation: Vacation ->
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

    fun setVacationImage(image: Bitmap) {
        val newVacation = _currentVacation.value?.let {
            Vacation(
                it.id,
                it.name,
                it.hotelName,
                it.location,
                it.moneyNeeded,
                it.description,
                image
            )
        }
        val byteArrImg = image.toByteArray()

        val status = _currentVacation.value?.id?.let { databaseHandler.changeImage(it, byteArrImg) }

        if (status != null) {
            if (status > -1) {
                _vacations.value?.forEach { vacation: Vacation ->
                    if (vacation.id == _currentVacation.value?.id) {
                        vacation.image = image
                    }
                }

                _currentVacation.value = newVacation!!
            }
        }
    }

    private fun Bitmap.toByteArray(): ByteArray {
        ByteArrayOutputStream().apply {
            compress(Bitmap.CompressFormat.JPEG, 10, this)
            return toByteArray()
        }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }
        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            )
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}