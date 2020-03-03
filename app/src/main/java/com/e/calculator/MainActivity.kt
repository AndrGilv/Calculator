package com.e.calculator

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.e.calculator.Calculator.calc
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.NumberFormatException


class MainActivity : AppCompatActivity() {

    lateinit var expressionTV: TextView
    var lastNumeric: Boolean = false
    var error: Boolean = false
    var lastPoint: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        expressionTV = findViewById(R.id.expressionTextView)
        expressionTV.setShowSoftInputOnFocus(false);
    }

    private fun insertInTextViewInCursor(text: String, offset: Int = 1){
        var index: Int = expressionTV.getSelectionStart()
        if(index == 0) index = expressionTV.length()
        val newText = expressionTextView.text.insert(index, text)
        expressionTV.text = newText
        expressionTextView.setSelection(index+offset)
    }

    fun onDigitBtnClicked(view: View) {
        if (error) {
            expressionTV.text = (view as Button).text
            error = false
        } else {
            insertInTextViewInCursor((view as Button).text.toString())
        }
        lastNumeric = true
    }

    fun onDecimalPointBtnClicked(view: View) {
        if (lastNumeric && !error && !lastPoint) {
            insertInTextViewInCursor(".")
            lastNumeric = false
            lastPoint = true
        }
    }

    fun onOperBtnClicked(view: View) {
        if (lastNumeric && !error) {
            insertInTextViewInCursor((view as Button).text.toString())
            lastNumeric = false
            lastPoint = false    // Reset the DOT flag
        }
    }

    fun onMinusBtnClicked(view: View){
        if ((!lastPoint || lastNumeric) && !error) {
            insertInTextViewInCursor("-")
            lastNumeric = false
            lastPoint = false
        }
    }

    fun onClearBtnClicked(view: View) {
        expressionTV.text = ""
        lastNumeric = false
        error = false
        lastPoint = false
    }

    fun onBackspaceBtnClicked(view: View){
        val editable: Editable = expressionTextView.getText()
        val selectStart = expressionTextView.selectionStart
        val selectEnd = expressionTextView.selectionEnd
        if(!expressionTV.text.isEmpty()){
            if(selectStart != selectEnd){
                editable.delete(selectStart, selectEnd)
                expressionTextView.setSelection(selectStart)
            }
            else if(selectStart == 0){
                editable.delete(0, 1)
                expressionTextView.setSelection(0)
            }
            else{
                editable.delete(selectStart - 1, selectStart)
                expressionTextView.setSelection(selectStart - 1)
            }
        }
    }

    fun onOpenBracketBtnClicked(view : View){
        if(!error && !lastPoint){
            insertInTextViewInCursor("(")
        }
        else if(error){
            expressionTV.text = "("
            lastNumeric = false
            error = false
            lastPoint = false
        }
    }

    fun onCloseBracketBtnClicked(view: View){
        if(!error && lastNumeric){
            insertInTextViewInCursor(")")
        }
    }

    fun onFuncBtnClicked(view: View){
        if(!lastNumeric && !lastPoint){
            var btnName: String = (view as Button).text.toString()
            btnName =  btnName + "("
            if(error){
                expressionTV.text = btnName
            }
            else if(true){
                insertInTextViewInCursor(btnName, btnName.length)
            }
        }

    }

    fun onEqualBtnClicked(view: View) {
        var expression: List<String>? = null
        if (!expressionTV.text.isEmpty() && !error) {
            try {
                expression = Parser.parse(expressionTV.text.toString())
            }
            catch(invalidExpressionEx: InvalidExpressionException){
                Log.e("CalcLog", invalidExpressionEx.message, invalidExpressionEx)
                Toast.makeText(this, "НЕКОРЕКТНО ВЫРАЖЕНИЕ",Toast.LENGTH_SHORT).show()
                expressionTV.text = "Error"
                error = true
            }
            catch (inconsistentBracketsException: InconsistentBracketsException){
                Log.e("CalcLog", inconsistentBracketsException.message, inconsistentBracketsException)
                Toast.makeText(this, "НЕСОГЛАСОВАННЫЕ СКОБКИ",Toast.LENGTH_SHORT).show()
                expressionTV.text = "Error"
                error = true
            }
            catch(invalidTernarOperArgumentsException: InvalidTernarOperArgumentsException){
                Log.e("CalcLog", invalidTernarOperArgumentsException.message, invalidTernarOperArgumentsException)
                Toast.makeText(this, "НЕКОРЕКТНАЯ ТЕРНАРНАЯ ОПЕРАЦИЯ",Toast.LENGTH_SHORT).show()
                expressionTV.text = "Error"
                error = true
            }

            if(expression != null){
                var result: Double? = null
                try{
                    result = calc(expression)

                }
                catch(e: NoSuchElementException){
                    Log.e("CalcLog", e.message, e)
                    Toast.makeText(this, "НЕВОЗМОЖНО ВЫЧИСЛИТЬ",Toast.LENGTH_SHORT).show()
                    expressionTV.text = "Error"
                    error = true
                }
                catch(e: NumberFormatException){
                    Log.e("CalcLog", e.message, e)
                    Toast.makeText(this, "ВВЕДЕНО НЕ РАСПОЗНАВАЕМОЕ ВЫРАЖЕНИЕ",Toast.LENGTH_SHORT).show()
                    expressionTV.text = "Error"
                    error = true
                }

                if(result != null){
                    var intResult: Int
                    if(Math.floor(result) == result){
                        intResult = result.toInt()
                        expressionTV.text = (intResult.toString())
                    }
                    else{
                        expressionTV.text = (result.toString())
                    }
                    var eprStr: String = ""
                    for (x in expression)
                        eprStr += "$x "
                    eprStr += "=$result"
                    Log.i("CalcLog", eprStr)
                    expressionTextView.setSelection(expressionTV.length())
                }
            }
        }
        else{
            Toast.makeText(this, "ВЫРАЖЕНИЕ НЕ МОЖЕТ БЫТЬ ПУСТОЙ СТРОКОЙ",Toast.LENGTH_SHORT).show()
        }
    }
}


