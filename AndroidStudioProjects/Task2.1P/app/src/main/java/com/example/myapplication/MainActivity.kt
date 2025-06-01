package com.example.myapplication // Replace with your actual package name

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    // UI Elements
    private lateinit var spinnerConversionType: Spinner
    private lateinit var spinnerSourceUnit: Spinner
    private lateinit var spinnerDestinationUnit: Spinner
    private lateinit var editTextValue: EditText
    private lateinit var buttonConvert: Button
    private lateinit var textViewResult: TextView

    // Data for Spinners
    private val conversionTypes = arrayOf("Length", "Weight", "Temperature")
    private val lengthUnits = arrayOf("Inch", "Foot", "Yard", "Mile", "Centimeter", "Meter", "Kilometer")
    private val weightUnits = arrayOf("Pound", "Ounce", "Ton (US)", "Kilogram", "Gram")
    private val temperatureUnits = arrayOf("Celsius", "Fahrenheit", "Kelvin")

    // Decimal format for output
    private val decimalFormat = DecimalFormat("#.#####")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        spinnerConversionType = findViewById(R.id.spinnerConversionType)
        spinnerSourceUnit = findViewById(R.id.spinnerSourceUnit)
        spinnerDestinationUnit = findViewById(R.id.spinnerDestinationUnit)
        editTextValue = findViewById(R.id.editTextValue)
        buttonConvert = findViewById(R.id.buttonConvert)
        textViewResult = findViewById(R.id.textViewResult)


        val conversionTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, conversionTypes)
        conversionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerConversionType.adapter = conversionTypeAdapter


        spinnerConversionType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                updateUnitSpinners(conversionTypes[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


        updateUnitSpinners(conversionTypes[0])


        buttonConvert.setOnClickListener {
            performConversion()
        }
    }


    private fun updateUnitSpinners(type: String) {
        val units = when (type) {
            "Length" -> lengthUnits
            "Weight" -> weightUnits
            "Temperature" -> temperatureUnits
            else -> arrayOf() // Should not happen
        }
        val unitAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSourceUnit.adapter = unitAdapter
        spinnerDestinationUnit.adapter = unitAdapter
    }


    private fun performConversion() {
        val inputValueString = editTextValue.text.toString()
        // Basic validation (more advanced validation is for SIT708)
        if (inputValueString.isEmpty()) {
            textViewResult.text = "Result: Please enter a value."
            return
        }

        val inputValue = inputValueString.toDoubleOrNull()
        if (inputValue == null) {
            textViewResult.text = "Result: Invalid input value."
            return
        }

        val conversionType = spinnerConversionType.selectedItem.toString()
        val sourceUnit = spinnerSourceUnit.selectedItem.toString()
        val destinationUnit = spinnerDestinationUnit.selectedItem.toString()


        if (sourceUnit == destinationUnit) {
            textViewResult.text = "Result: ${decimalFormat.format(inputValue)} $destinationUnit"
            return
        }

        val result = when (conversionType) {
            "Length" -> convertLength(inputValue, sourceUnit, destinationUnit)
            "Weight" -> convertWeight(inputValue, sourceUnit, destinationUnit)
            "Temperature" -> convertTemperature(inputValue, sourceUnit, destinationUnit)
            else -> null // Should not happen
        }

        if (result != null) {
            textViewResult.text = "Result: ${decimalFormat.format(result)} $destinationUnit"
        } else {

            textViewResult.text = "Result: Conversion not supported or invalid units selected."
        }
    }

    // --- Conversion Logic Functions ---

    /**
     * Converts length units.
     * Base unit for internal conversion: Centimeters (cm)
     * 1 inch = 2.54 cm
     * 1 foot = 30.48 cm
     * 1 yard = 91.44 cm
     * 1 mile = 1.60934 km
     */
    private fun convertLength(value: Double, fromUnit: String, toUnit: String): Double? {

        val valueInCm = when (fromUnit) {
            "Inch" -> value * 2.54
            "Foot" -> value * 30.48
            "Yard" -> value * 91.44
            "Mile" -> value * 1.60934 * 100000
            "Centimeter" -> value
            "Meter" -> value * 100
            "Kilometer" -> value * 100000
            else -> return null
        }


        return when (toUnit) {
            "Inch" -> valueInCm / 2.54
            "Foot" -> valueInCm / 30.48
            "Yard" -> valueInCm / 91.44
            "Mile" -> valueInCm / (1.60934 * 100000)
            "Centimeter" -> valueInCm
            "Meter" -> valueInCm / 100
            "Kilometer" -> valueInCm / 100000
            else -> null
        }
    }

    /**
     * Converts weight units.
     * Base unit for internal conversion: Grams (g)
     * 1 pound = 0.453592 kg
     * 1 ounce = 28.3495 g
     * 1 ton  = 907.185 kg
     */
    private fun convertWeight(value: Double, fromUnit: String, toUnit: String): Double? {

        val valueInGrams = when (fromUnit) {
            "Pound" -> value * 0.453592 * 1000 // pound to kg, then kg to g
            "Ounce" -> value * 28.3495
            "Ton (US)" -> value * 907.185 * 1000 // ton to kg, then kg to g
            "Kilogram" -> value * 1000
            "Gram" -> value
            else -> return null
        }


        return when (toUnit) {
            "Pound" -> valueInGrams / (0.453592 * 1000)
            "Ounce" -> valueInGrams / 28.3495
            "Ton (US)" -> valueInGrams / (907.185 * 1000)
            "Kilogram" -> valueInGrams / 1000
            "Gram" -> valueInGrams
            else -> null
        }
    }

    /**
     * Converts temperature units.
     * Celsius to Fahrenheit: F = (C * 1.8) + 32
     * Fahrenheit to Celsius: C = (F - 32) / 1.8
     * Celsius to Kelvin: K = C + 273.15
     * Kelvin to Celsius: C = K - 273.15
     */
    private fun convertTemperature(value: Double, fromUnit: String, toUnit: String): Double? {

        return when (fromUnit) {
            "Celsius" -> when (toUnit) {
                "Fahrenheit" -> (value * 1.8) + 32
                "Kelvin" -> value + 273.15
                else -> null
            }
            "Fahrenheit" -> when (toUnit) {
                "Celsius" -> (value - 32) / 1.8
                "Kelvin" -> ((value - 32) / 1.8) + 273.15
                else -> null
            }
            "Kelvin" -> when (toUnit) {
                "Celsius" -> value - 273.15
                "Fahrenheit" -> ((value - 273.15) * 1.8) + 32
                else -> null
            }
            else -> null
        }
    }
}
