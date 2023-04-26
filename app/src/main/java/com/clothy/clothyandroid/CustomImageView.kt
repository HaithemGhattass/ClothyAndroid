package com.clothy.clothyandroid

import android.content.Context
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.MotionEvent

class CustomImageView(context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    init {
        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val color = getPixelColorFromImage(event.x, event.y)
                // Do something with the color, e.g., change the background color of a view
                // myView.setBackgroundColor(color)
                performClick()
            }
            true
        }
    }

    private fun getPixelColorFromImage(x: Float, y: Float): Int {
        val bitmap = (drawable as BitmapDrawable).bitmap
        val inverse = Matrix()
        imageMatrix.invert(inverse)
        val touchPoint = floatArrayOf(x, y)
        inverse.mapPoints(touchPoint)
        val pixelX = touchPoint[0].toInt()
        val pixelY = touchPoint[1].toInt()

        return if (pixelX in 0 until bitmap.width && pixelY in 0 until bitmap.height) {
            bitmap.getPixel(pixelX, pixelY)
        } else {
            Color.TRANSPARENT
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
    interface OnColorTouchListener {
        fun onColorTouched(color: Int)
    }
}
