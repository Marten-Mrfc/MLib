package mlib.api.forms

import mlib.api.utilities.error
import mlib.api.utilities.message
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class Form(
    private val question: String,
    private val type: FormType,
    val duration: Int,
    private val onSubmit: (Player, Any) -> Unit
) {
    fun show(player: Player) {
        player.closeInventory()
        FormSession.create(player, this)
        player.message("§e$question")
        player.message("§7Please enter your answer (Type: ${type.name})")
    }

    internal fun handleInput(player: Player, input: String, plugin: Plugin) {
        if (!type.validate(input)) {
            player.error("§cInvalid input! Please enter a valid ${type.name.lowercase()}")
            return
        }

        val parsed = type.parse(input)
        // Run callback on main thread
        plugin.server.scheduler.scheduleSyncDelayedTask(plugin) {
            onSubmit(player, parsed)
        }
        FormSession.remove(player)
    }
}