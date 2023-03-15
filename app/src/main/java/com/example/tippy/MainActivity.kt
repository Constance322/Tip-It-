package com.example.tippy

import android.animation.ArgbEvaluator
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG="MainActivity"
private const val INITIAL_TIP_PERCENT=15

class MainActivity : AppCompatActivity() {
    private lateinit var etBase:EditText
    private lateinit var seekBarTip:SeekBar
    private lateinit var tvPercentTip:TextView
    private lateinit var tvTipAmount:TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvDescription:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBase=findViewById(R.id.etBase)
        seekBarTip=findViewById(R.id.seekBarTip)
        tvPercentTip=findViewById(R.id.tvPercentTip)
        tvTipAmount=findViewById(R.id.tvTipAmount)
        tvTotalAmount=findViewById(R.id.tvTotalAmount)
        tvDescription=findViewById(R.id.tvDescription)


        seekBarTip.progress= INITIAL_TIP_PERCENT
        tvPercentTip.text="$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        seekBarTip.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $p1")
                tvPercentTip.text="$p1%"
                computeTipAndTotal()
                updateTipDescription(p1)


            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
        etBase.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "afterTextChanged $p0")
                computeTipAndTotal()

            }



        })


    }

    private fun updateTipDescription(p1: Int) {
        val tipDescription =when(p1){
            in 0..9 ->"Poor"
            in 10..14->"Acceptable"
            in 15..19 ->"Good"
            in 28..24 ->"Great"
            else->"Amazing"
        }
        tvDescription.text=tipDescription
        val color=ArgbEvaluator().evaluate(
            p1.toFloat()/seekBarTip.max,
            ContextCompat.getColor(this, R.color.worst_tip),
            ContextCompat.getColor(this, R.color.best_tip)



        ) as Int
        tvDescription.setTextColor(color)



    }

    private fun computeTipAndTotal() {
        if(etBase.text.isEmpty()){
            tvTipAmount.text=""
            tvTotalAmount.text=""
            return
        }
        val baseAmount=etBase.text.toString().toDouble()
        val tipPercent=seekBarTip.progress
        val tipAmount=baseAmount*tipPercent/100
        val totalAmount=baseAmount+tipAmount

        tvTipAmount.text="%.2f".format(tipAmount)
        tvTotalAmount.text="%.2f".format(totalAmount)


    }
}