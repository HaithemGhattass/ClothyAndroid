package com.clothy.clothyandroid

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import java.io.IOException
/*
private lateinit var cameraBtn: ImageButton
private lateinit var myImage: ImageView
private val cameraRequestId  = 1222
@Composable
fun AddClothes() {
    val context = LocalContext.current
    AndroidView(
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.activity_chatt, null)

            myImage = view.findViewById<ImageView>(R.id.supermanImageView)
            cameraBtn  = view.findViewById<ImageButton>(R.id.greenCheckButton)
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
                    val txtOutput : TextView = view.findViewById(R.id.txtOutput)
                    val btn: Button = view.findViewById(R.id.cameraBtn)
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
                        }
                        .addOnFailureListener { e ->
                            // Task failed with an exception
                            // ...
                        }


                }

            }

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraActivityResultLauncher.launch(cameraIntent)


            view
        },
        update = { view ->
            // Update the view here if necessary

        }


    )

}

 */
