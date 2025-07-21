/*
 * MIT License
 *
 * Copyright (c) 2025 Mushi
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
package com.mushi.customtableview.listener.itemclick

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.mushi.customtableview.ITableView
import com.mushi.customtableview.adapter.recyclerview.CellRecyclerView
import com.mushi.customtableview.adapter.recyclerview.CellRowRecyclerViewAdapter
import com.mushi.customtableview.adapter.recyclerview.holder.AbstractViewHolder

/**
 * Created by Mushi on 26/09/2017.
 */
class CellRecyclerViewItemClickListener(recyclerView: CellRecyclerView, tableView: ITableView) :
    AbstractItemClickListener(recyclerView, tableView) {
    private val mCellRecyclerView = tableView.cellRecyclerView

    override fun clickAction(view: RecyclerView, e: MotionEvent): Boolean {
        // Get interacted view from x,y coordinate.
        val childView = view.findChildViewUnder(e.x, e.y)

        if (childView != null) {
            // Find the view holder
            val holder = mRecyclerView.getChildViewHolder(childView) as AbstractViewHolder

            // Get y position from adapter
            val adapter = mRecyclerView
                .adapter as CellRowRecyclerViewAdapter<*>?

            val column = holder.adapterPosition
            val row = adapter!!.yPosition

            // Control to ignore selection color
            if (!mTableView.isIgnoreSelectionColors) {
                mSelectionHandler.setSelectedCellPositions(holder, column, row)
            }

            // Call ITableView listener for item click
            tableViewListener.onCellClicked(holder, column, row)

            return true
        }
        return false
    }

    override fun longPressAction(e: MotionEvent) {
        // Consume the action for the time when either the cell row recyclerView or
        // the cell recyclerView is scrolling.
        if ((mRecyclerView.scrollState != RecyclerView.SCROLL_STATE_IDLE) ||
            (mCellRecyclerView.scrollState != RecyclerView.SCROLL_STATE_IDLE)
        ) {
            return
        }

        // Get interacted view from x,y coordinate.
        val child = mRecyclerView.findChildViewUnder(e.x, e.y)

        if (child != null) {
            // Find the view holder
            val holder = mRecyclerView.getChildViewHolder(child)

            // Get y position from adapter
            val adapter = mRecyclerView
                .adapter as CellRowRecyclerViewAdapter<*>?

            // Call ITableView listener for long click
            if (adapter != null) {
                tableViewListener.onCellLongPressed(
                    holder, holder.adapterPosition, adapter
                        .yPosition
                )
            }
        }
    }

    override fun doubleClickAction(e: MotionEvent): Boolean {
        // Get interacted view from x,y coordinate.
        val childView = mRecyclerView.findChildViewUnder(e.x, e.y)

        if (childView != null) {
            // Find the view holder
            val holder = mRecyclerView.getChildViewHolder(childView) as AbstractViewHolder

            // Get y position from adapter
            val adapter = mRecyclerView
                .adapter as CellRowRecyclerViewAdapter<*>?

            val column = holder.adapterPosition
            val row = adapter!!.yPosition

            // Control to ignore selection color
            if (!mTableView.isIgnoreSelectionColors) {
                mSelectionHandler.setSelectedCellPositions(holder, column, row)
            }

            // Call ITableView listener for item click
            tableViewListener.onCellDoubleClicked(holder, column, row)

            return true
        }
        return false
    }
}
