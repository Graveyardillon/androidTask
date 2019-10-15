package com.example.androidtask

import android.content.Context
import android.view.View
import android.widget.LinearLayout

class LabelView(context: Context) : LinearLayout(context) {

    init {
        View.inflate(context, R.layout.my_label, this)

        setOnTouchListener{ _, _ -> true }
    }
}