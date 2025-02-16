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
    private var onClick: Consumer<ClickContext> = Consumer {}
    private var meta: ItemMeta? = null
    private var cached: ItemStack? = null

    fun name(name: Component) = apply { this.displayName = name }
    fun description(description: List<Component>) = apply { this.description = description }
    fun amount(amount: Int) = apply { this.amount = amount }
    fun slots(vararg slots: Int) = apply { this.slots = slots }
    fun meta(meta: ItemMeta) = apply { this.meta = meta }
    fun onClick(handler: Consumer<ClickContext>) = apply { this.onClick = handler }

    private fun createItem(): ItemStack {
        return cached ?: ItemStack(material, amount).apply {
            itemMeta = (meta ?: itemMeta)?.apply {
                this.displayName(this@GuiItem.displayName)
                this.lore(description)
            }
            cached = this
        }
    }

    internal fun addToInventory(inventory: Inventory, slot: Int) {
        inventory.setItem(slot, createItem())
        GuiItemProcessor.registerClickHandler(inventory, slot) { event ->
            onClick.accept(ClickContext(event))
        }
    }

    class ClickContext(private val event: InventoryClickEvent) {
        val player: Player get() = event.whoClicked as Player
        val clickType = event.click
        val slot = event.slot

        fun cancel() {
            event.isCancelled = true
        }
    }
}