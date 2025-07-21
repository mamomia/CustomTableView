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
import android.util.SparseIntArray
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mushi.customtableview.ITableView
import com.mushi.customtableview.adapter.recyclerview.holder.AbstractViewHolder
import com.mushi.customtableview.util.TableViewUtils

/**
 * Created by Mushi on 30/07/2017.
 */
class ColumnHeaderLayoutManager(context: Context, private val mTableView: ITableView) :
    LinearLayoutManager(context) {
    //private SparseArray<Integer> mCachedWidthList;
    private val mCachedWidthList = SparseIntArray()

    init {
        this.orientation = HORIZONTAL
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
        // If has fixed width is true, than calculation of the column width is not necessary.
        if (mTableView.hasFixedWidth()) {
            super.measureChild(child, widthUsed, heightUsed)
            return
        }

        val position = getPosition(child)
        val cacheWidth = getCacheWidth(position)

        // If the width value of the cell has already calculated, then set the value
        if (cacheWidth != -1) {
            TableViewUtils.setWidth(child, cacheWidth)
        } else {
            super.measureChild(child, widthUsed, heightUsed)
        }
    }

    fun setCacheWidth(position: Int, width: Int) {
        mCachedWidthList.put(position, width)
    }

    fun getCacheWidth(position: Int): Int {
        return mCachedWidthList[position, -1]
    }

    val firstItemLeft: Int
        get() {
            val firstColumnHeader =
                findViewByPosition(findFirstVisibleItemPosition())
            return firstColumnHeader!!.left
        }

    /**
     * Helps to recalculate the width value of the cell that is located in given position.
     */
    fun removeCachedWidth(position: Int) {
        mCachedWidthList.removeAt(position)
    }

    /**
     * Clears the widths which have been calculated and reused.
     */
    fun clearCachedWidths() {
        mCachedWidthList.clear()
    }

    fun customRequestLayout() {
        var left = firstItemLeft
        var right: Int
        for (i in findFirstVisibleItemPosition()..<findLastVisibleItemPosition() + 1) {
            // Column headers should have been already calculated.

            right = left + getCacheWidth(i)

            val columnHeader = findViewByPosition(i)
            columnHeader!!.left = left
            columnHeader.right = right

            layoutDecoratedWithMargins(
                columnHeader,
                columnHeader.left,
                columnHeader.top,
                columnHeader.right,
                columnHeader.bottom
            )

            // + 1 is for decoration item.
            left = right + 1
        }
    }

    val visibleViewHolders: Array<AbstractViewHolder?>
        get() {
            val visibleChildCount =
                findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1
            var index = 0

            val views =
                arrayOfNulls<AbstractViewHolder>(visibleChildCount)
            for (i in findFirstVisibleItemPosition()..<findLastVisibleItemPosition() + 1) {
                views[index] = mTableView.columnHeaderRecyclerView
                    .findViewHolderForAdapterPosition(i) as AbstractViewHolder?

                index++
            }
            return views
        }

    fun getViewHolder(xPosition: Int): AbstractViewHolder? {
        return mTableView.columnHeaderRecyclerView
            .findViewHolderForAdapterPosition(xPosition) as AbstractViewHolder?
    }
}
