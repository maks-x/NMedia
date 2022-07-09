package ru.netology.nmedia.db

object PostsTable {

    const val NAME = "NMediaPostsTable"

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
        );
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