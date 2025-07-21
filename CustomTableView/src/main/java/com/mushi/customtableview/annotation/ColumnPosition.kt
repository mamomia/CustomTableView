package com.mushi.customtableview.annotation

import android.text.InputType

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ColumnPosition(
    val value: Int = 0,
    val heading: String,
    val columnType: CellFieldType = CellFieldType.Data,
    val inputType: Int = InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
)