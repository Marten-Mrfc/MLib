package mlib.api.gui

enum class GuiSize(val slots: Int) {
    ROW_ONE(9),
    ROW_TWO(18),
    ROW_THREE(27),
    ROW_FOUR(36),
    ROW_FIVE(45),
    ROW_SIX(54);

    companion object {
        fun fromRows(rows: Int) = GuiSize.entries.firstOrNull { it.slots == rows * 9 } ?: ROW_SIX
    }
}