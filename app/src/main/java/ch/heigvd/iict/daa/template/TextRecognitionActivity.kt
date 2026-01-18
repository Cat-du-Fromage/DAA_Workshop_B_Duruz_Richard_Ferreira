package ch.heigvd.iict.daa.template

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

import java.lang.reflect.Field

class TextRecognitionActivity : AppCompatActivity() {

    private val images = listOf(
        Pair("Image 1", R.drawable.test_image),
        Pair("Image 2", R.drawable.test_image2),
        Pair("Image 3", R.drawable.test_image3),
        Pair("Image 4", R.drawable.test_image4),
        Pair("Image 5", R.drawable.test_image5),
        Pair("Image 6", R.drawable.test_image6),
    )

    private lateinit var spinner: Spinner

    private lateinit var imageView: ImageView
    private lateinit var resultTextView: TextView
    private lateinit var selectButton: Button
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_text_recognition)

        spinner = findViewById(R.id.spinner)
        imageView = findViewById(R.id.image_view)
        resultTextView = findViewById(R.id.result_text)
        selectButton = findViewById(R.id.button_select)

        selectButton.setOnClickListener {
            selectImageFromGallery()
        }

        selectLocalImage()
    }

    private fun selectLocalImage() {
        // Remplir le spinner avec les NOMS des images
        val names = images.map { it.first }  // Juste les noms
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, names)
        spinner.adapter = adapter
        findViewById<Button>(R.id.button_test).setOnClickListener {
            val position = spinner.selectedItemPosition
            val imageId = images[position].second  // L'ID de l'image

            // Charger l'image
            val bitmap = BitmapFactory.decodeResource(resources, imageId)
            imageView.setImageBitmap(bitmap)

            resultTextView.text = "Analyse en cours"

            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            val image = InputImage.fromBitmap(bitmap, 0)

            recognizer.process(image)
                .addOnSuccessListener { result ->
                    displayResults(result)
                }
                .addOnFailureListener { e ->
                    resultTextView.text = "Erreur: ${e.message}"
                }
        }
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                // Charger l'image sélectionnée
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                imageView.setImageBitmap(bitmap)
                processImage(bitmap)
            }
        }
    }

    private fun processImage(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                displayResults(visionText)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("TextRecognition", "Erreur de reconnaissance", e)
            }
    }

    private fun displayResults(text: Text) {
        if (text.text.isEmpty()) {
            resultTextView.text = "Aucun texte détecté"
            return
        }
        resultTextView.text = text.text
    }

    override fun onDestroy() {
        super.onDestroy()
        recognizer.close()
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
    }
}