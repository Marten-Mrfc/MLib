package mlib.api.commands.builders

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.ParsedArgument
import org.bukkit.command.CommandSender
import java.lang.reflect.Field

class BrigadierCommandContext<S : CommandSender> internal constructor(
	actualContext: CommandContext<S>,
	val arguments: MutableMap<String, ParsedArgument<S, *>> = contextArgumentsField.get(actualContext) as MutableMap<String, ParsedArgument<S, *>>
) : CommandContext<S>(
	actualContext.source, actualContext.input, arguments,
	actualContext.command, actualContext.rootNode, actualContext.nodes,
	actualContext.range, actualContext.child, actualContext.redirectModifier, actualContext.isForked
) {

	inline fun <reified T> getArgument(name: String): T {
		return getArgument(name, T::class.java)
	}

	inline fun <reified T> ArgumentDefinition<T>.get(): T {
		return getArgument(name)
	}



	private companion object {
		val contextArgumentsField: Field = CommandContext::class.java
			.getDeclaredField("arguments").apply {
				isAccessible = true
			}
	}
}


data class ArgumentDefinition<T>(
	val name: String,
	val type: ArgumentType<T>
)