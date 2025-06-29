package mlib.api.commands.implementation.tree.nodes

import com.mojang.brigadier.Command
import com.mojang.brigadier.tree.CommandNode
import com.mojang.brigadier.tree.LiteralCommandNode
import com.mojang.brigadier.tree.RootCommandNode
import mlib.api.commands.implementation.dispatcher.PlatformMap
import mlib.api.utilities.debug
import org.bukkit.command.CommandSender
import org.bukkit.Bukkit

/**
 * A `RootCommandNode` subclass that facilities the wrapping and registration
 * of a `CommandNode` to a `CommandMap`.
 */
class BrigadierRoot(private val prefix: String, val map: PlatformMap) :
	RootCommandNode<CommandSender>(), MutableCommandNode<CommandSender> {

	/**
	 * Adds the `command` if the provided `DispatcherMap` does not contain
	 * a command with the same name. In addition, a fallback alias of the `command`
	 * is always created and added. If the `command` implements [mlib.api.dsl.command.implementation.tree.nodes.AliasableCommandNode],
	 * the aliases and the fallback of the aliases are also added in a similar fashion.
	 *
	 * @param child the command to be added
	 * @param replace if true, will replace existing command with same name instead of throwing error
	 * @throws IllegalArgumentException if the `command` is not a `LiteralCommandNode`
	 */
	fun addChild(child: CommandNode<CommandSender>, replace: Boolean = false) {
		debug("BrigadierRoot.addChild called with command '${child.name}', replace=$replace")
		
		if (!replace) {
			require(getChild(child.name) == null) { "Invalid command: '" + child.name + "', root already contains a child with the same name" }
		} else {
			// Remove existing command if it exists
			getChild(child.name)?.let { existing ->
				debug("Replacing existing command: ${existing.name}")
				removeChild(existing.name)
			}
		}
		require(child is LiteralCommandNode<*>) { "Invalid command: '" + child.name + "', commands registered to root must be a literal" }

		val literal = child as LiteralCommandNode<CommandSender>
		debug("Attempting to register command '${child.name}' with platform")
		debug("Map type: ${map.javaClass.simpleName}")
		debug("Literal name: ${literal.name}")
		
		val wrapper = if (map is mlib.api.commands.implementation.dispatcher.SpigotMap) {
			map.registerWithReplace(literal, replace)
		} else {
			map.register(literal)
		}
		
		if (wrapper == null) {
			debug("ERROR: Platform registration returned null wrapper for '${child.name}'")
			debug("This indicates the platform map failed to register the command")
			return
		}
		
		debug("Successfully registered command wrapper for '${child.name}'")
		debug("Wrapper name: ${wrapper.name}, label: ${wrapper.label}")
		super.addChild(literal.createAlias("$prefix:${literal.name}"))
		if (wrapper.name == wrapper.label) {
			super.addChild(literal)
			Bukkit.getLogger().info("[MLib] Added main command node for '${child.name}'")
		}
		if (literal is AliasableCommandNode<*>) {
			for (alias in ArrayList((literal as AliasableCommandNode<CommandSender>).aliases)) {
				if (wrapper.aliases.contains(alias!!.name)) {
					super.addChild(literal.createAlias("$prefix:${alias.name}"))
					super.addChild(alias)
					Bukkit.getLogger().info("[MLib] Added alias '${alias.name}' for command '${child.name}'")
				}
			}
		}
		Bukkit.getLogger().info("[MLib] Command registration complete for '${child.name}'")
	}

	/**
	 * Original addChild method - maintains backward compatibility
	 */
	override fun addChild(child: CommandNode<CommandSender>) {
		addChild(child, false)
	}

	override fun removeChild(child: String): CommandNode<CommandSender> {
		return removeAliasedChild(child)
	}

	override fun getRedirect(): CommandNode<CommandSender> = super.getRedirect()
	override fun setRedirect(redirect: CommandNode<CommandSender>?) {}

	override fun getCommand(): Command<CommandSender> = super.getCommand()
	override fun setCommand(command: Command<CommandSender>?) {}
}