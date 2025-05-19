package mlib.api.commands.builders.suggestionTypes

import org.bukkit.Keyed
import org.bukkit.Registry

class RegistrySuggestionType<T : Keyed>(private val registry: Registry<T>) : BaseSuggestionType<T>() {
    override fun getValues(): Collection<T> = registry.toSet()
}

class EnumSuggestionType<E : Enum<E>>(private val enumClass: Class<E>) : BaseSuggestionType<E>() {
    override fun getValues(): Collection<E> = enumClass.enumConstants.asList()
}

class CollectionSuggestionType<T>(private val collection: Collection<T>) : BaseSuggestionType<T>() {
    override fun getValues(): Collection<T> = collection
}