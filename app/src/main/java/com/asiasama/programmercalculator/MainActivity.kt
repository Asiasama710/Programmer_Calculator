package com.asiasama.programmercalculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var octalResult: EditText
    private lateinit var hexadecimalResult: EditText
    private lateinit var binaryResult: EditText
    private lateinit var decimalResult: EditText
    private var textWatcher: TextWatcher? = null
    private lateinit var currentFocusedView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textWatcher = ViewTextWatcher()
        setupViews()
        setupFocusListener()


    }

    private fun setupViews() {
        octalResult = findViewById(R.id.input_octal)
        hexadecimalResult = findViewById(R.id.input_hex)
        decimalResult = findViewById(R.id.input_decimal)
        binaryResult = findViewById(R.id.input_binary)
    }

    private fun setupFocusListener() {
        octalResult.onFocusChangeListener = FocusViewListener()
        hexadecimalResult.onFocusChangeListener = FocusViewListener()
        decimalResult.onFocusChangeListener = FocusViewListener()
        binaryResult.onFocusChangeListener = FocusViewListener()
    }


    inner class ViewTextWatcher : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            when (currentFocusedView.id) {
                octalResult.id -> {
                    if (checkValidInput(currentFocusedView.text.toString(), NumericalBase.OCTAL).not() )
                        Toast.makeText(this@MainActivity, " enter Invalid Octal number to convert it", Toast.LENGTH_SHORT).show()
                     convertOctalNumber()
                }
                hexadecimalResult.id -> {
                    if (checkValidInput(currentFocusedView.text.toString(), NumericalBase.HEXADECIMAL).not())
                        Toast.makeText(this@MainActivity, " enter Invalid Hexadecimal to convert it", Toast.LENGTH_SHORT).show()
                    convertHexadecimalNumber()
                }
                decimalResult.id -> {
                    if (checkValidInput(currentFocusedView.text.toString(), NumericalBase.DECIMAL).not())
                        Toast.makeText(this@MainActivity, " enter Invalid decimal number to convert it", Toast.LENGTH_SHORT).show()
                    convertDecimalNumber()
                }
                else -> {
                    if (checkValidInput(currentFocusedView.text.toString(), NumericalBase.BINARY).not())
                        Toast.makeText(this@MainActivity, " enter Invalid Binary number ( 0, 1) to convert it", Toast.LENGTH_SHORT).show()
                    convertBinaryNumber()

                }
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }

    }

    private fun convertOctalNumber() {
        binaryResult.setText(toOctal(currentFocusedView.text.toString(), NumericalBase.BINARY))
        hexadecimalResult.setText(toOctal(currentFocusedView.text.toString(), NumericalBase.HEXADECIMAL))
        decimalResult.setText(toOctal(currentFocusedView.text.toString(), NumericalBase.DECIMAL))
    }

    private fun convertHexadecimalNumber() {
        octalResult.setText(toHexadecimal(currentFocusedView.text.toString(), NumericalBase.OCTAL))
        binaryResult.setText(toHexadecimal(currentFocusedView.text.toString(), NumericalBase.BINARY))
        decimalResult.setText(toHexadecimal(currentFocusedView.text.toString(), NumericalBase.DECIMAL))
    }

    private fun convertDecimalNumber() {
        octalResult.setText(toDecimal(currentFocusedView.text.toString(), NumericalBase.OCTAL))
        hexadecimalResult.setText(toDecimal(currentFocusedView.text.toString(), NumericalBase.HEXADECIMAL))
        binaryResult.setText(toDecimal(currentFocusedView.text.toString(), NumericalBase.BINARY))

    }

    private fun convertBinaryNumber() {
        octalResult.setText(toBinary(currentFocusedView.text.toString(), NumericalBase.OCTAL))
        hexadecimalResult.setText(toBinary(currentFocusedView.text.toString(), NumericalBase.HEXADECIMAL))
        decimalResult.setText(toBinary(currentFocusedView.text.toString(), NumericalBase.DECIMAL))
    }


    inner class FocusViewListener : View.OnFocusChangeListener {
        override fun onFocusChange(view: View?, hasFocus: Boolean) {
            if (hasFocus) {
                currentFocusedView = view as EditText
                currentFocusedView.addTextChangedListener(textWatcher)
            } else {
                currentFocusedView.removeTextChangedListener(textWatcher)
            }
        }
    }

    private fun toOctal(number: String, base: NumericalBase): String {
        return when (base) {
            NumericalBase.BINARY -> baseConverter(NumericalBase.OCTAL, NumericalBase.BINARY, number)
            NumericalBase.DECIMAL -> baseConverter(NumericalBase.OCTAL, NumericalBase.DECIMAL, number)
            NumericalBase.HEXADECIMAL -> baseConverter(NumericalBase.OCTAL, NumericalBase.HEXADECIMAL, number)
            else -> ""
        }
    }

    private fun toHexadecimal(number: String, base: NumericalBase): String {
        return when (base) {
            NumericalBase.OCTAL -> baseConverter(NumericalBase.HEXADECIMAL, NumericalBase.OCTAL, number)
            NumericalBase.DECIMAL -> baseConverter(NumericalBase.HEXADECIMAL, NumericalBase.DECIMAL, number)
            NumericalBase.BINARY -> baseConverter(NumericalBase.HEXADECIMAL, NumericalBase.BINARY, number)
            else -> ""
        }
    }

    private fun toDecimal(number: String, base: NumericalBase): String {
        return when (base) {
            NumericalBase.OCTAL -> baseConverter(NumericalBase.DECIMAL, NumericalBase.OCTAL, number)
            NumericalBase.HEXADECIMAL -> baseConverter(NumericalBase.DECIMAL, NumericalBase.HEXADECIMAL, number)
            NumericalBase.BINARY -> baseConverter(NumericalBase.DECIMAL, NumericalBase.BINARY, number)
            else -> ""
        }
    }

    private fun toBinary(number: String, base: NumericalBase): String {
        return when (base) {
            NumericalBase.OCTAL -> baseConverter(NumericalBase.BINARY, NumericalBase.OCTAL, number)
            NumericalBase.DECIMAL -> baseConverter(NumericalBase.BINARY, NumericalBase.DECIMAL, number)
            NumericalBase.HEXADECIMAL -> baseConverter(NumericalBase.BINARY, NumericalBase.HEXADECIMAL, number)
            else -> ""
        }
    }

    private fun checkValidInput(number: String, base: NumericalBase): Boolean {
        return when (base) {
            NumericalBase.OCTAL -> number.matches(Regex("[0-7]+"))
            NumericalBase.DECIMAL -> number.matches(Regex("\\d+"))
            NumericalBase.HEXADECIMAL -> number.matches(Regex("[\\dA-Fa-f]+"))
            NumericalBase.BINARY -> number.matches(Regex("[0-1]+"))
        }
    }

    private fun baseConverter(sourceBase: NumericalBase, targetBase: NumericalBase, numberString: String): String {
        if (numberString.isBlank()) return ""

        val number = numberString.toBigIntegerOrNull(sourceBase.base)
        return if (number == null)  ""
               else number.toString(targetBase.base)

    }


}
