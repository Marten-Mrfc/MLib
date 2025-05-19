package mlib.api.gui.types

import org.bukkit.entity.Player

/**
 * Base interface for all GUI boosts
 */
interface GuiType {
    fun open(player: Player)
    fun update(player: Player)
}