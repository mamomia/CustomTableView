package com.mushi.customtableview.listener

interface CellTextChangeListener {
    fun onColumnUpdated(oldData: String?, newData: String?, column: Int, row: Int, cursor: Int)
    fun onColumnUpdated(isChecked: Boolean?, column: Int, row: Int)
}
