package com.mushi.customtableview.listener

interface CellTextChangeListener {
    fun onColumnUpdated(newData: String?, column: Int, row: Int, cursor: Int)
}
