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
}