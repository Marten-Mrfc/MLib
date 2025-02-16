package mlib.api.commands.builders

import mlib.api.commands.implementation.dispatcher.Dispatcher
import mlib.api.commands.implementation.tree.nodes.BrigadierLiteral
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

@DslMarker
@Retention(AnnotationRetention.BINARY)
annotation class NodeBuilderDSLMarker


inline fun Plugin.command(
	name: String,
	register: Boolean = true,
	crossinline builder: LiteralDSLBuilder.() -> Unit
): BrigadierLiteral<CommandSender> {
	val node = LiteralDSLBuilder(this, name)
		.apply(builder).build()

	if (register) Dispatcher.of(this).register(node)

	return node
}