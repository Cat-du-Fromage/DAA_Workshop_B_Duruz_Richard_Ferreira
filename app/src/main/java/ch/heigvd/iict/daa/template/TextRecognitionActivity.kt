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
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextRecognitionActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var resultTextView: TextView
    private lateinit var selectButton: Button
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_text_recognition)

        imageView = findViewById(R.id.image_view)
        resultTextView = findViewById(R.id.result_text)
        selectButton = findViewById(R.id.button_select)

        selectButton.setOnClickListener {
            selectImageFromGallery()
        }

        findViewById<Button>(R.id.button_test).setOnClickListener {
            // Charger l'image depuis les ressources
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test_image6)
            imageView.setImageBitmap(bitmap)
            processImage(bitmap)
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

                // Lancer la reconnaissance
                processImage(bitmap)
            }
        }
    }

    private fun processImage(bitmap: Bitmap) {
        // Créer un InputImage depuis le Bitmap
        val image = InputImage.fromBitmap(bitmap, 0)

        // Lancer la reconnaissance
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // Succès : afficher le texte détecté
                displayResults(visionText)
            }
            .addOnFailureListener { e ->
                // Erreur : afficher un message
                Toast.makeText(this, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("TextRecognition", "Erreur de reconnaissance", e)
            }
    }

    private fun displayResults(text: Text) {
        if (text.text.isEmpty()) {
            resultTextView.text = "Aucun texte détecté"
            return
        }

        // Afficher simplement tout le texte détecté
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