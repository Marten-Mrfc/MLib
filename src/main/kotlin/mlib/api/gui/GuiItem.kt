package mlib.api.gui

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.function.Consumer

class GuiItem(private val material: Material) {
    private var displayName: Component? = Component.empty()
    private var description: List<Component> = emptyList()
    private var amount: Int = 1
    internal var slots: IntArray = intArrayOf()
    private var onClick: Consumer<InventoryClickEvent> = Consumer {}
    private var meta: ItemMeta? = null
    private var cached: ItemStack? = null
    private var modelData: Int? = null

    fun name(name: Component) = apply { this.displayName = name }
    fun description(description: List<Component>) = apply { this.description = description }
    fun amount(amount: Int) = apply { this.amount = amount }
    fun slots(vararg slots: Int) = apply { this.slots = slots }
    fun meta(meta: ItemMeta) = apply { this.meta = meta }
    fun onClick(handler: Consumer<InventoryClickEvent>) = apply { this.onClick = handler }
    fun modelData(modelData: Int) = apply { this.modelData = modelData }

    private fun createItem(): ItemStack {
        return cached ?: ItemStack(material, amount).apply {
            itemMeta = (meta ?: itemMeta)?.apply {
                this.displayName(this@GuiItem.displayName)
                this.lore(description)
                this.setCustomModelData(modelData)
            }
            cached = this
        }
    }

    internal fun addToInventory(inventory: Inventory, slot: Int) {
        inventory.setItem(slot, createItem())
        GuiItemProcessor.registerClickHandler(inventory, slot) { event ->
            onClick.accept(event)
        }
    }
}