package com.eagleappbuffer.qrcodescanner.pref

import android.content.Context
import androidx.core.content.edit
import com.eagleappbuffer.qrcodescanner.BuildConfig

class PreferenceManager(context: Context) : IPreferenceManager {

    private val pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    override fun saveDate(date: String) {
        pref.edit {
            putString(DATEE, date)
        }
    }

    override fun getDate() = pref.getString(DATEE, "").toString()

    override fun saveAdCount(count: Int) {
        pref.edit {
            putInt(AD_COUNT, count)
        }
    }

    override fun getAdCount() = pref.getInt(AD_COUNT, 0)

    override fun saveExpiryTime(time: Long) {
        pref.edit {
            putLong(EXPIRY_TIME, time)
        }
    }

    override fun getExpiryTime() = pref.getLong(EXPIRY_TIME, -1)

    fun clearAds() {
        pref.edit {
            putLong(EXPIRY_TIME, -1)
            putInt(AD_COUNT, 0)
        }
    }

    companion object {
        const val PREFERENCE_NAME = BuildConfig.APPLICATION_ID;
        const val DATEE = "datee"
        const val AD_COUNT = "Ad Count"
        const val EXPIRY_TIME = "Expiry Time"
    }
}