package com.eagleappbuffer.qrcodescanner.pref

interface IPreferenceManager {

    fun saveDate(date: String)
    fun getDate(): String
    fun saveAdCount(count: Int)
    fun getAdCount(): Int
    fun saveExpiryTime(time: Long)
    fun getExpiryTime(): Long

}