package ru.netology.nmedia.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "draftTable")
class DraftEntity(

    @PrimaryKey
    @ColumnInfo(name = "draftKey")
    val draftKey: String,

    @ColumnInfo(name = "draftText")
    val draftText: String?
) {
    companion object {
        const val DRAFT_PRIMARY_KEY_QUOTES = "\"draft\""
    }
}
