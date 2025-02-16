package mlib.api.utilities

import mlib.api.architecture.extensions.softDepend
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class Messaging(private val plugin: Plugin) : Initializable {
    private val mm = MiniMessage.builder().build()
    private val placeholderAPI by lazy { plugin.softDepend<Plugin>("PlaceholderAPI") }

    override fun initialize() {}

    private fun String.withPlaceholders(player: Player?): String {
        return if (player != null && placeholderAPI != null) {
            me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, this)
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
        sendMessage("<gold><bold>${plugin.name}</bold><gray> | <white> $message".withPlaceholders(player).asMini())
    }

    fun message(message: String, player: Player? = null): Component {
        return "<gold><bold>${plugin.name}</bold><gray> | <white> $message".withPlaceholders(player).asMini()
    }
}