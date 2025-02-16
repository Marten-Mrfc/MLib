package mlib.api.utilities

import org.bukkit.NamespacedKey
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin

/**
 * Sets a custom value in the persistent data container of an ItemMeta.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> setCustomValue(meta: ItemMeta, plugin: Plugin, id: String, value: T) {
    val key = NamespacedKey(plugin, id)
    when (value) {
        is String -> meta.persistentDataContainer.set(key, PersistentDataType.STRING, value)
        is Boolean -> meta.persistentDataContainer.set(key, PersistentDataType.BOOLEAN, value)
        is Int -> meta.persistentDataContainer.set(key, PersistentDataType.INTEGER, value)
        is Double -> meta.persistentDataContainer.set(key, PersistentDataType.DOUBLE, value)
        is Float -> meta.persistentDataContainer.set(key, PersistentDataType.FLOAT, value)
        is Short -> meta.persistentDataContainer.set(key, PersistentDataType.SHORT, value)
        is Byte -> meta.persistentDataContainer.set(key, PersistentDataType.BYTE, value)
        is List<*> -> when {
            value.all { it is String } -> {
                meta.persistentDataContainer.set(key, PersistentDataType.LIST.strings(), value as List<String>)
            }
            value.all { it is Int } -> {
                meta.persistentDataContainer.set(key, PersistentDataType.LIST.integers(), value as List<Int>)
            }
        }
    }
}

/**
 * Checks if a custom value exists and matches in the persistent data container.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> checkCustomValue(meta: ItemMeta, plugin: Plugin, id: String, value: T): Boolean {
    val key = NamespacedKey(plugin, id)
    return when (value) {
        is String -> meta.persistentDataContainer.get(key, PersistentDataType.STRING) == value
        is Boolean -> meta.persistentDataContainer.get(key, PersistentDataType.BOOLEAN) == value
        is Int -> meta.persistentDataContainer.get(key, PersistentDataType.INTEGER) == value
        is Double -> meta.persistentDataContainer.get(key, PersistentDataType.DOUBLE) == value
        is Float -> meta.persistentDataContainer.get(key, PersistentDataType.FLOAT) == value
        is Short -> meta.persistentDataContainer.get(key, PersistentDataType.SHORT) == value
        is Byte -> meta.persistentDataContainer.get(key, PersistentDataType.BYTE) == value
        is List<*> -> when {
            value.all { it is String } -> {
                val stored = meta.persistentDataContainer.get(key, PersistentDataType.LIST.strings())
                stored == value
            }
            value.all { it is Int } -> {
                val stored = meta.persistentDataContainer.get(key, PersistentDataType.LIST.integers())
                stored == value
            }
            else -> false
        }
        else -> false
    }
}

/**
 * Gets a custom value from the persistent data container.
 */
fun getCustomValue(meta: ItemMeta, plugin: Plugin, id: String): Any? {
    val key = NamespacedKey(plugin, id)
    val container = meta.persistentDataContainer
    return when {
        container.has(key, PersistentDataType.STRING) -> container.get(key, PersistentDataType.STRING)
        container.has(key, PersistentDataType.BOOLEAN) -> container.get(key, PersistentDataType.BOOLEAN)
        container.has(key, PersistentDataType.INTEGER) -> container.get(key, PersistentDataType.INTEGER)
        container.has(key, PersistentDataType.DOUBLE) -> container.get(key, PersistentDataType.DOUBLE)
        container.has(key, PersistentDataType.FLOAT) -> container.get(key, PersistentDataType.FLOAT)
        container.has(key, PersistentDataType.SHORT) -> container.get(key, PersistentDataType.SHORT)
        container.has(key, PersistentDataType.BYTE) -> container.get(key, PersistentDataType.BYTE)
        container.has(key, PersistentDataType.LIST.strings()) -> container.get(key, PersistentDataType.LIST.strings())
        container.has(key, PersistentDataType.LIST.integers()) -> container.get(key, PersistentDataType.LIST.integers())
        else -> null
    }
}