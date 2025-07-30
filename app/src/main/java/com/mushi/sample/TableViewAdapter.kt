/*
 * MIT License
 *
 * Copyright (c) 2021 Evren Mushi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.mushi.sample

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mushi.customtableview.R
import com.mushi.customtableview.adapter.AbstractTableAdapter
import com.mushi.customtableview.adapter.recyclerview.holder.AbstractViewHolder
import com.mushi.customtableview.annotation.CellFieldType
import com.mushi.customtableview.holder.ActionCellViewHolder
import com.mushi.customtableview.holder.ColumnHeaderViewHolder
import com.mushi.customtableview.holder.DataCellViewHolder
import com.mushi.customtableview.holder.EditableCellViewHolder
import com.mushi.customtableview.holder.RowHeaderViewHolder
import com.mushi.customtableview.listener.CellTextChangeListener
import com.mushi.customtableview.model.Cell
import com.mushi.customtableview.model.ColumnHeader
import com.mushi.customtableview.model.RowHeader
import com.mushi.customtableview.sort.SortState
import com.mushi.customtableview.util.CustomTextWatcher

/**
 * Created by Mushi on 11/06/2017.
 *
 *
 */
class TableViewAdapter :
    AbstractTableAdapter<ColumnHeader?, RowHeader?, Cell?>() {
    private var cellItems: MutableList<MutableList<Cell?>>? = null
    private var tableCellListener: CellTextChangeListener? = null

    fun setTableCellListener(listener: CellTextChangeListener) {
        tableCellListener = listener
    }

    override fun setAllItems(
        columnHeaderItems: MutableList<ColumnHeader?>?,
        rowHeaderItems: MutableList<RowHeader?>?,
        cellItems: MutableList<MutableList<Cell?>>?
    ) {
        super.setAllItems(columnHeaderItems, rowHeaderItems, cellItems)
        this.cellItems = cellItems
    }

    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layout: View
        if (viewType == ACTION_CELL_TYPE) {
            layout = inflater.inflate(R.layout.table_view_action_cell_layout, parent, false)
            return ActionCellViewHolder(layout)
        } else if (viewType == EDITABLE_CELL_TYPE) {
            layout = inflater.inflate(R.layout.table_view_editable_cell_layout, parent, false)
            return EditableCellViewHolder(layout)
        }
        layout = inflater.inflate(R.layout.table_view_data_cell_layout, parent, false)
        return DataCellViewHolder(layout)
    }

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: Cell?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        when (holder.itemViewType) {
            ACTION_CELL_TYPE -> {
                val viewHolder = holder as ActionCellViewHolder
                viewHolder.setCell(cellItemModel as Cell)
            }

            EDITABLE_CELL_TYPE -> {
                val viewHolder = holder as EditableCellViewHolder
                viewHolder.setCell(
                    cellItemModel,
                    object : CustomTextWatcher(columnPosition, rowPosition) {
                        override fun textChanged(oldValue: String?, newValue: String?) {
                            //Log.e("", "oldValue: " + oldValue + ", newValue: " + newValue);
                            if (tableCellListener != null) {
                                var cursor: Int = viewHolder.cellText.selectionStart
                                cursor = if (cursor > 0) cursor else 1
                                tableCellListener!!.onColumnUpdated(
                                    oldValue,
                                    newValue,
                                    column,
                                    row,
                                    cursor
                                )
                            }
                        }
                    })
            }

            else -> {
                val viewHolder = holder as DataCellViewHolder
                viewHolder.setCell(cellItemModel as Cell)
            }
        }
    }

    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder {
        // Get Column Header xml Layout
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.table_view_column_header_layout, parent, false)
        return ColumnHeaderViewHolder(layout, tableView)
    }

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        columnHeaderItemModel: ColumnHeader?,
        columnPosition: Int
    ) {
        // Get the holder to update cell item text
        val columnHeaderViewHolder = holder as ColumnHeaderViewHolder
        columnHeaderViewHolder.setColumnHeader(columnHeaderItemModel)
    }

    override fun onCreateRowHeaderViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.table_view_row_header_layout, parent, false)
        return RowHeaderViewHolder(layout)
    }

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder,
        rowHeaderItemModel: RowHeader?,
        rowPosition: Int
    ) {
        // Get the holder to update row header item text
        val rowHeaderViewHolder = holder as RowHeaderViewHolder
        rowHeaderViewHolder.row_header_textview.text = rowHeaderItemModel!!.data.toString()
    }

    override fun onCreateCornerView(parent: ViewGroup): View {
        // Get Corner xml layout
        val corner = LayoutInflater.from(parent.context)
            .inflate(R.layout.table_view_corner_layout, parent, false)
        corner.setOnClickListener { view: View? ->
            val sortState =
                tableView!!.rowHeaderSortingStatus
            if (sortState != SortState.ASCENDING) {
                tableView!!.sortRowHeader(SortState.ASCENDING)
            } else {
                tableView!!.sortRowHeader(SortState.DESCENDING)
            }
        }
        return corner
    }

    override fun getCellItemViewType(position: Int): Int {
        val item = getCellItem(position, 0)
        if (item != null) {
            if (item.level == CellFieldType.Action) return ACTION_CELL_TYPE
            if (item.level == CellFieldType.Editable) return EDITABLE_CELL_TYPE
        }
        return 0
    }

    fun updateSingleRow(updatedRow: List<Cell>, rowPosition: Int) {
        if (cellItems != null && rowPosition < cellItems!!.size) {
            cellItems!![rowPosition] = updatedRow.toMutableList()
            setCellItems(cellItems as List<MutableList<Cell?>>?)
        }
    }

    companion object {
        private const val ACTION_CELL_TYPE = 1
        private const val EDITABLE_CELL_TYPE = 2
    }
}
