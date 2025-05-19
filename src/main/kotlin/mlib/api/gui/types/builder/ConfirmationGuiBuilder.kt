package mlib.api.gui.types.builder

import mlib.api.gui.GuiSize
import mlib.api.gui.types.ConfirmationGui
import mlib.api.utilities.asMini
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.function.Consumer

class ConfirmationGuiBuilder {
    private var title: Component = "".asMini()
    private var message: MutableList<Component> = mutableListOf("Are you sure?".asMini())
    private var size: GuiSize = GuiSize.ROW_THREE
    private var onConfirm: Consumer<Player> = Consumer {}
    private var onCancel: Consumer<Player> = Consumer {}
    private var confirmText: Component = "<green>Confirm".asMini()
    private var cancelText: Component = "<red>Cancel".asMini()
    private var confirmEnabled: Boolean = true
    private var confirmMaterial: Material = Material.GREEN_CONCRETE
    private var cancelMaterial: Material = Material.RED_CONCRETE
    private var confirmModel: Int = 0
    private var cancelModel: Int = 0

    fun title(title: Component) = apply { this.title = title }
    fun message(message: Component) = apply { this.message = mutableListOf(message) }
    fun message(message: List<Component>) = apply { this.message = message.toMutableList() }
    fun size(size: GuiSize) = apply { this.size = size }
    fun onConfirm(handler: Consumer<Player>) = apply { this.onConfirm = handler }
    fun onCancel(handler: Consumer<Player>) = apply { this.onCancel = handler }
    fun confirmText(text: Component) = apply { this.confirmText = text }
    fun cancelText(text: Component) = apply { this.cancelText = text }
    fun confirmEnabled(enabled: Boolean) = apply { this.confirmEnabled = enabled }
    fun confirmMaterial(material: Material) = apply { this.confirmMaterial = material }
    fun cancelMaterial(material: Material) = apply { this.cancelMaterial = material }
    fun confirmModel(model: Int) = apply { this.confirmModel = model }
    fun cancelModel(model: Int) = apply { this.cancelModel = model }

    fun build() = ConfirmationGui(
        title, message, onConfirm, onCancel, size,
        confirmText, cancelText, confirmEnabled,
        confirmMaterial, cancelMaterial, confirmModel, cancelModel
    )
}