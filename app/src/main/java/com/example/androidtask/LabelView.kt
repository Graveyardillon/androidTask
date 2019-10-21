package com.example.androidtask

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.example.androidtask.storage.LabelDataBaseHelper
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.my_label.view.*

class LabelView(context: Context) : LinearLayout(context) {

    val labelDataBaseHelper = LabelDataBaseHelper(context)

    companion object {
        val MAX_DURATION: Int = 500
    }

    var startTime: Long = 0

    init {
        View.inflate(context, R.layout.my_label, this)

        var initX = 0
        var initY = 0

        var toggleDrag = false

        setOnTouchListener{ v: View?, event: MotionEvent? ->

            var tappedX = 0
            var tappedY = 0

            when (event?.action) {
                MotionEvent.ACTION_UP -> {
                    startTime = System.currentTimeMillis()
                    println("up")

                    if(toggleDrag) {
                        // このタイミングで更新
                        val x = event!!.getX().toInt()
                        val y = event!!.getY().toInt()

                        val text = title.text
                        println(text)
                    }

                    toggleDrag = false
                }

                MotionEvent.ACTION_DOWN -> {
                    if(System.currentTimeMillis() - startTime <= MAX_DURATION) {
                        // ダブルタップ
                        println("double tap!!")

                        val result = labelDataBaseHelper.deleteLabel(v!!)

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