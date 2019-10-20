package com.example.androidtask

import android.content.Context
import android.database.DatabaseUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.Toast.LENGTH_LONG
import androidx.core.view.marginLeft
import com.example.androidtask.models.TitleModel
import com.example.androidtask.models.LabelModel
import com.example.androidtask.storage.TitleDataBaseHelper
import com.example.androidtask.storage.LabelDataBaseHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        var max_id = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val titleDataBaseHelper = TitleDataBaseHelper(this)
        val labelDataBaseHelper = LabelDataBaseHelper(this)

        if(titleDataBaseHelper.getRecordNum() != 0) {
            val initialText = titleDataBaseHelper.loadRecord()
            title_text.setText(initialText, TextView.BufferType.NORMAL)
        }

        val labelDetails = labelDataBaseHelper.loadLabels()

        labelDetails.get("x")?.let {
            for(i in 0..(it.count()-1)) {

                var labelView = LabelView(this@MainActivity)
                labelView.tag = labelDetails.get("id")!![i].toInt()
                var params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(
                    labelDetails.get("x")!![i].toDouble().toInt(),
                    labelDetails.get("y")!![i].toDouble().toInt(),
                    0,
                    0
                )

                max_id = labelDetails.get("id")!![i].toInt()

                relative_field.addView(labelView, params)
            }
        }



        // 全体にクリックリスナー設置
        relative_field.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                println(v.toString())

                when (event?.action) {
                    MotionEvent.ACTION_UP -> {
                        val x = event!!.getX()
                        val y = event!!.getY()

                        println("x: "+x+"y: "+y)

                        val label = LabelModel(
                            x.toString(),
                            y.toString(),
                            ""
                        )

                        val result = labelDataBaseHelper.insertLabel(label)
                        println("stored")

                        var labelView = LabelView(this@MainActivity)
                        max_id = max_id + 1
                        labelView.tag = max_id
                        var params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                        params.setMargins(x.toInt(), y.toInt(), 0, 0)

                        relative_field.addView(labelView, params)
                    }
                }
                return true
            }
        })

        // titleバーにリスナー設置
        title_text.setOnKeyListener { v, keyCode, event ->
            val editText = v as EditText

            // enterがクリックされたときの処理
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER ) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editText.windowToken, 0)

                val text = editText.text

                val title = TitleModel(
                    "0",
                    text.toString()
                )

                val result: Boolean

                if(titleDataBaseHelper.getRecordNum() == 0) {
                    result = titleDataBaseHelper.insertTitle(title)
                } else {
                    result = titleDataBaseHelper.updateTitle(title)
                }

                if(result) {
                    Toast.makeText(this, "Title saved", LENGTH_LONG).show()
                }
            }

            true
        }

    }
}
