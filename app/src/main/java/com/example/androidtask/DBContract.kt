package com.example.androidtask.storage

import android.provider.BaseColumns

// データベーススキーマなどの設定
object DBContract {
    class TitleEntry: BaseColumns {
        companion object {
            const val TABLE_NAME = "title"
            const val TITLE_TEXT = "title_text"
            const val TITLE_ID = "title_id"
        }
    }
    class  LabelEntry: BaseColumns {
        companion object {
            const val TABLE_NAME = "label"
            const val POSITION_X = "position_x"
            const val POSITION_Y = "position_y"
            const val CONTENT_TEXT = "text"
            const val TYPE = "type"
        }
    }
}