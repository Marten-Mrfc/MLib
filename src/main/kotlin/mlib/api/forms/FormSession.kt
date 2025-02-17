package mlib.api.forms

import mlib.api.utilities.error
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.UUID

object FormSession {
    private val sessions = mutableMapOf<UUID, SessionData>()
    private lateinit var plugin: Plugin

    data class SessionData(
        val form: Form,
        val expireAt: Long
    )

    fun initialize(plugin: Plugin) {
        this.plugin = plugin
    }

    fun create(player: Player, form: Form) {
        val expireAt = System.currentTimeMillis() + (form.duration * 1000)
        sessions[player.uniqueId] = SessionData(form, expireAt)

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            if (hasActiveForm(player)) {
                player.error("<red>Form expired!")
                remove(player)
            }
        }, form.duration * 20L)
    }

    fun hasActiveForm(player: Player) = sessions.containsKey(player.uniqueId)
    fun getForm(player: Player) = sessions[player.uniqueId]?.form
    fun remove(player: Player) = sessions.remove(player.uniqueId)
}