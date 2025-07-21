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
import com.mushi.customtableview.adapter.recyclerview.holder.AbstractViewHolder

/**
 * Created by Mushi on 26/09/2017.
 */
class RowHeaderRecyclerViewItemClickListener(
    recyclerView: CellRecyclerView,
    tableView: ITableView
) :
    AbstractItemClickListener(recyclerView, tableView) {
    override fun clickAction(view: RecyclerView, e: MotionEvent): Boolean {
        // Get interacted view from x,y coordinate.
        val childView = view.findChildViewUnder(e.x, e.y)

        if (childView != null) {
            // Find the view holder
            val holder = mRecyclerView.getChildViewHolder(childView) as AbstractViewHolder

            val row = holder.adapterPosition

            // Control to ignore selection color
            if (!mTableView.isIgnoreSelectionColors) {
                mSelectionHandler.setSelectedRowPosition(holder, row)
            }

            // Call ITableView listener for item click
            tableViewListener.onRowHeaderClicked(holder, row)
            return true
        }
        return false
    }

    override fun longPressAction(e: MotionEvent) {
        // Consume the action for the time when the recyclerView is scrolling.
        if (mRecyclerView.scrollState != RecyclerView.SCROLL_STATE_IDLE) {
            return
        }

        // Get interacted view from x,y coordinate.
        val child = mRecyclerView.findChildViewUnder(e.x, e.y)

        if (child != null) {
            // Find the view holder
            val holder = mRecyclerView.getChildViewHolder(child)

            // Call ITableView listener for long click
            tableViewListener.onRowHeaderLongPressed(holder, holder.adapterPosition)
        }
    }

    override fun doubleClickAction(e: MotionEvent): Boolean {
        // Get interacted view from x,y coordinate.
        val childView = mRecyclerView.findChildViewUnder(e.x, e.y)

        if (childView != null) {
            // Find the view holder
            val holder = mRecyclerView.getChildViewHolder(childView) as AbstractViewHolder

            val row = holder.adapterPosition

            // Control to ignore selection color
            if (!mTableView.isIgnoreSelectionColors) {
                mSelectionHandler.setSelectedRowPosition(holder, row)
            }

            // Call ITableView listener for item click
            tableViewListener.onRowHeaderDoubleClicked(holder, row)
            return true
        }
        return false
    }
}
