# MLib Debug System Documentation

## Overview

The MLib Debug System provides a robust and flexible way to add debugging capabilities to your Minecraft plugins. The system allows for conditional logging based on configuration settings and supports both simple messages and template-based debugging with variable substitution.

## Features

- **Configuration-based activation**: Debug mode can be toggled in each plugin's config.yml
- **String template support**: Use variable placeholders like `${playerName}` in debug messages
- **MLib version tracking**: Automatically maintains a config-version entry that tracks the MLib version
- **Lightweight impact**: Minimal performance impact when debugging is disabled
- **Extension methods**: Convenient API for calling debug functions from various contexts

## Setup

### 1. Plugin Configuration

Add the following to your plugin's `config.yml`:

```yaml
# Debug settings
debug: false # Set to true to enable debug mode

# Version tracking - Do not modify manually
config-version: 1.0.0 # MLib version - Do Not Change
```

### 2. Plugin Initialization

Make sure your plugin extends `KotlinPlugin` from MLib:

```kotlin
class YourPlugin : KotlinPlugin() {
    override fun onEnable() {
        super.onEnable()
        // Your initialization code
    }
}
```

## Usage

### Basic Debugging

```kotlin
// Using the direct Debug class
Debug.log(plugin, "Loading arena configuration")

// Using the utility function
debug(plugin, "Loading arena configuration")

// Using the plugin extension (if your plugin extends KotlinPlugin)
debug("Loading arena configuration")
```

### Template-based Debugging

Template debugging allows you to include dynamic values in your debug messages:

```kotlin
// Using named variables
val player = event.player
val selection = getSelection(player)

debug(plugin, "getSelection for ${player.name}: firstPos=${selection?.firstPosition != null} secondPos=${selection?.secondPosition != null}")

// Or using the template API with variables map
Debug.logTemplate(plugin, "getSelection for ${playerName}: firstPos=${hasFirstPos} secondPos=${hasSecondPos}",
    mapOf(
        "playerName" to player.name,
        "hasFirstPos" to (selection?.firstPosition != null),
        "hasSecondPos" to (selection?.secondPosition != null)
    )
)

// Or using the convenience function with pairs
debug(plugin, "getSelection for ${playerName}: firstPos=${hasFirstPos} secondPos=${hasSecondPos}",
    "playerName" to player.name,
    "hasFirstPos" to (selection?.firstPosition != null),
    "hasSecondPos" to (selection?.secondPosition != null)
)
```

## Debug Commands

You can add a debug command to your plugin:

```kotlin
literal("debug") {
    requiresPermissions("yourplugin.debug")
    executes {
        val newState = !plugin.config.getBoolean("debug", false)
        plugin.config.set("debug", newState)
        plugin.saveConfig()

        // Update the debug state in MLib
        Debug.updateState(plugin)

        source.message("Debug mode is now ${if (newState) "enabled" else "disabled"}.")
    }
}
```

## Best Practices

1. **Be selective**: Add debug statements to key areas of your code that might need troubleshooting
2. **Add context**: Include relevant IDs, names, and states to make debug output useful
3. **Group logically**: Organize debug messages to trace execution flow
4. **Log boundaries**: Add debug statements at the beginning and end of major operations
5. **Don't overdo it**: Too many debug messages can cause clutter and performance issues

## Advanced Usage

### Checking Debug State

You can check if debugging is enabled before performing expensive operations:

```kotlin
if (Debug.isEnabled(plugin)) {
    // Perform expensive debug operation
    val detailedInfo = generateDetailedDebugInformation()
    debug(plugin, detailedInfo)
}
```

### Runtime Toggle

You can create commands to toggle debug mode at runtime:

```kotlin
fun toggleDebug() {
    val newState = !plugin.config.getBoolean("debug", false)
    plugin.config.set("debug", newState)
    plugin.saveConfig()

    // Update the debug state in MLib
    Debug.updateState(plugin)

    plugin.logger.info("Debug mode is now ${if (newState) "enabled" else "disabled"}.")
}
```
