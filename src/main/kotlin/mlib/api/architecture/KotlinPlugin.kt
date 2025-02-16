package mlib.api.architecture

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import mlib.api.MLib

abstract class KotlinPlugin : SuspendingJavaPlugin() {

	override fun onEnable() {
		try {
			MLib.register(this)
		} catch (_: IllegalStateException) {}

		super.onEnable()
	}

}