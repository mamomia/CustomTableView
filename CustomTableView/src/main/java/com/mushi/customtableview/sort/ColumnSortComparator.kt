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
 * Created by Mushi on 25.11.2017.
 */
class ColumnSortComparator(
    private val xPosition: Int,
    private val sortState: SortState
) : AbstractSortComparator(), Comparator<List<ISortableModel>> {

    override fun compare(t1: List<ISortableModel>, t2: List<ISortableModel>): Int {
        val o1 = t1[xPosition].content
        val o2 = t2[xPosition].content

        return if (sortState == SortState.DESCENDING) {
            compareContent(o2, o1)
        } else {
            compareContent(o1, o2)
        }
    }
}
