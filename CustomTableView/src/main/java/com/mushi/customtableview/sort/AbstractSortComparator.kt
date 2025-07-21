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

import java.util.Date

/**
 * Created by Mushi on 6/2/18.
 */
abstract class AbstractSortComparator {

    fun compareContent(o1: Any?, o2: Any?): Int {
        return when {
            o1 == null && o2 == null -> 0
            o1 == null -> -1
            o2 == null -> 1
            o1 is Comparable<*> && o1::class == o2::class -> {
                @Suppress("UNCHECKED_CAST")
                (o1 as Comparable<Any>).compareTo(o2)
            }

            o1 is Number && o2 is Number -> compare(o1, o2)
            o1 is String && o2 is String -> o1.compareTo(o2)
            o1 is Date && o2 is Date -> compare(o1, o2)
            o1 is Boolean && o2 is Boolean -> compare(o1, o2)
            else -> o1.toString().compareTo(o2.toString())
        }
    }

    private fun compare(o1: Number, o2: Number): Int {
        return o1.toDouble().compareTo(o2.toDouble())
    }

    private fun compare(o1: Date, o2: Date): Int {
        return o1.time.compareTo(o2.time)
    }

    private fun compare(o1: Boolean, o2: Boolean): Int {
        return o1.compareTo(o2)
    }
}
