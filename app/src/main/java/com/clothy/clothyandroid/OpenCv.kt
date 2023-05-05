package com.clothy.clothyandroid

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader

class Cammm : AppCompatActivity(),LoaderCallbackInterface {
    private lateinit var cameraBtn: ImageButton
    private lateinit var myImage: CustomImageView
    private lateinit var  typee: EditText
    private lateinit var  Color: EditText
    private lateinit var  taille: EditText
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

    @SuppressLint("ClickableViewAccessibility")
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


                // Convert the bitmap to a Mat
                val image = Mat()
                Utils.bitmapToMat(imageBitmap, image)

                // Convert the image to grayscale
                Imgproc.cvtColor(image, image, Imgproc.CHAIN_APPROX_NONE)

                // Detect faces in the image
                val faces = MatOfRect()
                mJavaDetector!!.detectMultiScale(image, faces)

                // Blur each detected face
                for (face in faces.toArray()) {
                    Log.e("faces", face.toString())
                    // Extract the region of the image containing the face
                    val faceMat = Mat(image, Rect(face.x, face.y, face.width, face.height))

                    // Apply a Gaussian blur to the face
                    Imgproc.GaussianBlur(faceMat, faceMat, Size(101.0, 101.0), 50.0)

                    // Put the blurred face back into the image
                    faceMat.copyTo(image.submat(face))
                    faceMat.release()
                    image.submat(face).release()



                }

                // Convert the image back to a bitmap
                Utils.matToBitmap(image, imageBitmap)
                os.close()
                // Display the blurred image in an ImageView
                myImage = findViewById(R.id.myImage)
                myImage.setImageBitmap(imageBitmap)
                val localModel = LocalModel.Builder()
                    .setAssetFilePath("model (3).tflite")
                    .build()
                val txtOutput : TextView = findViewById(R.id.txtOutput)

                val options = CustomImageLabelerOptions.Builder(localModel)
                    .setConfidenceThreshold(0.2f)
                    .setMaxResultCount(1)
                    .build()
                val labeler = ImageLabeling.getClient(options)
                val imagee = InputImage.fromBitmap(imageBitmap,0)
                var outputText = ""
                var typ = ""
                labeler.process(imagee)
                    .addOnSuccessListener { labels ->
                        // Task completed successfully
                        for (label in labels) {
                            val text = label.text
                            val confidence = label.confidence
                            Log.e("confidence",confidence.toString())
                            outputText += "$text : $confidence\n"
                            typ += text
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
                        Color = findViewById(R.id.editTextColor)
                        Color.setText(hexColor)
                        typee =findViewById(R.id.editTextType)
                        taille = findViewById(R.id.editTexttaile)
                        typee.setText(typ)
                        val tailles = arrayOf("S", "L", "XL", "XXL", "XXXL")
                        val pointure = arrayOf("38", "39", "40", "41", "42","43","44","45")
                        if (typ.equals("shoes")){
                            taille.setOnTouchListener { v, event ->
                                if (event.action == MotionEvent.ACTION_UP) {
                                    val builder = AlertDialog.Builder(this)
                                    builder.setTitle("Select a shoe size\n")
                                    builder.setItems(pointure) { dialog, which ->
                                        taille.setText(pointure[which])
                                    }
                                    builder.show()
                                }
                                true
                            }
                            taille.inputType = InputType.TYPE_NULL


                        }else{
                            taille.setOnTouchListener { v, event ->
                                if (event.action == MotionEvent.ACTION_UP) {
                                    val builder = AlertDialog.Builder(this)
                                    builder.setTitle("Select Size")
                                    builder.setItems(tailles) { dialog, which ->
                                        taille.setText(tailles[which])
                                    }
                                    builder.show()
                                }
                                true
                            }
                            taille.inputType = InputType.TYPE_NULL
                        }
                        val cc: ImageButton = findViewById(R.id.greenCheckButton)

                        cc.setOnClickListener {
                            val bitmap = (myImage.drawable as BitmapDrawable).bitmap
                            val file = File(cacheDir, "image.jpg")
                            val outputStream = FileOutputStream(file)
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                            outputStream.flush()
                            outputStream.close()
                            val mediaType = "image/jpeg".toMediaTypeOrNull()
                            val requestFile = RequestBody.create(mediaType, file)
                            val imagePart = MultipartBody.Part.createFormData(
                                "photo",
                                file.name,
                                requestFile
                            )
                            val typee =
                                RequestBody.create("text/plain".toMediaTypeOrNull(), typ)
                            val taille = RequestBody.create(
                                "text/plain".toMediaTypeOrNull(),
                                taille.text.toString()
                            )
                            val couleur = RequestBody.create(
                                "text/plain".toMediaTypeOrNull(),
                                hexColor
                            )
                            val category = RequestBody.create(
                                "text/plain".toMediaTypeOrNull(),
                                "My Couleur"
                            )
                            val requestBody = MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addPart(imagePart)
                                .addPart(typee)
                                .addPart(couleur)
                                .addPart(taille)
                                .addPart(category)
                                .build()
                            val retro =
                                RetrofitClient().getInstance().create(OutfitService::class.java)

                            val call =
                                retro.uploadImage(imagePart, typee, taille, couleur, category)
                            call.enqueue(object : Callback<ResponseBody> {
                                override fun onResponse(
                                    call: Call<ResponseBody>,
                                    response: Response<ResponseBody>
                                ) {
                                    // Handle the response
                                }

                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    // Handle the error
                                }
                            })


                        }


                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        // ...
                    }
                val btn: ImageButton = findViewById(R.id.redXButton)
                btn.setOnClickListener{
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivity(cameraIntent)
                }
                val add : ImageButton= findViewById(R.id.greenCheckButton)
                add.setOnClickListener {

                }
            }

        }
    }


    override fun onPackageInstall(operation: Int, callback: InstallCallbackInterface?) {
        TODO("Not yet implemented")
    }
}
