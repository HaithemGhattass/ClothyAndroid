import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.clothy.clothyandroid.R
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
import java.io.BufferedReader
import java.io.InputStreamReader

class Cam : AppCompatActivity(), LoaderCallbackInterface {

    private val TAG = "cam"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.facedetector)

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
                val classifier = CascadeClassifier(
                    applicationContext.resources.getIdentifier("lbpcascade_frontalface.xml", "raw", packageName)
                        .let { inputStream ->
                            InputStreamReader(applicationContext.resources.openRawResource(inputStream)).use {
                                BufferedReader(it).use {
                                    it.readText()
                                }
                            }
                        }
                )

                // Load the image from resources
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.user_woman_2)

                // Convert the bitmap to a Mat
                val image = Mat()
                Utils.bitmapToMat(bitmap, image)

                // Convert the image to grayscale
                Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY)

                // Detect faces in the image
                val faces = MatOfRect()
                classifier.detectMultiScale(image, faces)

                // Blur each detected face
                for (face in faces.toArray()) {
                    // Extract the region of the image containing the face
                    val faceMat = Mat(image, Rect(face.x, face.y, face.width, face.height))

                    // Apply a Gaussian blur to the face
                    Imgproc.GaussianBlur(faceMat, faceMat, Size(0.0, 0.0), 10.0)

                    // Put the blurred face back into the image
                    faceMat.copyTo(image.submat(face))
                }

                // Convert the image back to a bitmap
                Utils.matToBitmap(image, bitmap)

                // Display the blurred image in an ImageView
                val imageView = findViewById<ImageView>(R.id.fd_activity_surface_view)
                imageView.setImageBitmap(bitmap)
            }

        }
    }

    override fun onPackageInstall(operation: Int, callback: InstallCallbackInterface?) {
        TODO("Not yet implemented")
    }


    // Not implemented
    }


