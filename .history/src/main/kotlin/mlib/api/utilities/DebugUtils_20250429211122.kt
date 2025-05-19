package mlib.api.utilities

import org.bukkit.plugin.Plugin

/**
 * Global debug utility function.
 * This function provides a convenient way to log debug messages from anywhere in the code.
 * 
 * @param plugin The plugin context for the debug message
 * @param message The message to log
 */
fun debug(plugin: Plugin, message: String) {
    Debug.log(plugin, message)
}

/**
 * Global debug utility function with template support.
 * This function supports string template format with ${variable} syntax.
 * 
 * Example: debug(plugin, "Player ${playerName} has ${health} health", 
 *                "playerName" to player.name, "health" to 20)
 * 
 * @param plugin The plugin context for the debug message
 * @param template The template string with ${variable} placeholders
 * @param variables Pairs of variable names and their values
 */
fun debug(plugin: Plugin, template: String, vararg variables: Pair<String, Any?>) {
    Debug.logTemplate(plugin, template, variables.toMap())
}

/**
 * Toggle debug mode for a plugin and save the setting to config.
 * 
 * @param plugin The plugin to toggle debug mode for
 * @return The new debug state
 */
fun toggleDebug(plugin: Plugin): Boolean {
    return Debug.toggleDebug(plugin)
}
