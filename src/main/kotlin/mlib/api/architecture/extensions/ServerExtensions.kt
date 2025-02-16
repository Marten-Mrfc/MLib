// src/main/kotlin/mlib/api/extensions/server/ServerExtensions.kt

package mlib.api.architecture.extensions

import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import mlib.api.commands.provideCommandController
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.Command
import org.bukkit.command.SimpleCommandMap
import org.bukkit.event.Listener
import org.bukkit.permissions.Permissible
import org.bukkit.plugin.Plugin
import java.lang.reflect.Field
import kotlin.collections.iterator

val server get() = Bukkit.getServer()

var Server.whitelist: Boolean
	get() = hasWhitelist()
	set(value) = setWhitelist(value)

fun Plugin.registerEvents(
	vararg listeners: Listener
) = listeners.forEach { server.pluginManager.registerEvents(it, this) }

fun Plugin.registerSuspendingEvents(
	vararg listeners: Listener
) = listeners.forEach { server.pluginManager.registerSuspendingEvents(it, this) }

fun Permissible.allPermissions(vararg permissions: String): Boolean = permissions.all { hasPermission(it) }

fun Permissible.hasPermissionOrStar(permission: String): Boolean =
	hasPermission(permission) || hasPermission(permission.replaceAfterLast('.', "*"))

private val serverCommands: SimpleCommandMap by lazy {
	server::class.java.getDeclaredField("commandMap").apply {
		isAccessible = true
	}.get(server) as SimpleCommandMap
}

private val knownCommandsField: Field by lazy {
	SimpleCommandMap::class.java.getDeclaredField("knownCommands").apply {
		isAccessible = true
	}
}

fun Command.unregister(plugin: Plugin) {
	try {
		val knownCommands = knownCommandsField.get(serverCommands) as MutableMap<String, Command>
		val toRemove = ArrayList<String>()
		for ((key, value) in knownCommands) {
			if (value === this) {
				toRemove.add(key)
			}
		}
		for (name in toRemove) {
			knownCommands.remove(name)
		}

		provideCommandController(plugin)?.commands?.removeIf { it === this }
	} catch (e: Exception) {
		e.printStackTrace()
	}
}