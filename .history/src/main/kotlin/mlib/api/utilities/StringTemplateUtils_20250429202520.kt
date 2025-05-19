package mlib.api.utilities

/**
 * Format a string by resolving placeholders like ${name} using provided variables.
 * This is a utility function to support string templates in debug messages.
 *
 * @param args Map of variable names to their values
 * @return The formatted string with placeholders replaced
 */
fun String.formatTemplate(vararg args: Pair<String, Any?>): String {
    val pattern = Regex("\\$\\{([^}]*)\\}")
    val argsMap = args.toMap()
    
    return pattern.replace(this) { matchResult ->
        val variableName = matchResult.groupValues[1]
        argsMap[variableName]?.toString() ?: matchResult.value
    }
}

/**
 * Enhanced debug function that supports string templates.
 * Usage example: debug("Player ${player.name} has ${health} health", "player" to player, "health" to 20)
 *
 * @param template The template string with ${variable} placeholders
 * @param args Named pairs of variable names and values
 */
fun debug(template: String, vararg args: Pair<String, Any?>) {
    val message = template.formatTemplate(*args)
    debug(message)
}
