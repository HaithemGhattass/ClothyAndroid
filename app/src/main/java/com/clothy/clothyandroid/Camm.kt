package com.clothy.clothyandroid

import android.content.Context
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import org.opencv.android.InstallCallbackInterface
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfRect
import org.opencv.core.Rect
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import java.io.*

class Camm : AppCompatActivity(),LoaderCallbackInterface {
    private var mCascadeFile: File? = null
    private var mJavaDetector: CascadeClassifier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camm)

        // Initialize OpenCV
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, this)
        } else {
            onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    override fun onManagerConnected(status: Int) {
        when (status) {
            LoaderCallbackInterface.SUCCESS -> {
                // Load the cascade classifier for face detection
                val `is` = resources.openRawResource(R.raw.lbpcascade_frontalface)
                val cascadeDir = getDir("cascade", Context.MODE_PRIVATE)
                mCascadeFile = File(cascadeDir, "lbpcascade_frontalface.xml")
                val os = FileOutputStream(mCascadeFile)
                val buffer = ByteArray(4096)
                var bytesRead: Int
                while (`is`.read(buffer).also { bytesRead = it } != -1) {
                    os.write(buffer, 0, bytesRead)
                }
                `is`.close()
                os.close()
                mJavaDetector = CascadeClassifier(mCascadeFile!!.absolutePath)
                val extras = intent.extras
                val imageBitmap = extras?.getParcelable<Bitmap>("image")
                // Load the image from resources
                val options = BitmapFactory.Options().apply {
                    inSampleSize = 4
                    inPreferredConfig = Bitmap.Config.RGB_565
                }
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.nn,options)
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                val compressedBitmap = BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size(),options)
                // Convert the bitmap to a Mat
                val image = Mat()
                Utils.bitmapToMat(compressedBitmap, image)
                // Convert the image to grayscale
                Imgproc.cvtColor(image, image, Imgproc.CHAIN_APPROX_NONE)

                // Detect faces in the image
                val faces = MatOfRect()
                val scaleFactor = 1.05
                mJavaDetector!!.detectMultiScale(image, faces)

                // Blur each detected face
                for (face in faces.toArray()) {
                    Log.e("faces", face.toString())
                    // Extract the region of the image containing the face
                    val faceMat = Mat(image, Rect(face.x, face.y, face.width, face.height))
                    val kernelSizeFraction = 0.1 // adjust this value as needed
                    val kernelSize = Size(face.width * kernelSizeFraction, face.height * kernelSizeFraction)
                    // Apply a Gaussian blur to the face
                    Imgproc.GaussianBlur(faceMat, faceMat, Size(109.0, 109.0), 90.0)

                    // Put the blurred face back into the image
                    faceMat.copyTo(image.submat(face))




                }

                // Convert the image back to a bitmap
                Utils.matToBitmap(image, compressedBitmap)
                os.close()
                image.release()
                faces.release()
                // Display the blurred image in an ImageView
                val imageView = findViewById<ImageView>(R.id.fd_activity_surface_view)
                imageView.setImageBitmap(compressedBitmap)
            }

        }
    }

    override fun onPackageInstall(operation: Int, callback: InstallCallbackInterface?) {
        TODO("Not yet implemented")
    }
    }
fun Context.assetsToBitmap(fileName: String): Bitmap?{
    return try {
        with(assets.open(fileName)){
            BitmapFactory.decodeStream(this)
        }
    } catch (e: IOException) { null }
}private fun blurBitmap(bitmap: Bitmap, x: Float, y: Float, radius: Float): Bitmap {
    val blurredBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(blurredBitmap)
    val paint = Paint()
    paint.isAntiAlias = true
    canvas.drawBitmap(bitmap, 0f, 0f, paint)
    val blurMaskFilter = BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL)
    paint.maskFilter = blurMaskFilter
    canvas.drawCircle(x, y, radius, paint)
    return blurredBitmap
}