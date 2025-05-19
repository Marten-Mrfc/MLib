package mlib.api.utilities

import mlib.api.MLib
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin
import java.io.File

/**
 * Configuration manager for MLib and plugins using MLib.
 * Handles configuration access, versioning, and defaults.
 */
object ConfigManager {
    private const val CONFIG_VERSION_KEY = "config-version"
    private val loadedConfigs = mutableMapOf<Plugin, FileConfiguration>()

    /**
     * Initialize the configuration for a plugin.
     * Creates default config values if they don't exist.
     *
     * @param plugin The plugin to initialize configuration for
     */
    fun initializeConfig(plugin: Plugin) {
        // Ensure config file exists
        if (!File(plugin.dataFolder, "config.yml").exists()) {
            plugin.saveDefaultConfig()
        }

        // Load the config
        plugin.reloadConfig()
        
        // Add default MLib values if they don't exist
        val config = plugin.config
        
        // Set debug value if it doesn't exist
        if (!config.contains("debug")) {
            config.set("debug", false)
            plugin.saveConfig()
        }
        
        // Set version tracking
        val currentVersion = MLib.VERSION
        if (!config.contains(CONFIG_VERSION_KEY) || config.getString(CONFIG_VERSION_KEY) != currentVersion) {
            config.set(CONFIG_VERSION_KEY, currentVersion)
            plugin.saveConfig()
        }
        
        // Cache the config
        loadedConfigs[plugin] = config
        
        // Update debug state
        Debug.updateState(plugin)
    }

    /**
     * Reload the configuration for a plugin.
     *
     * @param plugin The plugin to reload configuration for
     */
    fun reloadConfig(plugin: Plugin) {
        plugin.reloadConfig()
        loadedConfigs[plugin] = plugin.config
        Debug.updateState(plugin)
    }

    /**
     * Get a boolean value from the configuration.
     *
     * @param plugin The plugin to get configuration for
     * @param key The configuration key
     * @param defaultValue The default value if the key doesn't exist
     * @return The configuration value
     */
    fun getBoolean(plugin: Plugin, key: String, defaultValue: Boolean = false): Boolean {
        return plugin.config.getBoolean(key, defaultValue)
    }

    /**
     * Get a string value from the configuration.
     *
     * @param plugin The plugin to get configuration for
     * @param key The configuration key
     * @param defaultValue The default value if the key doesn't exist
     * @return The configuration value
     */
    fun getString(plugin: Plugin, key: String, defaultValue: String = ""): String {
        return plugin.config.getString(key, defaultValue) ?: defaultValue
    }

    /**
     * Get an integer value from the configuration.
     *
     * @param plugin The plugin to get configuration for
     * @param key The configuration key
     * @param defaultValue The default value if the key doesn't exist
     * @return The configuration value
     */
    fun getInt(plugin: Plugin, key: String, defaultValue: Int = 0): Int {
        return plugin.config.getInt(key, defaultValue)
    }

    /**
     * Get a double value from the configuration.
     *
     * @param plugin The plugin to get configuration for
     * @param key The configuration key
     * @param defaultValue The default value if the key doesn't exist
     * @return The configuration value
     */
    fun getDouble(plugin: Plugin, key: String, defaultValue: Double = 0.0): Double {
        return plugin.config.getDouble(key, defaultValue)
    }
}
