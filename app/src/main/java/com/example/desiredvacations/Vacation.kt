package com.example.desiredvacations

import android.media.Image

data class Vacation(
  val id: Int,
  var name: String,
  var hotelName: String,
  var location: String,
  var moneyNeeded: String,
  var description: String = "No description added yet.",
  // var image:Image
  )