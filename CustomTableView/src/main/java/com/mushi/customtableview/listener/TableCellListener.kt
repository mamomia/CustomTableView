package com.mushi.customtableview.listener

import androidx.recyclerview.widget.RecyclerView

interface TableCellListener {
    fun onCellClicked(cellView: RecyclerView.ViewHolder, column: Int, row: Int)
}
