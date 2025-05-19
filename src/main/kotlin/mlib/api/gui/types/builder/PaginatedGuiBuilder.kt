package mlib.api.gui.types.builder

import mlib.api.gui.Gui
import mlib.api.gui.GuiSize
import mlib.api.gui.types.PaginatedGui
import mlib.api.utilities.asMini
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.function.Consumer

class PaginatedGuiBuilder {
    private var title: Component = "".asMini()
    private var size: GuiSize = GuiSize.ROW_SIX
    private var items: MutableList<PaginatedGui.PaginatedItem> = mutableListOf()
    private var itemClickHandler: ((Player, ItemStack, Int) -> Unit)? = null
    private var extraSetup: ((Gui) -> Unit)? = null
    private var backgroundMaterial: Material? = Material.BLACK_STAINED_GLASS_PANE
    private var onClick: Consumer<InventoryClickEvent> = Consumer {}
    fun title(title: Component) = apply { this.title = title }
    fun size(size: GuiSize) = apply { this.size = size }
    fun setBackground(material: Material) = apply { this.backgroundMaterial = material }
    fun addItem(material: Material, name: Component, description: List<Component> = emptyList(), amount: Int = 1, meta: ItemMeta? = null) = apply {
        items.add(PaginatedGui.PaginatedItem(material, name, description, amount, meta))
    }
    fun onClick(handler: Consumer<InventoryClickEvent>) = apply { this.onClick = handler }
    fun onItemClick(handler: (Player, ItemStack, Int) -> Unit) = apply {
        this.itemClickHandler = handler
    }

    fun customizeGui(setup: (Gui) -> Unit) = apply {
        this.extraSetup = setup
    }

    fun build(): PaginatedGui {
        val gui = PaginatedGui(title, size)
        gui.setItems(items)
        gui.setBackground(backgroundMaterial)
        if (itemClickHandler != null) {
            gui.onItemClick(itemClickHandler!!)
        }

        if (extraSetup != null) {
            gui.setExtraSetup(extraSetup!!)
        }

        return gui
    }
}