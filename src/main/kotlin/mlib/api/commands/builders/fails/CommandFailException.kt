package mlib.api.commands.builders.fails

import net.kyori.adventure.text.TextComponent
import org.bukkit.command.CommandSender

class CommandFailException(
	val failMessage: TextComponent? = null,
	val source: CommandSender
) : RuntimeException() {

	override val message: String?
		get() = failMessage?.toString()

}