package mlib.api.gui.types

import mlib.api.gui.Gui
import mlib.api.gui.GuiSize
import mlib.api.utilities.notMini
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.function.Consumer

/**
 * Paginated GUI for displaying large amounts of items
 */
class PaginatedGui(
    private val baseTitle: Component,
    private val size: GuiSize = GuiSize.ROW_SIX
) : GuiType {
    private var items: List<PaginatedItem> = mutableListOf()
    private var currentPage = 0
    private var itemsPerPage: Int = size.slots - 9 // Reserve bottom row for navigation
    private var onItemClick: ((Player, ItemStack, Int) -> Unit)? = null
    private var extraSetup: ((Gui) -> Unit)? = null
    private var backgroundMaterial: Material? = Material.BLACK_STAINED_GLASS_PANE
    private var onClick: Consumer<InventoryClickEvent> = Consumer {}

    fun onClick(handler: Consumer<InventoryClickEvent>) = apply { this.onClick = handler }

    fun setItems(items: List<PaginatedItem>) {
        this.items = items
        currentPage = 0
    }

    fun onItemClick(handler: (Player, ItemStack, Int) -> Unit) {
        this.onItemClick = handler
    }

    fun setExtraSetup(setup: (Gui) -> Unit) {
        this.extraSetup = setup
    }

    fun setBackground(material: Material?) {
        this.backgroundMaterial = material
    }

    fun nextPage() {
        if (currentPage < maxPage()) {
            currentPage++
        }
    }

    fun previousPage() {
        if (currentPage > 0) {
            currentPage--
        }
    }

    private fun maxPage(): Int = if (items.isEmpty()) 0 else (items.size - 1) / itemsPerPage

    override fun open(player: Player) {
        val gui = createPageGui()
        gui.open(player)
    }

    override fun update(player: Player) {
        open(player)
    }

    private fun createPageGui(): Gui {
        val pageTitle = if (maxPage() > 0) {
            Component.text("${baseTitle.notMini()} (${currentPage + 1}/${maxPage() + 1})")
        } else {
            baseTitle
        }

        val gui = Gui(pageTitle, size)

        // Fill with background if specified
        backgroundMaterial?.let { material ->
            gui.fill(material) {}
        }

        // Add items for current page
        val startIndex = currentPage * itemsPerPage
        val endIndex = minOf(startIndex + itemsPerPage, items.size)

        for (i in startIndex until endIndex) {
            val idx = i - startIndex
            val item = items[i]

            gui.item(item.material) {
                name(item.name)
                description(item.description)
                if (item.amount > 1) amount(item.amount)
                slots(idx)
                if (item.meta != null) meta(item.meta)
                onClick { event ->
                    event.isCancelled = true
                    onItemClick?.invoke(event.whoClicked as Player, event.currentItem ?: return@onClick, i)
                }
            }
        }

        // Add navigation controls in bottom row
        val lastRow = size.slots - 9

        // Previous page button
        if (currentPage > 0) {
            gui.item(Material.ARROW) {
                name(Component.text("Previous Page"))
                description(listOf(Component.text("Go to page $currentPage")))
                slots(lastRow)

                onClick { event ->
                    event.isCancelled = true
                    previousPage()
                    update(event.whoClicked as Player)
                }
            }
        }

        // Next page button
        if (currentPage < maxPage()) {
            gui.item(Material.ARROW) {
                name(Component.text("Next Page"))
                description(listOf(Component.text("Go to page ${currentPage + 2}")))
                slots(lastRow + 8)

                onClick { event ->
                    event.isCancelled = true
                    nextPage()
                    update(event.whoClicked as Player)
                }
            }
        }

        // Apply any extra setup
        extraSetup?.invoke(gui)

        return gui
    }

    class PaginatedItem(
        val material: Material,
        val name: Component,
        val description: List<Component>,
        val amount: Int = 1,
        val meta: ItemMeta? = null
    )
}