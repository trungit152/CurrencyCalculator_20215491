package com.example.currencyswapcalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.util.Log
import java.text.NumberFormat
import java.util.Locale
import android.widget.AdapterView

class MainActivity : AppCompatActivity(), View.OnClickListener  {

    lateinit var topText: TextView;
    lateinit var bottomText: TextView;
    lateinit var currentText: TextView;
    lateinit var topCurrencySpinner: Spinner
    lateinit var bottomCurrencySpinner: Spinner

    val exchangeRates = doubleArrayOf(25390.0, 7.13, 1.0, 153.60, 0.77, 1384.22)

    var isNewNumber: Boolean = false;
    var topValue: Double = 0.0;
    var bottomValue: Double = 0.0;
    var currentValue: Double = 0.0;
    var currentInt: Long = 0;
    var currentDouble: Double = 0.0;
    var isDot: Boolean = false;
    var dotTime: Int = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        topText = findViewById(R.id.top_text)
        bottomText = findViewById(R.id.bottom_text)
        topCurrencySpinner = findViewById(R.id.topSpinner)
        bottomCurrencySpinner = findViewById(R.id.botSpinner)

        currentText = topText

        val currencyAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.currency_array,
            android.R.layout.simple_spinner_item
        )
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        topCurrencySpinner.adapter = currencyAdapter
        bottomCurrencySpinner.adapter = currencyAdapter

        // Bắt sự kiện khi thay đổi giá trị trong Spinner
        topCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                UpdateOtherText()  // Cập nhật lại giá trị khi chọn thay đổi
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        bottomCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                UpdateOtherText()  // Cập nhật lại giá trị khi chọn thay đổi
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        updateCurrentText(topText)
        topText.setOnClickListener {
            UpdateOtherText()
            updateCurrentText(topText)
        }

        bottomText.setOnClickListener {
            UpdateOtherText()
            updateCurrentText(bottomText)
        }

        findViewById<Button>(R.id.btn0).setOnClickListener(this)
        findViewById<Button>(R.id.btn1).setOnClickListener(this)
        findViewById<Button>(R.id.btn2).setOnClickListener(this)
        findViewById<Button>(R.id.btn3).setOnClickListener(this)
        findViewById<Button>(R.id.btn4).setOnClickListener(this)
        findViewById<Button>(R.id.btn5).setOnClickListener(this)
        findViewById<Button>(R.id.btn6).setOnClickListener(this)
        findViewById<Button>(R.id.btn7).setOnClickListener(this)
        findViewById<Button>(R.id.btn8).setOnClickListener(this)
        findViewById<Button>(R.id.btn9).setOnClickListener(this)
        findViewById<Button>(R.id.btnCE).setOnClickListener(this)
        findViewById<Button>(R.id.btnC).setOnClickListener(this)
        findViewById<Button>(R.id.btnDot).setOnClickListener(this)
    }


    override fun onClick(p0: View?) {
        val id = p0?.id;
        if (id == R.id.btn0) {
            addDigit(0)
        }
        else if (id == R.id.btn1) {
            addDigit(1)
        }
        else if (id == R.id.btn2) {
            addDigit(2)
        }
        else if (id == R.id.btn3) {
            addDigit(3)
        }
        else if (id == R.id.btn4) {
            addDigit(4)
        }
        else if (id == R.id.btn5) {
            addDigit(5)
        }
        else if (id == R.id.btn6) {
            addDigit(6)
        }
        else if (id == R.id.btn7) {
            addDigit(7)
        }
        else if (id == R.id.btn8) {
            addDigit(8)
        }
        else if (id == R.id.btn9) {
            addDigit(9)
        }
        else if (id == R.id.btnDot) {
            isDot = true;
        }
        else if (id == R.id.btnCE) {
            resetValue();
            currentText.text = "0";
            topText.text = "0";
            bottomText.text = "0";
        }
    }

    private fun updateCurrentText(selectedText: TextView) {
        if(currentText != selectedText){
            isNewNumber = true;
        }
        topText.setTypeface(null, android.graphics.Typeface.NORMAL);
        bottomText.setTypeface(null, android.graphics.Typeface.NORMAL);

        // Set the selected TextView as current and make it bold
        currentText = selectedText;
        currentText.setTypeface(null, android.graphics.Typeface.BOLD);
    }

    private fun addDigit(a: Int){
        if(isNewNumber){
            resetValue();
            isNewNumber = false;
        }
        if(!isDot){
            currentInt = currentInt*10 + a;
        }
        else{
            if(dotTime <= 10){
                dotTime*=10;
                currentDouble += a.toDouble()/(dotTime).toDouble() ;
            }
        }
        currentValue = if (dotTime > 1) {
            currentInt.toDouble() + currentDouble;
        } else {
            currentInt.toDouble()
        }
        Log.d("ok", topValue.toString())

        currentText.text = formatCurrency(currentValue)


        if(currentText == topText){
            topValue = currentValue;
        }
        else if (currentText == bottomText){
            bottomValue = currentValue;
        }
        UpdateOtherText();
    }
    private fun resetValue(){
        isDot = false;
        topValue = 0.0;
        bottomValue = 0.0;
        currentValue = 0.0;
        currentInt = 0;
        currentDouble = 0.0;
        dotTime = 1;
    }
    private fun UpdateOtherText(){
        if(currentText == topText){
            bottomValue = topValue/(exchangeRates[topCurrencySpinner.selectedItemId.toInt()]
            / exchangeRates[bottomCurrencySpinner.selectedItemId.toInt()]);
            bottomText.text =  formatCurrency(bottomValue);
        }
        else if (currentText == bottomText){
            topValue = bottomValue/(exchangeRates[bottomCurrencySpinner.selectedItemId.toInt()]
                    / exchangeRates[topCurrencySpinner.selectedItemId.toInt()]);
            topText.text = formatCurrency(topValue);
        }
    }

    private fun formatCurrency(value: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US) ;
        formatter.maximumFractionDigits = 2;
        return formatter.format(value);
    }
}