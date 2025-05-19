package mlib.api.gui.types

import mlib.api.gui.Gui
import mlib.api.gui.GuiSize
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * Standard GUI type for basic menus
 */
class StandardGui(
    private val title: Component,
    val size: GuiSize = GuiSize.ROW_THREE
) : GuiType {
    private val gui: Gui = Gui(title, size)

    override fun open(player: Player) {
        gui.open(player)
    }

    override fun update(player: Player) {
        open(player)
    }

    fun item(material: Material, init: mlib.api.gui.GuiItem.() -> Unit) = gui.item(material, init)

    fun fill(material: Material, init: mlib.api.gui.GuiItem.() -> Unit = {}) = gui.fill(material, init)

    fun border(material: Material, init: mlib.api.gui.GuiItem.() -> Unit = {}) = gui.border(material, init)
}