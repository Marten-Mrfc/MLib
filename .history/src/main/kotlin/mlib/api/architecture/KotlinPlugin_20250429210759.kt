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
	
	/**
	 * Log a debug message with template support if debugging is enabled.
	 *
	 * @param template The template string with ${variable} placeholders
	 * @param variables Pairs of variable names and their values
	 */
	fun debugTemplate(template: String, vararg variables: Pair<String, Any?>) {
		Debug.logTemplate(this, template, variables.toMap())
	}
	
	/**
	 * Check if debugging is enabled for this plugin.
	 *
	 * @return true if debugging is enabled, false otherwise
	 */
	fun isDebugEnabled(): Boolean {
		return Debug.isEnabled(this)
	}
	
	/**
	 * Toggle debug mode for this plugin.
	 *
	 * @return The new debug state
	 */
	fun toggleDebug(): Boolean {
		return Debug.toggleDebug(this)
	}
}