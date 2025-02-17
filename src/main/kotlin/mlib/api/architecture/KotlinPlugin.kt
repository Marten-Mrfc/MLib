package mlib.api.architecture

import mlib.api.MLib
import org.bukkit.plugin.java.JavaPlugin

abstract class KotlinPlugin : JavaPlugin() {

	override fun onEnable() {
		try {
			MLib.register(this)
		} catch (_: IllegalStateException) {}

		super.onEnable()
	}

}