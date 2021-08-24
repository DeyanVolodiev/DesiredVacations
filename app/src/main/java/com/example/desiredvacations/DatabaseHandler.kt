package com.example.desiredvacations

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context) :
  SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
  companion object {
    private const val DATABASE_VERSION = 6
    private const val DATABASE_NAME = "Desired Vacations"

    private const val TABLE_DESIRED_VACATIONS = "DesiredVacations"

    private const val KEY_ID = "_id"
    private const val KEY_NAME = "name"
    private const val KEY_HOTEL_NAME = "hotelName"
    private const val KEY_LOCATION = "location"
    private const val KEY_MONEY_NEEDED = "moneyNeeded"
    private const val KEY_DESCRIPTION = "description"
    private const val KEY_IMAGE = "image"
  }

  override fun onCreate(db: SQLiteDatabase?) {
    val createCitiesTable = "CREATE TABLE $TABLE_DESIRED_VACATIONS(" +
        "$KEY_ID INTEGER PRIMARY KEY," +
        "$KEY_NAME TEXT," +
        "$KEY_HOTEL_NAME TEXT," +
        "$KEY_LOCATION TEXT," +
        "$KEY_MONEY_NEEDED TEXT," +
        "$KEY_DESCRIPTION TEXT," +
        "$KEY_IMAGE BLOB" +
        ");"

    db?.execSQL(createCitiesTable)
  }

  override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    db!!.execSQL("DROP TABLE IF EXISTS $TABLE_DESIRED_VACATIONS")
    onCreate(db)
  }

  /**
   * Method to read the records from cities table in form of ArrayList
   */
  @SuppressLint("Range")
  fun viewVacation(): MutableList<Vacation> {
    val vacationsList: ArrayList<Vacation> = ArrayList<Vacation>()

    // Query to select all the records from the table.
    val selectQuery = "SELECT * FROM $TABLE_DESIRED_VACATIONS"
    val db = this.readableDatabase

    var cursor: Cursor? = null
    try {
      cursor = db.rawQuery(selectQuery, null)
    } catch (e: SQLiteException) {
      db.execSQL(selectQuery)
      return ArrayList()
    }

    var id: Int
    var name: String
    var hotelName: String
    var location: String
    var moneyNeeded: String
    var description: String
    var image: Bitmap

    if (cursor.moveToFirst()) {
      do {
        id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
        name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
        hotelName = cursor.getString(cursor.getColumnIndex(KEY_HOTEL_NAME))
        location = cursor.getString(cursor.getColumnIndex(KEY_LOCATION))
        moneyNeeded = cursor.getString(cursor.getColumnIndex(KEY_MONEY_NEEDED))
        description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION))

        val temp = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE))
        image = BitmapFactory.decodeByteArray(temp, 0, temp.size)

        val vacation = Vacation(
          id,
          name,
          hotelName,
          location,
          moneyNeeded,
          description,
          image
        )
        vacationsList.add(vacation)

      } while (cursor.moveToNext())
    }
    return vacationsList
  }

  /**
   * Function to insert city record
   */
  fun createVacation(vacation: Vacation, imageByteArr:ByteArray): Long {
    val db = this.writableDatabase

    val contentValues = ContentValues()
    contentValues.put(KEY_NAME, vacation.name)
    contentValues.put(KEY_HOTEL_NAME, vacation.hotelName)
    contentValues.put(KEY_LOCATION, vacation.location)
    contentValues.put(KEY_MONEY_NEEDED, vacation.moneyNeeded)
    contentValues.put(KEY_DESCRIPTION, vacation.description)
    contentValues.put(KEY_IMAGE, imageByteArr)

    val result = db.insert(TABLE_DESIRED_VACATIONS, null, contentValues)

    db.close()
    return result
  }

  /**
   * Function to update city record
   */
  fun updateVacations(
    id: Int,
    newVacationName: String,
    newVacationHotelName: String,
    newVacationLocation: String,
    newVacationMoneyNeeded: String,
    neVacationDescription: String,
  ): Int {
    val db = this.writableDatabase

    val contentValues = ContentValues()
    contentValues.put(KEY_NAME, newVacationName)
    contentValues.put(KEY_HOTEL_NAME, newVacationHotelName)
    contentValues.put(KEY_LOCATION, newVacationLocation)
    contentValues.put(KEY_MONEY_NEEDED, newVacationMoneyNeeded)
    contentValues.put(KEY_DESCRIPTION, neVacationDescription)

    // Updating Row
    val result = db.update(TABLE_DESIRED_VACATIONS, contentValues, "$KEY_ID=${id}", null)

    db.close()
    return result
  }

  /**
   * Function to delete city record
   */
  fun deleteVacation(VacationToDeleteId: Int): Int {
    val db = this.writableDatabase
    val contentValues = ContentValues()
    contentValues.put(KEY_ID, VacationToDeleteId)

    // Deleting Row
    val result = db.delete(TABLE_DESIRED_VACATIONS, "$KEY_ID=$VacationToDeleteId", null)

    db.close()
    return result
  }

  fun changeImage(vacationId: Int, image: ByteArray): Int {
    val db = this.writableDatabase

    val contentValues = ContentValues()
    contentValues.put(KEY_IMAGE, image)

    // Updating Row
    val result = db.update(TABLE_DESIRED_VACATIONS, contentValues, "$KEY_ID=${vacationId}", null)

    db.close()
    return result
  }
}