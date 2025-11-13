package com.mushi.sample

import android.text.InputType
import com.mushi.customtableview.annotation.CellFieldType
import com.mushi.customtableview.annotation.ColumnIgnore
import com.mushi.customtableview.annotation.ColumnPosition
import com.mushi.customtableview.annotation.ColumnStatus

class DocumentRow {

//    @ColumnStatus
//    var rowStatus: Boolean = false

    @ColumnIgnore
    var ItemCode: String = ""

    @ColumnPosition(
        value = 1,
        heading = "Description",
        columnType = CellFieldType.Editable,
        inputType = InputType.TYPE_CLASS_TEXT
    )
    var ItemDescription: String = ""

    @ColumnPosition(value = 2, heading = "WHS")
    var WhsName: String = "General"

    @ColumnPosition(
        value = 3,
        heading = "WHS Qty",
        columnType = CellFieldType.Editable,
        inputType = InputType.TYPE_CLASS_NUMBER
    )
    var WhsQty: Double = 0.0

    @ColumnPosition(
        value = 4,
        heading = "Pair",
        columnType = CellFieldType.Editable,
        inputType = InputType.TYPE_CLASS_NUMBER
    )
    var Quantity: Double = 0.0

    @ColumnPosition(
        value = 5,
        heading = "Dozen",
        columnType = CellFieldType.Editable,
        inputType = InputType.TYPE_CLASS_NUMBER
    )
    var Dozen: Double = 0.0

    @ColumnPosition(value = 6, heading = "Committed")
    var Committed: Double = 0.0

    @ColumnPosition(
        value = 7,
        heading = "Unit Price",
        columnType = CellFieldType.Editable,
        inputType = InputType.TYPE_CLASS_NUMBER
    )
    var UnitPrice: Double = 0.0

    @ColumnIgnore
    var Tax: Double = 0.0

    @ColumnPosition(value = 8, heading = "Total")
    var LineTotal: Double = 0.0

    @ColumnPosition(value = 9, heading = "Active", columnType = CellFieldType.CheckBox)
    var isActive: Boolean = false

    @ColumnPosition(value = 10, heading = "Delete", columnType = CellFieldType.Action)
    var ActionDelete: Int = R.drawable.ic_delete
}
