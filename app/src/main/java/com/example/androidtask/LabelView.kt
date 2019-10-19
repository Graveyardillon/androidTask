package com.example.androidtask

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout

class LabelView(context: Context) : LinearLayout(context) {

    companion object {
        val MAX_DURATION: Int = 500
    }

    var startTime: Long = 0

    init {
        View.inflate(context, R.layout.my_label, this)

        var initX = 0
        var initY = 0

        setOnTouchListener{ v: View?, event: MotionEvent? ->

            var tappedX = 0
            var tappedY = 0

            when (event?.action) {
                MotionEvent.ACTION_UP -> {
                    startTime = System.currentTimeMillis()
                    println("up")
                }

                MotionEvent.ACTION_DOWN -> {
                    if(System.currentTimeMillis() - startTime <= MAX_DURATION) {
                        // ダブルタップ
                        println("double tap!!")
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

                    tappedX = event!!.getX().toInt()
                    tappedY = event!!.getY().toInt()
                }
            }
            true
        }
    }
}