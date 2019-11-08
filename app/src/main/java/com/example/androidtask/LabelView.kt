package com.example.androidtask

import android.content.Context
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.core.content.ContextCompat.getSystemService
import com.example.androidtask.storage.LabelDataBaseHelper
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.my_label.view.*

class LabelView(context: Context) : LinearLayout(context) {

    val labelDataBaseHelper = LabelDataBaseHelper(context)

    companion object {
        val MAX_DURATION: Int = 200
    }

    var startTime: Long = 0

    init {
        View.inflate(context, R.layout.my_label, this)

        var initX = 0
        var initY = 0

        var toggleDrag = false

        title.setOnKeyListener { v, keyCode, event ->
            val editText = v as EditText

            if(event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editText.windowToken, 0)

                labelDataBaseHelper.updateLabel(this.x.toInt(), this.y.toInt(), title.text.toString(), this.tag.toString())
            }

            true
        }

        setOnTouchListener{ v: View?, event: MotionEvent? ->

            var tappedX = 0
            var tappedY = 0

            when (event?.action) {
                MotionEvent.ACTION_UP -> {
                    startTime = System.currentTimeMillis()
                    println("up")

                    if(toggleDrag) {
                        // ドラッグ後，このタイミングで更新
                        this.x += event!!.getX() - tappedX.toFloat() - initX.toFloat()
                        this.y += event!!.getY() - tappedY.toFloat() - initY.toFloat()

                        println("U: "+this.x.toString() + "/" + this.y.toString())

                        val text = title.text.toString()
                        val id = v!!.tag.toString()

                        labelDataBaseHelper.updateLabel(this.x.toInt(), this.y.toInt(), text, id)
                    }

                    toggleDrag = false
                }

                MotionEvent.ACTION_DOWN -> {
                    if(System.currentTimeMillis() - startTime <= MAX_DURATION) {
                        // ダブルタップ

                        val mainActivity = MainActivity()

                        val result = labelDataBaseHelper.deleteLabel(v!!)

                        this.visibility = View.INVISIBLE

                        if(result) {
                            Toast.makeText(context, "Deleted.", LENGTH_LONG).show()
                        }

                    } else {
                        println(System.currentTimeMillis() - startTime)
                        tappedX = event!!.getX().toInt()
                        tappedY = event!!.getY().toInt()

                        initX = tappedX
                        initY = tappedY
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    this.x += event!!.getX() - tappedX.toFloat() - initX.toFloat()
                    this.y += event!!.getY() - tappedY.toFloat() - initY.toFloat()

                    toggleDrag = true
                }
            }
            true
        }
    }
}