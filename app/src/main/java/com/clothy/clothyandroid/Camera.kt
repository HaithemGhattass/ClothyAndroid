package com.clothy.clothyandroid

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import java.io.IOException

class Camera : AppCompatActivity() {

    private lateinit var cameraBtn: ImageButton
    private lateinit var myImage: CustomImageView
    private val cameraRequestId  = 1222

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        myImage = findViewById<CustomImageView>(R.id.myImage)
        cameraBtn  = findViewById<ImageButton>(R.id.greenCheckButton)
        /**get Permission*/
        if (ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.CAMERA
            )== PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                cameraRequestId
            )
        /**set camera Open*/

            val cameraActivityResultLauncher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    val images: Bitmap = data?.extras?.get("data") as Bitmap
                    myImage.setImageBitmap(images)
                    val localModel = LocalModel.Builder()
                        .setAssetFilePath("model (3).tflite")
                        .build()
                    val img: ImageView = findViewById(R.id.myImage)
                    // assets folder image file name with extension
                    val fileName = "v.jpg"
                    // get bitmap from assets folder
                    val bitmap: Bitmap? = assetsToBitmap(fileName)
                    bitmap?.apply {
                        img.setImageBitmap(this)
                    }
                    val txtOutput : TextView = findViewById(R.id.txtOutput)

                    val options = CustomImageLabelerOptions.Builder(localModel)
                        .setConfidenceThreshold(0.2f)
                        .setMaxResultCount(1)
                        .build()
                    val labeler = ImageLabeling.getClient(options)
                    val image = InputImage.fromBitmap(images,0)
                    var outputText = ""
                    labeler.process(image)
                        .addOnSuccessListener { labels ->
                            // Task completed successfully
                            for (label in labels) {
                                val text = label.text
                                val confidence = label.confidence
                                Log.e("confidence",confidence.toString())
                                outputText += "$text : $confidence\n"
                                //val index = label.index
                            }

                            txtOutput.text = outputText
                            val drawable = myImage.drawable

// Get the color of the drawable
                            val color = when (drawable) {
                                is ColorDrawable -> drawable.color
                                is BitmapDrawable -> getDominantColor(drawable.bitmap)
                                else -> throw IllegalArgumentException("Unsupported drawable type")
                            }
                            txtOutput.setTextColor(color)
                            println(color)
                            val hexColor = java.lang.String.format("#%06X", 0xFFFFFF and color)
                        }
                        .addOnFailureListener { e ->
                            // Task failed with an exception
                            // ...
                        }

                }

            }
        val btn: ImageButton = findViewById(R.id.redXButton)
        btn.setOnClickListener{
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraActivityResultLauncher.launch(cameraIntent)
        }
        val add : ImageButton= findViewById(R.id.greenCheckButton)
        add.setOnClickListener {

        }

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraActivityResultLauncher.launch(cameraIntent)


    }



}
fun Context.assetsToBitmap(fileName: String): Bitmap?{
    return try {
        with(assets.open(fileName)){
            BitmapFactory.decodeStream(this)
        }
    } catch (e: IOException) { null }
}
fun getDominantColor(bitmap: Bitmap): Int {
    val newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true)
    val color = newBitmap.getPixel(0, 0)
    newBitmap.recycle()
    return color
}