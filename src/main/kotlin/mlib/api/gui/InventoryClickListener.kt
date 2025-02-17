package mlib.api.gui

import mlib.api.utilities.Initializable
import mlib.api.architecture.extensions.registerEvents
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.plugin.Plugin

// In InventoryClickListener.kt
internal class InventoryClickListener(val plugin: Plugin) : Listener, Initializable {
    override fun initialize() {
        plugin.registerEvents(this)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        GuiItemProcessor.handleClick(event)
    }
}