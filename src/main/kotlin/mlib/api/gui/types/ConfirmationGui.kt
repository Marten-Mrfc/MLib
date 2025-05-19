package mlib.api.gui.types

import mlib.api.gui.Gui
import mlib.api.gui.GuiSize
import mlib.api.utilities.asMini
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.function.Consumer

/**
 * Confirmation GUI for yes/no decisions
 */
class ConfirmationGui(
    private val title: Component,
    private val message: List<Component>,
    private val onConfirm: Consumer<Player>,
    private val onCancel: Consumer<Player>,
    private val size: GuiSize = GuiSize.ROW_THREE,
    private val confirmText: Component = "<green>Confirm".asMini(),
    private val cancelText: Component = "<red>Cancel".asMini(),
    private val confirmEnabled: Boolean = true,
    private val confirmMaterial: Material = Material.LIME_CONCRETE,
    private val cancelMaterial: Material = Material.RED_CONCRETE,
    private val confirmModel: Int = 11,
    private val cancelModel: Int = 15
    ) : GuiType {
    private val gui: Gui = Gui(title, size)

    init {
        setupGui()
    }

    private fun setupGui() {
        // Fill with gray glass panes
        gui.fill(Material.GRAY_STAINED_GLASS_PANE) {}

        // Information item in the center
        gui.item(Material.PAPER) {
            name(message.firstOrNull() ?: Component.empty())
            description(message.drop(1))
            slots(13)
            onClick { event -> event.isCancelled = true }
        }

        // Confirm button
        if(confirmEnabled) {
            gui.item(confirmMaterial) {
                name(confirmText)
                slots(11)
                modelData(confirmModel)
                onClick { event ->
                    event.isCancelled = true
                    onConfirm.accept(event.whoClicked as Player)
                }
            }
        } else {
            gui.item(cancelMaterial) {
                name(cancelText)
                modelData(cancelModel)
                description(listOf("<gray>Deze actie is niet beschikbaar!".asMini()))
                slots(11)
                onClick { event ->
                    event.isCancelled = true
                    onCancel.accept(event.whoClicked as Player)
                }
            }
        }

        // Cancel button
        gui.item(cancelMaterial) {
            name(cancelText)
            slots(15)
            modelData(cancelModel)
            onClick { event ->
                event.isCancelled = true
                onCancel.accept(event.whoClicked as Player)
            }
        }
    }

    override fun open(player: Player) {
        gui.open(player)
    }

    override fun update(player: Player) {
        open(player)
    }
}