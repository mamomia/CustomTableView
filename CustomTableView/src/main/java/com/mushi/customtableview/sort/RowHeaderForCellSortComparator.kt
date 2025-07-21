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

/**
 * Created by Mushi on 14/2/18.
 */
class RowHeaderForCellSortComparator(
    private val referenceList: List<ISortableModel>,
    private val columnList: List<List<ISortableModel>>,
    private val sortState: SortState
) : Comparator<List<ISortableModel>> {

    private val rowHeaderSortComparator = RowHeaderSortComparator(sortState)

    override fun compare(o1: List<ISortableModel>, o2: List<ISortableModel>): Int {
        val refIndex1 = columnList.indexOf(o1)
        val refIndex2 = columnList.indexOf(o2)

        // Fallback if index not found
        if (refIndex1 == -1 || refIndex2 == -1) return 0

        val content1 = referenceList[refIndex1].content
        val content2 = referenceList[refIndex2].content

        return if (sortState == SortState.DESCENDING) {
            rowHeaderSortComparator.compareContent(content2, content1)
        } else {
            rowHeaderSortComparator.compareContent(content1, content2)
        }
    }
}

