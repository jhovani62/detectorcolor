package com.jacexample.colorrecognitionapp

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.app.ActivityCompat
import android.Manifest
import android.content.pm.PackageManager
import android.view.View
import androidx.activity.enableEdgeToEdge
import java.util.Locale

import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {


    private lateinit var btnRecognizeColor: Button
    private lateinit var txtRecognizedColor: TextView
    private lateinit var colorView: View
    private lateinit var btnExit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val mainLayout = findViewById<ConstraintLayout>(R.id.main)
        mainLayout.setBackgroundResource(R.drawable.fondo23) // Reemplaza con tu imagen

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnRecognizeColor = findViewById(R.id.btnRecognizeColor)
        txtRecognizedColor = findViewById(R.id.txtRecognizedColor)
        colorView = findViewById(R.id.colorView)
        btnExit = findViewById(R.id.btnExit)

        btnRecognizeColor.setOnClickListener {
            checkPermissionsAndStartRecognition()
        }

        btnExit.setOnClickListener {
            finish()
        }
    }

    private fun checkPermissionsAndStartRecognition() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            // Solicita el permiso si no se ha concedido
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 200)
        } else {

            startVoiceRecognition()
        }
    }

    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, 100)
        } else {
            Toast.makeText(this, "Speech Recognition not supported", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val recognizedText = results?.get(0)?.toLowerCase(Locale.ROOT)

            if (recognizedText != null) {
                identifyColor(recognizedText)
            }
        }
    }
    private fun identifyColor(colorName: String) {
        val color = when (colorName) {
            // Colores básicos
            "rojo" -> android.R.color.holo_red_dark
            "azul" -> android.R.color.holo_blue_dark
            "verde" -> android.R.color.holo_green_dark
            "amarillo" -> android.R.color.holo_orange_light
            "negro" -> android.R.color.black
            "blanco" -> android.R.color.white
            "naranja" -> android.R.color.holo_orange_dark
            "morado" -> android.R.color.holo_purple
            "rosado" -> android.R.color.holo_red_light
            "gris" -> android.R.color.darker_gray
            "gris claro" -> android.R.color.primary_text_light
            "gris oscuro" -> android.R.color.background_dark
            "violeta" -> android.R.color.holo_purple
            "celeste" -> android.R.color.holo_blue_bright
            "café" -> android.R.color.holo_red_light
            "beige" -> android.R.color.holo_orange_light
            "verde claro" -> android.R.color.holo_green_light
            "verde oscuro" -> android.R.color.holo_green_dark
            "azul claro" -> android.R.color.holo_blue_light
            "azul oscuro" -> android.R.color.holo_blue_bright
            "rojo claro" -> android.R.color.holo_red_light
            "rojo oscuro" -> android.R.color.holo_red_dark
            "rosa" -> android.R.color.holo_red_light

            else -> null
        }

        if (color == null) {
            colorView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
            txtRecognizedColor.text = "Color no reconocible: $colorName"
            Toast.makeText(this, "Color no reconocible o no existe: $colorName", Toast.LENGTH_SHORT).show()
        } else {
            colorView.setBackgroundColor(ContextCompat.getColor(this, color))
            txtRecognizedColor.text = "Color reconocido: $colorName"
        }
    }
}
