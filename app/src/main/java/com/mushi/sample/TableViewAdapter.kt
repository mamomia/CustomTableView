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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mushi.customtableview.R
import com.mushi.customtableview.adapter.AbstractTableAdapter
import com.mushi.customtableview.adapter.recyclerview.holder.AbstractViewHolder
import com.mushi.customtableview.annotation.CellFieldType
import com.mushi.customtableview.holder.ActionCellViewHolder
import com.mushi.customtableview.holder.BoxCellViewHolder
import com.mushi.customtableview.holder.ColumnHeaderViewHolder
import com.mushi.customtableview.holder.DataCellViewHolder
import com.mushi.customtableview.holder.EditableCellViewHolder
import com.mushi.customtableview.holder.RowHeaderViewHolder
import com.mushi.customtableview.listener.CellTextChangeListener
import com.mushi.customtableview.model.Cell
import com.mushi.customtableview.model.ColumnHeader
import com.mushi.customtableview.model.RowHeader
import com.mushi.customtableview.sort.SortState
import com.mushi.customtableview.util.CustomCheckedWatcher
import com.mushi.customtableview.util.CustomTextWatcher

class TableViewAdapter : AbstractTableAdapter<ColumnHeader?, RowHeader?, Cell?>() {

    private var tableCellListener: CellTextChangeListener? = null

    fun setTableCellListener(listener: CellTextChangeListener?) {
        tableCellListener = listener
    }

    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layout = when (viewType) {
            ACTION_CELL_TYPE -> inflater.inflate(
                R.layout.table_view_action_cell_layout,
                parent,
                false
            )

            EDITABLE_CELL_TYPE -> inflater.inflate(
                R.layout.table_view_editable_cell_layout,
                parent,
                false
            )

            BOX_CELL_TYPE -> inflater.inflate(R.layout.table_view_box_cell_layout, parent, false)
            else -> inflater.inflate(R.layout.table_view_data_cell_layout, parent, false)
        }

        return when (viewType) {
            ACTION_CELL_TYPE -> ActionCellViewHolder(layout)
            EDITABLE_CELL_TYPE -> EditableCellViewHolder(layout)
            BOX_CELL_TYPE -> BoxCellViewHolder(layout)
            else -> DataCellViewHolder(layout)
        }
    }

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: Cell?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        when (holder.itemViewType) {
            ACTION_CELL_TYPE -> {
                (holder as ActionCellViewHolder).setCell(cellItemModel)
            }

            EDITABLE_CELL_TYPE -> {
                val viewHolder = holder as EditableCellViewHolder
                val cell = cellItemModel ?: return
                viewHolder.setCell(
                    cell,
                    object : CustomTextWatcher(columnPosition, rowPosition) {
                        override fun textChanged(oldValue: String?, newValue: String?) {
                            if (newValue != oldValue) {
                                // ✅ Persist to data model
                                mCellItems.getOrNull(rowPosition)
                                    ?.getOrNull(columnPosition)?.content =
                                    newValue

                                // ✅ Notify listener
                                tableCellListener?.let {
                                    val cursor = viewHolder.cellText.selectionStart.coerceAtLeast(0)
                                    it.onColumnUpdated(oldValue, newValue, column, row, cursor)
                                }
                            }
                        }
                    })
            }

            BOX_CELL_TYPE -> {
                val viewHolder = holder as BoxCellViewHolder
                val cell = cellItemModel ?: return
                viewHolder.setCell(
                    cell,
                    object : CustomCheckedWatcher(columnPosition, rowPosition) {
                        override fun checkedChanged(isChecked: Boolean) {
                            // ✅ Persist checkbox state
                            mCellItems.getOrNull(rowPosition)?.getOrNull(columnPosition)?.content =
                                isChecked.toString()
                            tableCellListener?.onColumnUpdated(isChecked, column, row)
                        }
                    })
            }

            else -> {
                (holder as DataCellViewHolder).setCell(cellItemModel)
            }
        }
    }

    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.table_view_column_header_layout, parent, false)
        return ColumnHeaderViewHolder(layout, tableView)
    }

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        columnHeaderItemModel: ColumnHeader?,
        columnPosition: Int
    ) {
        (holder as ColumnHeaderViewHolder).setColumnHeader(columnHeaderItemModel)
    }

    override fun onCreateRowHeaderViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.table_view_row_header_layout, parent, false)
        return RowHeaderViewHolder(layout)
    }

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder,
        rowHeaderItem: RowHeader?,
        rowPosition: Int
    ) {
        val rowHeaderViewHolder = holder as RowHeaderViewHolder
        rowHeaderViewHolder.row_header_textview.text = rowHeaderItem?.data.toString()
        val colorRes = rowHeaderItem?.backgroundColor ?: com.mushi.sample.R.color.darker_gray
        rowHeaderViewHolder.row_header_layout.setBackgroundColor(colorRes)
    }

    override fun onCreateCornerView(parent: ViewGroup): View {
        val corner = LayoutInflater.from(parent.context)
            .inflate(R.layout.table_view_corner_layout, parent, false)
        corner.setOnClickListener {
            val sortState = tableView?.rowHeaderSortingStatus
            if (sortState != SortState.ASCENDING) {
                tableView?.sortRowHeader(SortState.ASCENDING)
            } else {
                tableView?.sortRowHeader(SortState.DESCENDING)
            }
        }
        return corner
    }

    override fun getCellItemViewType(position: Int): Int {
        val item = getCellItem(position, 0)
        return when (item?.level) {
            CellFieldType.Action -> ACTION_CELL_TYPE
            CellFieldType.Editable -> EDITABLE_CELL_TYPE
            CellFieldType.CheckBox -> BOX_CELL_TYPE
            else -> 0
        }
    }

    fun updateSingleRow(updatedHeader: RowHeader?, updatedRow: List<Cell?>, rowPosition: Int) {
        if (rowPosition < mCellItems.size) {
            updateSingleRowItems(updatedHeader, updatedRow.toMutableList(), rowPosition)
        }
    }

    companion object {
        private const val ACTION_CELL_TYPE = 1
        private const val EDITABLE_CELL_TYPE = 2
        private const val BOX_CELL_TYPE = 3
    }
}