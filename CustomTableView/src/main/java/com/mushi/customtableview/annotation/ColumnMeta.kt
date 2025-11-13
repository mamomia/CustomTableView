package com.mushi.customtableview.annotation

import android.text.InputType

data class ColumnMeta(
    val id: String = "",
    val fieldName: String,
    val displayName: String,
    val position: Int,
    var selected: Boolean,
    var disabled: Boolean,
    val columnType: CellFieldType = CellFieldType.Data,
    val inputType: Int = InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
)