package com.jettaskboard.multiplatform.domain.model

data class Board(
    val title: String,
    val imageUrl: String,
    val isStarred: Boolean = false
)

data class BoardListModel(
    val listOfBoards: List<Board> = listOf()
)
