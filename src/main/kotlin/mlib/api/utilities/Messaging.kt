package mlib.api.utilities

import mlib.api.MLib
import mlib.api.architecture.extensions.server
import mlib.api.architecture.extensions.softDepend
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

private val mm = MiniMessage.builder().build()

private fun getPlugin(any: Any): Plugin? {
    return when (any) {
        is Plugin -> any
        is CommandSender -> server.pluginManager.plugins.firstOrNull { plugin ->
            MLib.instances.containsKey(plugin)
        }
        else -> null
    }
}

private fun String.withPlaceholders(player: Player?): String {
    return if (player != null) {
        try {
            Class.forName("me.clip.placeholderapi.PlaceholderAPI")
            me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, this)
        } catch (e: ClassNotFoundException) {
            this
        }
    } else {
        this
    }
}

fun String.asMini(player: Player? = null): Component {
    return mm.deserialize(withPlaceholders(player))
}

fun Component.notMiniText(): String {
    return mm.serialize(this).replace("\\<", "<")
}

fun Component.notMini(): String {
    return mm.serialize(this)
}

fun CommandSender.sendMini(message: String, player: Player? = null) {
    sendMessage(message.withPlaceholders(player).asMini())
}

fun CommandSender.error(message: String, player: Player? = null) {
    sendMessage("<red><bold>Error</bold><gray> | <white> $message".withPlaceholders(player).asMini())
}

fun CommandSender.message(message: String, player: Player? = null) {
    val plugin = getPlugin(this) ?: return
    sendMessage("<gold><bold>${plugin.name}</bold><gray> | <white> $message".withPlaceholders(player).asMini())
}

fun String.asPluginMessage(player: Player? = null): Component {
    val plugin = getPlugin(this) ?: return asMini(player)
    return "<gold><bold>${plugin.name}</bold><gray> | <white> $this".withPlaceholders(player).asMini(player)
}

fun CommandSender.action(message: String) {
    if (this is Player) {
        sendActionBar { message.asMini(this) }
    } else {
        sendMessage(message.asMini())
    }
}