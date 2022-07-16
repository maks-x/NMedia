package ru.netology.nmedia.db

object PostsTable {

    const val NAME = "NMediaPostsTable"

    const val DRAFT_TABLE_NAME = "draftTable"
    const val DRAFT_PRIMKEY_COLUMN_NAME = "draftKey"
    const val DRAFT_TEXT_COLUMN_NAME = "draftText"
    const val DRAFT_PRIMARY_KEY = "draft"

    val DDL = """
        CREATE TABLE $NAME (
            ${Column.ID.columnName} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Column.AUTHOR.columnName} TEXT NOT NULL,
            ${Column.PUBLISHED.columnName} TEXT NOT NULL,
            ${Column.AVATAR_ID.columnName} INTEGER NOT NULL,
            ${Column.TEXT.columnName} TEXT NOT NULL,
            ${Column.VIDEO_LINK.columnName} TEXT,
            ${Column.LIKED_BY_ME.columnName} BOOLEAN NOT NULL DEFAULT 0,
            ${Column.LIKES_COUNT.columnName} INTEGER NOT NULL DEFAULT 0,
            ${Column.COMMENTS_COUNT.columnName} INTEGER NOT NULL DEFAULT 0,
            ${Column.SHARE_COUNT.columnName} INTEGER NOT NULL DEFAULT 0,
            ${Column.VIEWS_COUNT.columnName} INTEGER NOT NULL DEFAULT 0
        )
    """.trimIndent()

    val ddlForDraft = """
        CREATE TABLE $DRAFT_TABLE_NAME (
            $DRAFT_PRIMKEY_COLUMN_NAME TEXT NOT NULL DEFAULT $DRAFT_PRIMARY_KEY PRIMARY KEY,
            $DRAFT_TEXT_COLUMN_NAME TEXT DEFAULT NULL
        )        
    """.trimIndent()

    val defaultDraftDDL = """
        INSERT INTO $DRAFT_TABLE_NAME DEFAULT VALUES
    """.trimIndent()

    val ALL_COLUMNS_NAMES = Column.values().map {
        it.columnName
    }.toTypedArray()

    enum class Column(val columnName: String) {
        ID("id"),
        AUTHOR("author"),
        PUBLISHED("published"),
        AVATAR_ID("avatarID"),
        TEXT("text"),
        VIDEO_LINK("videoLink"),
        LIKED_BY_ME("likedByMe"),
        LIKES_COUNT("likesCount"),
        COMMENTS_COUNT("commentsCount"),
        SHARE_COUNT("shareCount"),
        VIEWS_COUNT("viewsCount")
    }
}