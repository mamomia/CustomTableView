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
package com.mushi.customtableview.layoutmanager

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mushi.customtableview.ITableView
import com.mushi.customtableview.adapter.recyclerview.CellRecyclerView
import com.mushi.customtableview.adapter.recyclerview.holder.AbstractViewHolder
import com.mushi.customtableview.util.TableViewUtils

/**
 * Created by Mushi on 10/06/2017.
 */
class ColumnLayoutManager(context: Context, private val mTableView: ITableView) :
    LinearLayoutManager(context) {
    private var mCellRowRecyclerView: CellRecyclerView? = null
    private val mColumnHeaderRecyclerView = mTableView.columnHeaderRecyclerView
    private val mColumnHeaderLayoutManager = mTableView.columnHeaderLayoutManager
    private val mCellLayoutManager = mTableView.cellLayoutManager

    var isNeedFit: Boolean = false
        private set
    private var mNeedFitForHorizontalScroll = false
    var lastDx: Int = 0
        private set
    private var mYPosition = 0

    init {
        // Set default orientation
        this.orientation = HORIZONTAL

        //If you are using a RecyclerView.RecycledViewPool, it might be a good idea to set this
        // flag to true so that views will be available to other RecyclerViews immediately.
        this.recycleChildrenOnDetach = true
    }

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        mCellRowRecyclerView = view as CellRecyclerView
        mYPosition = rowPosition
    }

    override fun measureChildWithMargins(child: View, widthUsed: Int, heightUsed: Int) {
        super.measureChildWithMargins(child, widthUsed, heightUsed)

        // If has fixed width is true, than calculation of the column width is not necessary.
        if (mTableView.hasFixedWidth()) {
            return
        }

        measureChild(child, widthUsed, heightUsed)
    }

    override fun measureChild(child: View, widthUsed: Int, heightUsed: Int) {
        val columnPosition = getPosition(child)

        // Get cached width size of column and cell
        val cacheWidth = mCellLayoutManager.getCacheWidth(mYPosition, columnPosition)
        val columnCacheWidth = mColumnHeaderLayoutManager.getCacheWidth(columnPosition)

        // Already each of them is same width size.
        if (cacheWidth != -1 && cacheWidth == columnCacheWidth) {
            // Control whether we need to set width or not.
            if (child.measuredWidth != cacheWidth) {
                TableViewUtils.setWidth(child, cacheWidth)
            }
        } else {
            val columnHeaderChild =
                mColumnHeaderLayoutManager.findViewByPosition(columnPosition) ?: return

            // Need to calculate which one has the broadest width ?
            fitWidthSize(
                child, mYPosition, columnPosition, cacheWidth, columnCacheWidth,
                columnHeaderChild
            )
        }

        // Control all of the rows which has same column position.
        if (shouldFitColumns(columnPosition, mYPosition)) {
            if (lastDx < 0) {
                Log.e(
                    LOG_TAG, "x: " + columnPosition + " y: " + mYPosition + " fitWidthSize " +
                            "left side "
                )
                mCellLayoutManager.fitWidthSize(columnPosition, true)
            } else {
                mCellLayoutManager.fitWidthSize(columnPosition, false)
                Log.e(
                    LOG_TAG, "x: " + columnPosition + " y: " + mYPosition + " fitWidthSize " +
                            "right side"
                )
            }
            isNeedFit = false
        }

        // It need to be cleared to prevent unnecessary calculation.
        mNeedFitForHorizontalScroll = false
    }

    private fun fitWidthSize(
        child: View,
        row: Int,
        column: Int,
        cellWidth: Int,
        columnHeaderWidth: Int,
        columnHeaderChild: View
    ) {
        var cellWidth = cellWidth
        var columnHeaderWidth = columnHeaderWidth
        if (cellWidth == -1) {
            // Alternatively, TableViewUtils.getWidth(child);
            cellWidth = child.measuredWidth
        }

        if (columnHeaderWidth == -1) {
            // Alternatively, TableViewUtils.getWidth(columnHeaderChild)
            columnHeaderWidth = columnHeaderChild.measuredWidth
        }

        if (cellWidth != 0) {
            if (columnHeaderWidth > cellWidth) {
                cellWidth = columnHeaderWidth
            } else if (cellWidth > columnHeaderWidth) {
                columnHeaderWidth = cellWidth
            }

            // Control whether column header needs to be change interns of width
            if (columnHeaderWidth != columnHeaderChild.width) {
                TableViewUtils.setWidth(columnHeaderChild, columnHeaderWidth)
                isNeedFit = true
                mNeedFitForHorizontalScroll = true
            }

            // Set the value to cache it for column header.
            mColumnHeaderLayoutManager.setCacheWidth(column, columnHeaderWidth)
        }


        // Set the width value to cache it for cell .
        TableViewUtils.setWidth(child, cellWidth)
        mCellLayoutManager.setCacheWidth(row, column, cellWidth)
    }

    private fun shouldFitColumns(xPosition: Int, yPosition: Int): Boolean {
        if (mNeedFitForHorizontalScroll) {
            if (!mCellRowRecyclerView!!.isScrollOthers && mCellLayoutManager.shouldFitColumns(
                    yPosition
                )
            ) {
                if (lastDx > 0) {
                    val last = findLastVisibleItemPosition()
                    //Log.e(LOG_TAG, "Warning: findFirstVisibleItemPosition is " + last);
                    return xPosition == last
                } else if (lastDx < 0) {
                    val first = findFirstVisibleItemPosition()
                    //Log.e(LOG_TAG, "Warning: findFirstVisibleItemPosition is " + first);
                    return xPosition == first
                }
            }
        }
        return false
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {
        if (mColumnHeaderRecyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE &&
            mCellRowRecyclerView!!.isScrollOthers
        ) {
            // Every CellRowRecyclerViews should be scrolled after the ColumnHeaderRecyclerView.
            // Because it is the main compared one to make each columns fit.
            mColumnHeaderRecyclerView.scrollBy(dx, 0)
        }
        // It is important to determine the next attached view to fit all columns
        lastDx = dx

        // Set the right initialPrefetch size to improve performance
        this.initialPrefetchItemCount = 2

        return super.scrollHorizontallyBy(dx, recycler, state)
    }

    private val rowPosition: Int
        get() = mCellLayoutManager.getPosition(mCellRowRecyclerView!!)

    fun clearNeedFit() {
        isNeedFit = false
    }

    val visibleViewHolders: Array<AbstractViewHolder?>
        get() {
            val visibleChildCount =
                findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1
            var index = 0

            val views =
                arrayOfNulls<AbstractViewHolder>(visibleChildCount)
            for (i in findFirstVisibleItemPosition()..<findLastVisibleItemPosition() + 1) {
                views[index] = mCellRowRecyclerView?.findViewHolderForAdapterPosition(i) as AbstractViewHolder?

                index++
            }
            return views
        }

    companion object {
        private val LOG_TAG: String = ColumnLayoutManager::class.java.simpleName
    }
}
