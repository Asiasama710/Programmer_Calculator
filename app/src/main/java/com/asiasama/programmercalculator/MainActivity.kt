package com.asiasama.programmercalculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var octalEditText: EditText
    private lateinit var hexadecimalEditText: EditText
    private lateinit var binaryEditText: EditText
    private lateinit var decimalEditText: EditText
    private var textWatcher: TextWatcher? = null
    private lateinit var currentFocusedEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textWatcher = ViewTextWatcher()
        setupViews()
        setupFocusListener()


    }

    private fun setupViews() {
        octalEditText = findViewById(R.id.input_octal)
        hexadecimalEditText = findViewById(R.id.input_hex)
        decimalEditText = findViewById(R.id.input_decimal)
        binaryEditText = findViewById(R.id.input_binary)
    }

    private fun setupFocusListener() {
        octalEditText.onFocusChangeListener = FocusViewListener()
        hexadecimalEditText.onFocusChangeListener = FocusViewListener()
        decimalEditText.onFocusChangeListener = FocusViewListener()
        binaryEditText.onFocusChangeListener = FocusViewListener()
    }

    private fun setOutputForEditText(number: String) {
        when (currentFocusedEditText.id) {
            octalEditText.id -> {
                binaryEditText.setText(baseConverter(NumericalBase.OCTAL, NumericalBase.BINARY, number))
                hexadecimalEditText.setText(baseConverter(NumericalBase.OCTAL, NumericalBase.HEXADECIMAL, number))
                decimalEditText.setText(baseConverter(NumericalBase.OCTAL, NumericalBase.DECIMAL, number))
            }
            hexadecimalEditText.id -> {
                octalEditText.setText(baseConverter(NumericalBase.HEXADECIMAL, NumericalBase.OCTAL, number))
                binaryEditText.setText(baseConverter(NumericalBase.HEXADECIMAL, NumericalBase.BINARY, number))
                decimalEditText.setText(baseConverter(NumericalBase.HEXADECIMAL, NumericalBase.DECIMAL, number))
            }
            decimalEditText.id -> {
                octalEditText.setText(baseConverter(NumericalBase.DECIMAL, NumericalBase.OCTAL, number))
                hexadecimalEditText.setText( baseConverter(NumericalBase.DECIMAL, NumericalBase.HEXADECIMAL, number))
                binaryEditText.setText(baseConverter(NumericalBase.DECIMAL, NumericalBase.BINARY, number))

            }
            binaryEditText.id -> {
                octalEditText.setText(baseConverter(NumericalBase.BINARY, NumericalBase.OCTAL, number))
                hexadecimalEditText.setText(baseConverter(NumericalBase.BINARY, NumericalBase.HEXADECIMAL, number))
                decimalEditText.setText(baseConverter(NumericalBase.BINARY, NumericalBase.DECIMAL, number))

            }
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

        if (checkValidInput(numberString, sourceBase).not() ) return ""
        if (numberString.isBlank()) return ""

        val number = numberString.toBigIntegerOrNull(sourceBase.base)
        return if (number == null)  ""
        else number.toString(targetBase.base)

    }

    inner class ViewTextWatcher : TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            setOutputForEditText(text.toString())
        }
        override fun afterTextChanged(p0: Editable?) {
        }
    }

    inner class FocusViewListener : OnFocusChangeListener {
        override fun onFocusChange(view: View?, hasFocus: Boolean) {
            if (hasFocus) {
                currentFocusedEditText = view as EditText
                currentFocusedEditText.addTextChangedListener(textWatcher)
            } else {
                currentFocusedEditText.removeTextChangedListener(textWatcher)
            }
        }
    }


}
