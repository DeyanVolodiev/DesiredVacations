package com.example.desiredvacations.ui.main

import android.media.Image

data class Vacation(
  val id: Int,
  val name: String,
  val hotelName: String,
  val location: String,
  val moneyNeeded: String,
  var description: String = "No description added yet.",
  // var image:Image
  )