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
package com.mushi.customtableview.holder

import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.mushi.customtableview.ITableView
import com.mushi.customtableview.R
import com.mushi.customtableview.adapter.recyclerview.holder.AbstractSorterViewHolder
import com.mushi.customtableview.model.ColumnHeader
import com.mushi.customtableview.sort.SortState

/**
 * Created by Mushi on 23/10/2017.
 */
class ColumnHeaderViewHolder(itemView: View, private val tableView: ITableView?) :
    AbstractSorterViewHolder(itemView) {
    private val column_header_container: LinearLayout =
        itemView.findViewById(R.id.column_header_container)
    private val column_header_textview: TextView =
        itemView.findViewById(R.id.column_header_textView)
    private val column_header_sortButton: ImageButton =
        itemView.findViewById(R.id.column_header_sortButton)
    private val mSortButtonClickListener: View.OnClickListener = View.OnClickListener {
        if (sortState == SortState.ASCENDING) {
            tableView!!.sortColumn(adapterPosition, SortState.DESCENDING)
        } else if (sortState == SortState.DESCENDING) {
            tableView!!.sortColumn(adapterPosition, SortState.ASCENDING)
        } else {
            // Default one
            tableView!!.sortColumn(adapterPosition, SortState.DESCENDING)
        }
    }

    init {
        // Set click listener to the sort button
        column_header_sortButton.setOnClickListener(mSortButtonClickListener)
    }

    /**
     * This method is calling from onBindColumnHeaderHolder on TableViewAdapter
     */
    fun setColumnHeader(columnHeader: ColumnHeader?) {
        column_header_textview.text = columnHeader!!.data.toString()
        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can remove them.
        // It is necessary to remeasure itself.
        column_header_container.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        column_header_textview.requestLayout()
    }

    override fun onSortingStatusChanged(sortState: SortState) {
        Log.e(
            LOG_TAG, (" + onSortingStatusChanged : x:  " + adapterPosition + " old state "
                    + sortState + " current state : " + sortState + " visiblity: " +
                    column_header_sortButton.visibility)
        )
        super.onSortingStatusChanged(sortState)
        // It is necessary to remeasure itself.
        column_header_container.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        controlSortState(sortState)
        Log.e(
            LOG_TAG, (" - onSortingStatusChanged : x:  " + adapterPosition + " old state "
                    + sortState + " current state : " + sortState + " visiblity: " +
                    column_header_sortButton.visibility)
        )
        column_header_textview.requestLayout()
        column_header_sortButton.requestLayout()
        column_header_container.requestLayout()
        itemView.requestLayout()
    }

    private fun controlSortState(sortState: SortState) {
        if (sortState == SortState.ASCENDING) {
            column_header_sortButton.visibility = View.VISIBLE
            column_header_sortButton.setImageResource(R.drawable.ic_down)
        } else if (sortState == SortState.DESCENDING) {
            column_header_sortButton.visibility = View.VISIBLE
            column_header_sortButton.setImageResource(R.drawable.ic_up)
        } else {
            column_header_sortButton.visibility = View.INVISIBLE
        }
    }

    companion object {
        private val LOG_TAG: String = ColumnHeaderViewHolder::class.java.simpleName
    }
}
