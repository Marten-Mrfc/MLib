package mlib.api.utilities

import mlib.api.MLib
import org.bukkit.plugin.Plugin
import java.util.logging.Level

/**
 * Debug utility for MLib and plugins using MLib.
 * Handles debug logging based on configuration settings.
 */
object Debug {
    private val enabledPlugins = mutableMapOf<Plugin, Boolean>()
    private const val CONFIG_DEBUG_KEY = "debug"
    private const val DEFAULT_DEBUG_VALUE = false

    /**
     * Log a debug message if debugging is enabled for the plugin.
     *
     * @param plugin The plugin context for the debug message
     * @param message The message to log
     */
    fun log(plugin: Plugin, message: String) {
        if (isEnabled(plugin)) {
            plugin.logger.log(Level.INFO, "[DEBUG] $message")
        }
    }

    /**
     * Log a debug message if debugging is enabled for the plugin.
     * Supports string template format with ${variable} syntax.
     *
     * @param plugin The plugin context for the debug message
     * @param template The template string with ${variable} placeholders
     * @param variables Map of variable names to their values
     */
    fun logTemplate(plugin: Plugin, template: String, variables: Map<String, Any?>) {
        if (isEnabled(plugin)) {
            val message = template.replace(Regex("\\$\\{([^}]*)\\}")) { matchResult ->
                val variableName = matchResult.groupValues[1]
                variables[variableName]?.toString() ?: matchResult.value
            }
            plugin.logger.log(Level.INFO, "[DEBUG] $message")
        }
    }

    /**
     * Log a debug message if debugging is enabled for the plugin.
     * This extension function allows for cleaner syntax.
     *
     * @param message The message to log
     */
    fun Plugin.debug(message: String) {
        log(this, message)
    }

    /**
     * Log a debug message with template support if debugging is enabled.
     * This extension function allows for cleaner syntax with templates.
     *
     * @param template The template string with ${variable} placeholders
     * @param variables Map of variable names to their values
     */
    fun Plugin.debugTemplate(template: String, variables: Map<String, Any?>) {
        logTemplate(this, template, variables)
    }

    /**
     * Check if debugging is enabled for the plugin.
     *
     * @param plugin The plugin to check
     * @return true if debugging is enabled, false otherwise
     */
    fun isEnabled(plugin: Plugin): Boolean {
        return enabledPlugins.getOrElse(plugin) {
            // If not cached, check config and cache the result
            val enabled = plugin.config.getBoolean(CONFIG_DEBUG_KEY, DEFAULT_DEBUG_VALUE)
            enabledPlugins[plugin] = enabled
            enabled
        }
    }

    /**
     * Update the debug state for a plugin.
     * This is typically called when config is reloaded.
     *
     * @param plugin The plugin to update
     */
    fun updateState(plugin: Plugin) {
        val enabled = plugin.config.getBoolean(CONFIG_DEBUG_KEY, DEFAULT_DEBUG_VALUE)
        enabledPlugins[plugin] = enabled
    }

    /**
     * Reset the cached debug state for a plugin.
     * This forces the system to check the config next time.
     *
     * @param plugin The plugin to reset
     */
    fun resetState(plugin: Plugin) {
        enabledPlugins.remove(plugin)
    }

    /**
     * Reset all cached debug states.
     */
    fun resetAllStates() {
        enabledPlugins.clear()
    }
}

/**
 * Extension function to log a debug message from any context.
 * Attempts to find the relevant plugin for the caller.
 *
 * @param message The message to log
 */
fun debug(message: String) {
    val plugin = MLib.instances.keys.firstOrNull() ?: return
    Debug.log(plugin, message)
}

/**
 * Extension function to log a debug message for a specific plugin.
 *
 * @param plugin The plugin context for the debug message
 * @param message The message to log
 */
fun debug(plugin: Plugin, message: String) {
    Debug.log(plugin, message)
}
