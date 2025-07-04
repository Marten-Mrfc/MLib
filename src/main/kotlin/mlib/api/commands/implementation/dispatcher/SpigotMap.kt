package mlib.api.commands.implementation.dispatcher

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.tree.LiteralCommandNode
import mlib.api.commands.implementation.tree.nodes.AliasableCommandNode
import mlib.api.utilities.debug
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.SimpleCommandMap
import org.bukkit.plugin.Plugin
import java.lang.reflect.Field

internal class SpigotMap(var prefix: String, var plugin: Plugin, var map: SimpleCommandMap) : PlatformMap {

	private val knownCommands: MutableMap<String, Command> =
		mapKnownCommandsField.get(map) as MutableMap<String, Command>

	lateinit var dispatcher: CommandDispatcher<CommandSender>

	override fun register(node: LiteralCommandNode<CommandSender>): DispatcherCommand? {
		debug("SpigotMap attempting to register command '${node.name}'")
		debug("knownCommands contains '${node.name}': ${knownCommands.containsKey(node.name)}")
		
		if (knownCommands.containsKey(node.name)) {
			debug("Command '${node.name}' already exists, returning null")
			return null
		}
		val wrapped = wrap(node)
		debug("Registering wrapped command '${node.name}' with map")
		map.register(prefix, wrapped)
		debug("Successfully registered command '${node.name}'")
		return wrapped
	}

	/**
	 * Register a command with support for replacing existing commands
	 */
	fun registerWithReplace(node: LiteralCommandNode<CommandSender>, replace: Boolean = false): DispatcherCommand? {
		debug("SpigotMap attempting to register command '${node.name}' with replace=$replace")
		debug("knownCommands contains '${node.name}': ${knownCommands.containsKey(node.name)}")
		
		if (knownCommands.containsKey(node.name)) {
			if (replace) {
				debug("Command '${node.name}' already exists, unregistering first")
				unregister(node.name)
			} else {
				debug("Command '${node.name}' already exists and replace=false, returning null")
				return null
			}
		}
		
		val wrapped = wrap(node)
		debug("Registering wrapped command '${node.name}' with map")
		map.register(prefix, wrapped)
		debug("Successfully registered command '${node.name}'")
		return wrapped
	}

	override fun unregister(name: String): DispatcherCommand? {
		val commands = knownCommands
		val command = commands[name] as? DispatcherCommand ?: return null
		commands.remove(name, command)
		commands.remove("$prefix:$name", command)
		for (alias in command.aliases) {
			commands.remove(alias, command)
			commands.remove("$prefix:$alias", command)
		}
		command.unregister(map)
		return command
	}

	fun wrap(node: LiteralCommandNode<CommandSender>): DispatcherCommand {
		val aliases = ArrayList<String>()
		if (node is AliasableCommandNode<*>) {
			for (alias in (node as AliasableCommandNode<*>).aliases) {
				aliases.add(alias.name)
			}
		}
		val command = DispatcherCommand(node.name, plugin, dispatcher, node.usageText, aliases)
		debug("Created DispatcherCommand '${node.name}' with tab completion support")
		return command
	}



	private companion object {
		val mapKnownCommandsField: Field = SimpleCommandMap::class.java
			.getDeclaredField("knownCommands").apply {
				isAccessible = true
			}
	}
}