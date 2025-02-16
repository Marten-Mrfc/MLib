package mlib.api.gui

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import java.util.concurrent.ConcurrentHashMap

class Gui(private val title: Component, size: GuiSize) {
    private val size = size.slots
    private val items = ConcurrentHashMap<Int, GuiItem>()
    private var fillItem: GuiItem? = null

    fun item(material: Material, init: GuiItem.() -> Unit): GuiItem {
        val item = GuiItem(material).apply(init)
        item.slots.forEach { slot ->
            require(slot < size) { "Slot $slot is outside of inventory bounds" }
            items[slot] = item
        }
        return item
    }

    fun fill(material: Material, init: GuiItem.() -> Unit = {}) {
        fillItem = GuiItem(material).apply(init)
        for (i in 0 until size) {
            if (!items.containsKey(i)) {
                items[i] = fillItem!!
            }
        }
    }

    fun border(material: Material, init: GuiItem.() -> Unit = {}) {
        val item = GuiItem(material).apply(init)
        val rows = size / 9
        for (i in 0 until size) {
            if (i < 9 || i >= size - 9 || i % 9 == 0 || i % 9 == 8) {
                if (!items.containsKey(i)) {
                    items[i] = item
                }
            }
        }
    }

    fun open(player: Player) {
        val inventory = Bukkit.createInventory(null, size, title)
        items.forEach { (slot, item) -> item.addToInventory(inventory, slot) }
        player.openInventory(inventory)
    }
}