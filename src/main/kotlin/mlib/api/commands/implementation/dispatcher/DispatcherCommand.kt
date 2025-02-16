package mlib.api.commands.implementation.dispatcher

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.Message
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.exceptions.CommandSyntaxException
import mlib.api.commands.types.ChatCommandSyntaxException
import mlib.api.utilities.Messaging.asMini
import mlib.api.utilities.isOverridden
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginIdentifiableCommand
import org.bukkit.plugin.Plugin
import java.lang.reflect.Method
import kotlin.math.max
import kotlin.math.min

class DispatcherCommand(
	name: String,
	private val plugin: Plugin,
	val dispatcher: CommandDispatcher<CommandSender>,
	usage: String,
	aliases: List<String>
) : Command(name, "", usage, aliases), PluginIdentifiableCommand {

	override fun execute(sender: CommandSender, label: String, vararg arguments: String): Boolean {
		if (!testPermission(sender)) {
			return true
		}

		val reader = StringReader(join(label, arguments))

		if (reader.canRead() && reader.peek() == '/') {
			reader.skip()
		}

		try {
			dispatcher.execute(reader, sender)
		} catch (exception: CommandSyntaxException) {
			if (exception is ChatCommandSyntaxException) {
				sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>${exception.chatMessage}</red>"))
			}
			else {
				sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>${exception.rawMessage.toComponent()}</red>"))
			}

			report(sender, exception.input, exception.cursor)
		}

		return true
	}

	override fun getPlugin(): Plugin {
		return plugin
	}

	private fun report(sender: CommandSender, input: String?, cursor: Int) {
		if (input == null || cursor < 0) return

		val index = min(input.length, cursor)
		val errorStart = input.lastIndexOf(' ', index - 1) + 1

		val failedCommandMessage = Component.text().colorIfAbsent(NamedTextColor.GRAY)

		if (errorStart > 10) {
			failedCommandMessage.append(Component.text("..."))
		}
		failedCommandMessage.append(Component.text(input.substring(max(0, errorStart - 10), errorStart)))

		if (errorStart < input.length) {
			val error = Component.text(input.substring(errorStart, cursor)).colorIfAbsent(NamedTextColor.RED).decorate(
				TextDecoration.UNDERLINED)
			failedCommandMessage.append(error)
		}

		val context = Component.translatable("command.context.here").colorIfAbsent(NamedTextColor.RED).style(Style.style(TextDecoration.ITALIC))
		failedCommandMessage.append(context)

		sender.sendMessage(failedCommandMessage.build())
	}

	private fun Message.toComponent(): Component {
		val messageClass = this::class.java

		return if (messageClass.simpleName == "ChatMessage") {
			verifyChatMessageClass(messageClass)
			val key = chatMessageGetKeyMethod.invoke(this) as String
			val args = chatMessageGetArgsMethod.invoke(this) as Array<*>
			Component.translatable(key, *args.map { Component.text(it.toString()) }.toTypedArray())
		}
		else {
			string.asMini()
		}
	}

	private fun join(name: String, arguments: Array<out String>): String {
		var command = "/$name"
		if (arguments.isNotEmpty()) {
			command += " ${arguments.joinToString(" ")}"
		}
		return command
	}

	private companion object {
		lateinit var chatMessageGetKeyMethod: Method
			private set

		lateinit var chatMessageGetArgsMethod: Method
			private set

		fun verifyChatMessageClass(chatMessageClass: Class<*>) {
			if (::chatMessageGetKeyMethod.isInitialized) return

			chatMessageGetKeyMethod = chatMessageClass.methods
				.find { it.returnType == String::class.java && !it.isOverridden }!!
			chatMessageGetArgsMethod = chatMessageClass.methods
				.find { it.returnType == Array<Any>::class.java && !it.isOverridden }!!
		}
	}
}