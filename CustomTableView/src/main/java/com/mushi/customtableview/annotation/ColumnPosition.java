package com.mushi.customtableview.annotation;

import android.text.InputType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnPosition {
    int value() default 0;

    String heading();

    CellFieldType columnType() default CellFieldType.Data;

    int inputType() default InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE;
}