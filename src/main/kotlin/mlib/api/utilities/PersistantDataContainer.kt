package mlib.api.utilities

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.bukkit.NamespacedKey
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import java.lang.reflect.Type

private val gson = GsonBuilder()
    .registerTypeAdapter(Class::class.java, object : JsonSerializer<Class<*>>, JsonDeserializer<Class<*>> {
        override fun serialize(src: Class<*>, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            return JsonPrimitive(src.name)
        }

        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Class<*> {
            return Class.forName(json.asString)
        }
    }).create()

fun <T : Any> setCustomValue(meta: ItemMeta, plugin: Plugin, id: String, value: T) {
    val key = NamespacedKey(plugin, id)
    val container = meta.persistentDataContainer
    when (value) {
        is String -> container.set(key, PersistentDataType.STRING, value)
        is Boolean -> container.set(key, PersistentDataType.BOOLEAN, value)
        is Int -> container.set(key, PersistentDataType.INTEGER, value)
        is Double -> container.set(key, PersistentDataType.DOUBLE, value)
        is Float -> container.set(key, PersistentDataType.FLOAT, value)
        is Short -> container.set(key, PersistentDataType.SHORT, value)
        is Byte -> container.set(key, PersistentDataType.BYTE, value)
        is List<*> -> container.set(key, PersistentDataType.STRING, gson.toJson(value))
    }
}

fun <T : Any> checkCustomValue(meta: ItemMeta, plugin: Plugin, id: String, value: T): Boolean {
    val key = NamespacedKey(plugin, id)
    val container = meta.persistentDataContainer
    return when (value) {
        is String -> {
            val storedValue = container.get(key, PersistentDataType.STRING)
            if (storedValue != null && storedValue.startsWith("[")) {
                val type = object : TypeToken<List<String>>() {}.type
                gson.fromJson<List<String>>(storedValue, type).contains(value)
            } else {
                storedValue == value
            }
        }
        is Boolean -> container.get(key, PersistentDataType.BOOLEAN) == value
        is Int -> container.get(key, PersistentDataType.INTEGER) == value
        is Double -> container.get(key, PersistentDataType.DOUBLE) == value
        is Float -> container.get(key, PersistentDataType.FLOAT) == value
        is Short -> container.get(key, PersistentDataType.SHORT) == value
        is Byte -> container.get(key, PersistentDataType.BYTE) == value
        is List<*> -> {
            val json = container.get(key, PersistentDataType.STRING)
            val type = object : TypeToken<List<Any>>() {}.type
            gson.fromJson<List<Any>>(json, type).contains(value)
        }
        else -> false
    }
}

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