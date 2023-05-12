package com.clothy.clothyandroid

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.clothy.clothyandroid.services.OutfitService
import com.clothy.clothyandroid.services.RetrofitClient
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.core.Mat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import org.opencv.android.Utils
import org.opencv.core.MatOfRect
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier


class Camera : AppCompatActivity() {

    private lateinit var cameraBtn: ImageButton
    private lateinit var myImage: CustomImageView
    private lateinit var  typee: EditText
    private lateinit var  Color: EditText
    private lateinit var  taille: EditText
    private val cameraRequestId  = 1222
    var m: Mat? = null

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //myImage = findViewById(R.id.myImage)
        /**get Permission*/
        if (ContextCompat.checkSelfPermission(
                applicationContext,Manifest.permission.CAMERA
            )== PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                cameraRequestId
            )
        }
        /**set camera Open*/

        val cameraInt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraInt, cameraRequestId)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequestId && resultCode == RESULT_OK) {
            /**save to Image In layout*/
            val image: Bitmap = data?.extras?.get("data") as Bitmap
            //myImage.setImageBitmap(image)
            val rotationAngle = -90f // Rotate left by 90 degrees
            val matrix = Matrix()
            matrix.postRotate(rotationAngle)
            val rotatedBitmap = Bitmap.createBitmap(
                image,
                0, 0, // Start coordinates
                image.width, image.height, // Width and height
                matrix, // Rotation matrix
                true // Filter
            )
            // Pass the image bitmap to the next activity
            val intent = Intent(this, Camm::class.java)
            intent.putExtra("image", rotatedBitmap)
            startActivity(intent)
        }else{
            val intent =Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }


}


fun blurFaces(bitmap: Bitmap): Bitmap {
    val rawResourceId = R.raw.lbpcascade_frontalface
    val inputStream = MyApplication.getInstance().resources.openRawResource(rawResourceId)

// create a file in the cache directory to copy the raw resource to
    val cacheDir = MyApplication.getInstance().cacheDir
    val classifierFile = File(cacheDir, "lbpcascade_frontalface.xml")
// copy the raw resource to the cache directory
    inputStream.use { input ->
        FileOutputStream(classifierFile).use { output ->
            input.copyTo(output)
        }
    }
    val grayMat = Mat()
    val rgbaMat = Mat()
    Utils.bitmapToMat(bitmap, rgbaMat)
    Imgproc.cvtColor(rgbaMat, grayMat, Imgproc.COLOR_RGBA2GRAY)
    val classifier = CascadeClassifier(classifierFile.absolutePath)
    val faces = MatOfRect()
    classifier.detectMultiScale(grayMat, faces)
    for (face in faces.toArray()) {
        Imgproc.blur(rgbaMat.submat(face), rgbaMat.submat(face), Size(90.0, 90.0))
    }
    Utils.matToBitmap(rgbaMat, bitmap)
    return bitmap
}
fun getDominantColor(bitmap: Bitmap): Int {
    val newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true)
    val color = newBitmap.getPixel(0, 0)
    newBitmap.recycle()
    return color
}


