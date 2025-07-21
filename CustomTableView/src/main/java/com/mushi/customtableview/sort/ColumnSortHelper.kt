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
package com.mushi.customtableview.sort

import com.mushi.customtableview.adapter.recyclerview.holder.AbstractSorterViewHolder
import com.mushi.customtableview.layoutmanager.ColumnHeaderLayoutManager

/**
 * Created by Mushi on 15.12.2017.
 */
class ColumnSortHelper(private val mColumnHeaderLayoutManager: ColumnHeaderLayoutManager) {
    private val mSortingColumns: MutableList<Directive> = ArrayList()

    private fun sortingStatusChanged(column: Int, sortState: SortState) {
        val holder = mColumnHeaderLayoutManager.getViewHolder(column)

        if (holder != null) {
            if (holder is AbstractSorterViewHolder) {
                holder.onSortingStatusChanged(sortState)
            } else {
                throw IllegalArgumentException(
                    "Column Header ViewHolder must extend " +
                            "AbstractSorterViewHolder"
                )
            }
        }
    }

    fun setSortingStatus(column: Int, status: SortState) {
        val directive = getDirective(column)
        if (directive !== EMPTY_DIRECTIVE) {
            mSortingColumns.remove(directive)
        }
        if (status != SortState.UNSORTED) {
            mSortingColumns.add(Directive(column, status))
        }

        sortingStatusChanged(column, status)
    }

    fun clearSortingStatus() {
        mSortingColumns.clear()
    }

    val isSorting: Boolean
        get() = mSortingColumns.size != 0

    fun getSortingStatus(column: Int): SortState {
        return getDirective(column).direction
    }

    private fun getDirective(column: Int): Directive {
        for (i in mSortingColumns.indices) {
            val directive = mSortingColumns[i]
            if (directive.column == column) {
                return directive
            }
        }
        return EMPTY_DIRECTIVE
    }

    private class Directive(val column: Int, val direction: SortState)

    companion object {
        private val EMPTY_DIRECTIVE = Directive(-1, SortState.UNSORTED)
    }
}
