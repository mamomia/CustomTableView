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

import androidx.core.util.ObjectsCompat
import androidx.recyclerview.widget.DiffUtil

/**
 * Created by Mushi on 23.11.2017.
 */
class ColumnSortCallback(
    private val mOldCellItems: List<List<ISortableModel>>,
    private val mNewCellItems: List<List<ISortableModel>>,
    private val mColumnPosition: Int
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldCellItems.size
    }

    override fun getNewListSize(): Int {
        return mNewCellItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Control for precaution from IndexOutOfBoundsException
        if (mOldCellItems.size > oldItemPosition && mNewCellItems.size > newItemPosition) {
            if (mOldCellItems[oldItemPosition].size > mColumnPosition && mNewCellItems[newItemPosition].size > mColumnPosition) {
                // Compare ids
                val oldId = mOldCellItems[oldItemPosition][mColumnPosition].id
                val newId = mNewCellItems[newItemPosition][mColumnPosition].id
                return oldId == newId
            }
        }
        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Control for precaution from IndexOutOfBoundsException
        if (mOldCellItems.size > oldItemPosition && mNewCellItems.size > newItemPosition) {
            if (mOldCellItems[oldItemPosition].size > mColumnPosition && mNewCellItems[newItemPosition].size > mColumnPosition) {
                // Compare contents
                val oldContent = mOldCellItems[oldItemPosition][mColumnPosition]
                    .content
                val newContent = mNewCellItems[newItemPosition][mColumnPosition]
                    .content
                return ObjectsCompat.equals(oldContent, newContent)
            }
        }
        return false
    }
}
