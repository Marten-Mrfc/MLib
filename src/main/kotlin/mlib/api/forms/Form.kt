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
        player.message("<yellow>$question")
        player.message("<gray>Please enter your answer (Type: ${type.name})")
    }

    internal fun handleInput(player: Player, input: String, plugin: Plugin) {
        if (!type.validate(input)) {
            player.error("<red>Invalid input! Please enter a valid ${type.name.lowercase()}")
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