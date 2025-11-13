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

package com.mushi.customtableview.util;

import static java.util.stream.Collectors.toList;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mushi.customtableview.R;
import com.mushi.customtableview.annotation.CellFieldType;
import com.mushi.customtableview.annotation.ColumnIgnore;
import com.mushi.customtableview.annotation.ColumnPosition;
import com.mushi.customtableview.annotation.ColumnStatus;
import com.mushi.customtableview.model.ColumnHeader;
import com.mushi.customtableview.model.RowHeader;
import com.mushi.customtableview.model.Cell;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Mushi on 18/09/2017.
 */

public class TableViewUtils {

    /**
     * Helps to force width value before calling requestLayout by the system.
     */
    public static void setWidth(@NonNull View view, int width) {
        // Change width value from params
        view.getLayoutParams().width = width;

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), View
                .MeasureSpec.EXACTLY);
        view.measure(widthMeasureSpec, heightMeasureSpec);

        view.requestLayout();
    }

    public static <T> List<List<Cell>> getCellList(List<T> dataList, Class<T> clazz) {
        try {
            List<List<Cell>> list = new ArrayList<>();
            if (dataList.isEmpty()) return list;
            for (T item : dataList) {
                int id = 0;
                List<Cell> cellList = new ArrayList<>();
                List<Field> fields = sortListByAnnotation(clazz.getDeclaredFields());
                for (Field field : fields) {
                    CellFieldType columnType = field.getAnnotation(ColumnPosition.class).columnType();
                    int inputType = field.getAnnotation(ColumnPosition.class).inputType();

                    if (!field.isAnnotationPresent(ColumnIgnore.class) && columnType != null &&
                            (columnType == CellFieldType.Data ||
                                    columnType == CellFieldType.Action ||
                                    columnType == CellFieldType.Editable ||
                                    columnType == CellFieldType.CheckBox
                            )
                    ) {
                        field.setAccessible(true);
                        Object text = field.get(item);
                        id++;
                        cellList.add(new Cell(String.valueOf(id), text, columnType, inputType));
                    }
                }
                list.add(cellList);
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static <T> List<Cell> getCell(T item, Class<T> clazz, int position) {
        return getCell(item, clazz, position, 1);
    }

    public static <T> List<Cell> getCell(T item, Class<T> clazz, int position, int cursor) {
        try {
            int id = 0;
            List<Cell> cellList = new ArrayList<>();
            List<Field> fields = sortListByAnnotation(clazz.getDeclaredFields());
            for (Field field : fields) {
                CellFieldType columnType = field.getAnnotation(ColumnPosition.class).columnType();
                int inputType = field.getAnnotation(ColumnPosition.class).inputType();
                if (!field.isAnnotationPresent(ColumnIgnore.class) && columnType != null &&
                        (columnType == CellFieldType.Data ||
                                columnType == CellFieldType.Action ||
                                columnType == CellFieldType.Editable ||
                                columnType == CellFieldType.CheckBox)
                ) {
                    field.setAccessible(true);
                    Object text = field.get(item);
                    id++;
                    cellList.add(new Cell(String.valueOf(id), text, columnType, inputType, cursor, (position == id)));
                }
            }
            return cellList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    @NonNull
    public static <T> RowHeader getRowHeader(Context context, T item, Class<T> clazz, int position) {
        RowHeader header = new RowHeader(String.valueOf((position + 1)), String.valueOf((position + 1)), resolveRowHeaderColor(context, null));
        // check if has status column
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ColumnStatus.class)) {
                field.setAccessible(true);
                try {
                    Object text = field.get(item);
                    header.setBackgroundColor(resolveRowHeaderColor(context,
                            ((text.toString().equalsIgnoreCase("true")) ? "cell_header_active_color" : "cell_header_deactive_color")
                    ));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            }
        }
        return header;
    }

    @NonNull
    public static <T> List<RowHeader> getRowHeaderList(Context context, Class<T> clazz, List<T> dataList) {
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            RowHeader header = new RowHeader(String.valueOf((i + 1)), String.valueOf((i + 1)), resolveRowHeaderColor(context, null));
            // check if has status column
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ColumnStatus.class)) {
                    field.setAccessible(true);
                    try {
                        Object text = field.get(dataList.get(i));
                        header.setBackgroundColor(resolveRowHeaderColor(context,
                                ((text.toString().equalsIgnoreCase("true")) ? "cell_header_active_color" : "cell_header_deactive_color")
                        ));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
            }
            list.add(header);
        }
        return list;
    }

    @NonNull
    public static <T> List<ColumnHeader> getColumnHeaderList(Class<T> clazz) {
        return getColumnHeaderList(clazz, true);
    }

    @NonNull
    public static <T> List<ColumnHeader> getColumnHeaderList(Class<T> clazz, boolean showActionColumns) {
        List<ColumnHeader> list = new ArrayList<>();
        List<Field> fields = sortListByAnnotation(clazz.getDeclaredFields());
        for (int i = 1; i <= fields.size(); i++) {
            Field field = fields.get(i - 1);
            CellFieldType columnType = field.getAnnotation(ColumnPosition.class).columnType();
            if (!field.isAnnotationPresent(ColumnIgnore.class) && columnType != null &&
                    (columnType == CellFieldType.Data || columnType == CellFieldType.Action || columnType == CellFieldType.Editable || columnType == CellFieldType.CheckBox)) {
                if (!showActionColumns && columnType == CellFieldType.Action)
                    continue;

                field.setAccessible(true);
                String fieldName = field.getAnnotation(ColumnPosition.class).heading();
                list.add(new ColumnHeader(String.valueOf(i), fieldName));
            }
        }
        return list;
    }

    @NonNull
    public static List<ColumnHeader> getColumnHeaderList(String... args) {
        List<ColumnHeader> list = new ArrayList<>();
        for (int i = 1; i <= args.length; i++) {
            String columnName = args[i - 1];
            list.add(new ColumnHeader(String.valueOf(i), columnName));
        }
        return list;
    }

    public static List<Field> sortListByAnnotation(Field[] fields) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Arrays.stream(fields).filter(m -> m.getAnnotation(ColumnPosition.class) != null) // only use annotated methods
                    .sorted(Comparator.comparingInt(m -> m.getAnnotation(ColumnPosition.class).value())) // sort by value
                    .collect(toList());
        } else {
            List<Field> result = filter(Arrays.asList(fields), type -> type.getAnnotation(ColumnPosition.class) != null);
            Collections.sort(result, (o1, o2) -> {
                int pos1 = o1.getAnnotation(ColumnPosition.class).value();
                int pos2 = o2.getAnnotation(ColumnPosition.class).value();
                return Integer.compare(pos1, pos2); // or manually: return pos1 - pos2;
            });
            return result;
        }
    }

    public static <T> boolean isColumnEditable(Class<T> clazz, int columnIndex) {
        try {
            List<Field> fields = sortListByAnnotation(clazz.getDeclaredFields());
            Field field = fields.get(columnIndex);
            CellFieldType columnType = field.getAnnotation(ColumnPosition.class).columnType();
            return (columnType != null && columnType == CellFieldType.Editable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static int resolveRowHeaderColor(Context context, String colorName) {
        try {
            if (colorName == null || colorName.isEmpty()) colorName = "cell_header_def_color";
            // Try to get the color ID dynamically — allows override from app if same name exists
            int colorResId = context.getResources()
                    .getIdentifier(colorName, "color", context.getPackageName());

            // If not found in app, fallback to library resource
            if (colorResId == 0 && colorName.equals("cell_header_def_color")) {
                colorResId = R.color.cell_header_def_color;
            } else if (colorResId == 0 && colorName.equals("cell_header_active_color")) {
                colorResId = R.color.cell_header_active_color;
            } else if (colorResId == 0 && colorName.equals("cell_header_deactive_color")) {
                colorResId = R.color.cell_header_deactive_color;
            }

            // Convert to actual color int
            return context.getColor(colorResId);

        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to a safe default color (e.g., light gray)
            return Color.LTGRAY;
        }
    }

    public interface Predicate<T> {
        boolean apply(T type);
    }

    public static <T> List<T> filter(List<T> col, Predicate<T> predicate) {
        List<T> result = new ArrayList<T>();
        for (T element : col) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }

    public static <T> List<T> filterType(List<?> listA, Class<T> c) {
        List<T> listB = new ArrayList<T>();
        for (Object a : listA) {
            if (c.isInstance(a)) {
                listB.add(c.cast(a));
            }
        }
        return listB;
    }

    private static class NumericTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }

}
