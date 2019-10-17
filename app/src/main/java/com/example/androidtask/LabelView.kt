package com.example.androidtask

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout

class LabelView(context: Context) : LinearLayout(context) {

    init {
        View.inflate(context, R.layout.my_label, this)

        var initX = 0
        var initY = 0

        setOnTouchListener{ v: View?, event: MotionEvent? ->

            var tappedX = 0
            var tappedY = 0

            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    tappedX = event!!.getX().toInt()
                    tappedY = event!!.getY().toInt()

                    initX = tappedX
                    initY = tappedY
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