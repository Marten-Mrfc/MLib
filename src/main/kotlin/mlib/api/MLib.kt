package mlib.api

import mlib.api.commands.CommandController
import mlib.api.gui.InventoryClickListener
import mlib.api.utilities.Initializable
import org.bukkit.plugin.Plugin

class MLib internal constructor(internal val plugin: Plugin) {

	companion object {
		private val _instances = mutableMapOf<Plugin, MLib>()
		val instances: Map<Plugin, MLib> get() = _instances

		fun register(plugin: Plugin): MLib {
			check(plugin !in instances) { "Api for this plugin already initialized." }
			return MLib(plugin)
		}
	}

	internal val commandController = CommandController(plugin)
	internal val guiListener = InventoryClickListener(plugin)
	internal val controllers = arrayOf<Initializable>(
		commandController,
		guiListener
	)

	init {
		_instances[plugin] = this

		val controllers = controllers
		for (controller in controllers) {
			controller.initialize()
		}
	}
}