package com.clothy.clothyandroid

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import com.clothy.clothyandroid.entities.Profile
import com.google.gson.GsonBuilder
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

object Utils {
    private const val TAG = "Utils"
    fun loadProfiles(context: Context): List<Profile>? {
        return try {
            val builder = GsonBuilder()
            val gson = builder.create()
            val array = JSONArray(loadJSONFromAsset(context, "profiles.json"))
            val profileList: MutableList<Profile> = ArrayList<Profile>()
            for (i in 0 until array.length()) {
                val profile: Profile = gson.fromJson(array.getString(i), Profile::class.java)
                profileList.add(profile)
            }
            profileList
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun loadJSONFromAsset(context: Context, jsonFileName: String): String? {
        val json: String
        val `is`: InputStream
        try {
            val manager = context.assets
            Log.d(TAG, "path $jsonFileName")
            `is` = manager.open(jsonFileName)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun getDisplaySize(windowManager: WindowManager): Point {
        return try {
            val display = windowManager.defaultDisplay
            val displayMetrics = DisplayMetrics()
            display.getMetrics(displayMetrics)
            Point(displayMetrics.widthPixels, displayMetrics.heightPixels)
        } catch (e: Exception) {
            e.printStackTrace()
            Point(0, 0)
        }
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}