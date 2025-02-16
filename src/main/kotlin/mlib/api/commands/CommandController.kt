package mlib.api.commands

import mlib.api.MLib
import mlib.api.architecture.extensions.registerEvents
import mlib.api.architecture.extensions.unregister
import mlib.api.utilities.Initializable
import org.bukkit.command.Command
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin

internal fun provideCommandController(plugin: Plugin) = MLib.instances[plugin]?.commandController


internal class CommandController(val plugin: Plugin) : Listener, Initializable {

	val commands = mutableListOf<Command>()


	override fun initialize() {
		plugin.registerEvents(this)
	}


	@EventHandler
	fun onPluginDisableEvent(event: PluginDisableEvent) {
		if (event.plugin != plugin) return

		commands.forEach {
			it.unregister(plugin)
		}
	}
}