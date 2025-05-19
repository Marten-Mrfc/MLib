package mlib.api.gui.types.builder

import mlib.api.gui.types.StandardGui
import mlib.api.gui.GuiSize
import mlib.api.utilities.asMini
import net.kyori.adventure.text.Component

class StandardGuiBuilder {
    private var title: Component = "".asMini()
    private var size: GuiSize = GuiSize.ROW_THREE
    private var setupAction: (StandardGui) -> Unit = {}

    fun title(title: Component) = apply { this.title = title }
    fun size(size: GuiSize) = apply { this.size = size }
    fun setup(setup: (StandardGui) -> Unit) = apply { this.setupAction = setup }

    fun build(): StandardGui {
        val gui = StandardGui(title, size)
        setupAction(gui)
        return gui
    }
}