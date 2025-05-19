package mlib.api.commands.builders.suggestionTypes

import mlib.api.commands.builders.ArgumentDSLBuilder
import org.bukkit.Keyed
import org.bukkit.Registry

object SuggestionPresets {

    fun <T : Keyed> registry(registry: Registry<T>): SuggestionType<T> = RegistrySuggestionType(registry)

    fun <E : Enum<E>> enum(enumClass: Class<E>): SuggestionType<E> = EnumSuggestionType(enumClass)

    fun <T> collection(collection: Collection<T>): SuggestionType<T> = CollectionSuggestionType(collection)
}

fun ArgumentDSLBuilder<*>.suggests(suggestionType: SuggestionType<*>) {
    this.suggests { builder ->
        suggestionType.suggest(this, builder)
    }
}