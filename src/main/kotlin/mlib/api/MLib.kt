package mlib.api

import mlib.api.architecture.extensions.registerEvents
import mlib.api.commands.CommandController
import mlib.api.gui.InventoryClickListener
import mlib.api.forms.FormSession
import mlib.api.forms.FormListener
import mlib.api.utilities.ConfigManager
import mlib.api.utilities.Debug
import mlib.api.utilities.Initializable
import mlib.api.utilities.debug
import org.bukkit.plugin.Plugin

class MLib internal constructor(internal val plugin: Plugin) {

	companion object {
		private val _instances = mutableMapOf<Plugin, MLib>()
		val instances: Map<Plugin, MLib> get() = _instances
		const val VERSION = "1.0.0"

		fun register(plugin: Plugin): MLib {
			check(plugin !in instances) { "Api for this plugin already initialized." }
			return MLib(plugin)
		}
	}

	internal val commandController = CommandController(plugin)
	internal val guiListener = InventoryClickListener(plugin)
	internal val formListener = FormListener()
	internal val controllers = arrayOf<Initializable>(
		commandController,
		guiListener
	)
	init {
		_instances[plugin] = this

		// Initialize config
		ConfigManager.initializeConfig(plugin)

		val controllers = controllers
		for (controller in controllers) {
			controller.initialize()
		}

		FormSession.initialize(plugin)
		plugin.registerEvents(formListener)
		
		if (Debug.isEnabled(plugin)) {
			plugin.logger.info("[MLib] Debug mode enabled")
		}
	}
}