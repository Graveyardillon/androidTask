package com.example.androidtask

import android.content.Context
import android.view.View
import android.widget.LinearLayout

class LabelView(context: Context) : LinearLayout(context) {

    var onClick: (()->Unit)? = null

    constructor(context: Context, onClick: () -> Unit): this(context) {
        this.onClick = onClick
    }

    init {
        View.inflate(context, R.layout.my_label, this)

        setOnTouchListener{ _, _ -> true }
    }
}