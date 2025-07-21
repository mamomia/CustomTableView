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
package com.mushi.customtableview.listener

import android.view.View
import android.view.View.OnLayoutChangeListener
import android.view.ViewGroup
import com.mushi.customtableview.ITableView

/**
 * Created by Mushi on 21.01.2018.
 */
class TableViewLayoutChangeListener(tableView: ITableView) : OnLayoutChangeListener {
    private val mCellRecyclerView = tableView.cellRecyclerView
    private val mColumnHeaderRecyclerView = tableView.columnHeaderRecyclerView
    private val mCellLayoutManager = tableView.cellLayoutManager

    override fun onLayoutChange(
        v: View,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        oldLeft: Int,
        oldTop: Int,
        oldRight: Int,
        oldBottom: Int
    ) {
        if (v.isShown && (right - left) != (oldRight - oldLeft)) {
            // Control who need the remeasure

            if (mColumnHeaderRecyclerView.width > mCellRecyclerView.width) {
                // Remeasure all nested CellRow recyclerViews
                mCellLayoutManager.remeasureAllChild()
            } else if (mCellRecyclerView.width > mColumnHeaderRecyclerView.width) {
                // It seems Column Header is needed.

                mColumnHeaderRecyclerView.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                mColumnHeaderRecyclerView.requestLayout()
            }
        }
    }
}
