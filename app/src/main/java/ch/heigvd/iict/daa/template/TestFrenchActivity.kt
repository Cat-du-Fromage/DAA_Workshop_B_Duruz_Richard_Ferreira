package ch.heigvd.iict.daa.template

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TestFrenchActivity : AppCompatActivity() {

    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Assurez-vous d'avoir le layout activity_test.xml
        setContentView(R.layout.activity_test)

        resultTextView = findViewById(R.id.resultTextView)

        // Créer une image avec du texte français
        val frenchText = """
            Bonjour le monde!
            Ceci est un test de reconnaissance
            de texte en français avec ML Kit.
            Le résultat devrait être correct.
        """.trimIndent()

        val bitmap = createTextImage(frenchText)
        testFrenchRecognition(bitmap)
    }

    private fun createTextImage(text: String): Bitmap {
        val bitmap = Bitmap.createBitmap(800, 400, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 32f
            isAntiAlias = true
        }

        // Dessiner le texte
        var y = 50f
        text.split("\n").forEach { line ->
            canvas.drawText(line, 50f, y, paint)
            y += 40f
        }

        return bitmap
    }

    private fun testFrenchRecognition(bitmap: Bitmap) {
        val recognizer: TextRecognizer = TextRecognition.getClient(
            TextRecognizerOptions.DEFAULT_OPTIONS
        )

        val image = InputImage.fromBitmap(bitmap, 0)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val detectedText = visionText.text
                resultTextView.text = "Texte détecté :\n$detectedText"

                // Vérifier si le français est bien reconnu
                if (detectedText.contains("Bonjour") ||
                    detectedText.contains("français")) {
                    resultTextView.append("\n\n✅ Reconnaissance française fonctionnelle!")
                }
            }
            .addOnFailureListener { e ->
                resultTextView.text = "Erreur : ${e.message}"
            }
    }
}