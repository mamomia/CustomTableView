package com.mushi.customtableview.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ColumnIndex(val value: Int, val heading: String = "")