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
package com.mushi.customtableview.preference

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Mushi on 4.03.2018.
 */
open class Preferences : Parcelable {
    var rowPosition: Int = 0
    var rowPositionOffset: Int = 0
    var columnPosition: Int = 0
    var columnPositionOffset: Int = 0
    var selectedRowPosition: Int = 0
    var selectedColumnPosition: Int = 0

    constructor()

    protected constructor(`in`: Parcel) {
        rowPosition = `in`.readInt()
        rowPositionOffset = `in`.readInt()
        columnPosition = `in`.readInt()
        columnPositionOffset = `in`.readInt()
        selectedRowPosition = `in`.readInt()
        selectedColumnPosition = `in`.readInt()
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of [.writeToParcel],
     * the return value of this method must include the
     * [.CONTENTS_FILE_DESCRIPTOR] bit.
     *
     * @return a bitmask indicating the set of special object types marshaled by this Parcelable
     * object instance.
     */
    override fun describeContents(): Int {
        return 0
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written. May be 0 or [              ][.PARCELABLE_WRITE_RETURN_VALUE].
     */
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(rowPosition)
        dest.writeInt(rowPositionOffset)
        dest.writeInt(columnPosition)
        dest.writeInt(columnPositionOffset)
        dest.writeInt(selectedRowPosition)
        dest.writeInt(selectedColumnPosition)
    }

    companion object CREATOR : Parcelable.Creator<Preferences> {
        override fun createFromParcel(parcel: Parcel): Preferences {
            return Preferences(parcel)
        }

        override fun newArray(size: Int): Array<Preferences?> {
            return arrayOfNulls(size)
        }
    }
}
