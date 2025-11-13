package com.mushi.customtableview.annotation

enum class CellFieldType(val type: String) {
    Header("Header"),
    Data("Data"),
    Action("Action"),
    Editable("Editable"),
    CheckBox("CheckBox");

    companion object {
        /**
         * Safely parse a string into a CellFieldType.
         * Returns null if not found.
         */
        fun fromType(type: String?): CellFieldType? {
            return entries.firstOrNull { it.type.equals(type, ignoreCase = true) }
        }

        /**
         * Same as fromType but returns a default value instead of null.
         */
        fun fromTypeOrDefault(type: String?, default: CellFieldType = Data): CellFieldType {
            return entries.firstOrNull { it.type.equals(type, ignoreCase = true) } ?: default
        }
    }
}
