package mlib.api.forms

import io.papermc.paper.event.player.AsyncChatEvent
import mlib.api.MLib
import mlib.api.utilities.notMiniText
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class FormListener : Listener {
    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        val player = event.player
        val form = FormSession.getForm(player) ?: return
        val plugin = MLib.instances.keys.first()

        event.isCancelled = true
        form.handleInput(player, event.message().notMiniText(), plugin)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        FormSession.remove(event.player)
    }
}