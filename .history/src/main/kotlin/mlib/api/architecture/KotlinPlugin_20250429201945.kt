package mlib.api.architecture

import mlib.api.MLib
import mlib.api.utilities.Debug
import mlib.api.utilities.debug
import org.bukkit.plugin.java.JavaPlugin

/**
 * Base class for plugins using MLib.
 * Provides integration with MLib functionality.
 */
abstract class KotlinPlugin : JavaPlugin() {

	override fun onEnable() {
		try {
			MLib.register(this)
			debug(this, "MLib initialized for ${this.name}")
		} catch (e: IllegalStateException) {
			logger.warning("MLib already initialized for ${this.name}")
		}

		super.onEnable()
	}
	
	/**
	 * Log a debug message if debugging is enabled.
	 *
	 * @param message The message to log
	 */
	fun debug(message: String) {
		Debug.log(this, message)
	}

}