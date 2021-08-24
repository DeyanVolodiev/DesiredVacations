package com.example.desiredvacations

import android.content.Context
import android.graphics.Bitmap

data class Vacation(
  val id: Int,
  var name: String,
  var hotelName: String,
  var location: String,
  var moneyNeeded: String,
  var description: String,
  var image: Bitmap
)