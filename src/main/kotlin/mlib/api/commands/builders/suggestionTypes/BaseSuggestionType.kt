package mlib.api.commands.builders.suggestionTypes

import com.mojang.brigadier.suggestion.SuggestionsBuilder
import mlib.api.commands.builders.BrigadierCommandContext
import org.bukkit.command.CommandSender

abstract class BaseSuggestionType<T> : SuggestionType<T> {
    protected var filters = mutableListOf<(T) -> Boolean>()
    protected var transformer: ((T) -> String)? = null
    protected var matcher: ((String, String) -> Boolean)? = null
    protected var comparator: Comparator<T>? = null

    abstract fun getValues(): Collection<T>

    override fun suggest(context: BrigadierCommandContext<CommandSender>, builder: SuggestionsBuilder) {
        val input = builder.remainingLowerCase

        var values = getValues()
            .filter { item -> filters.all { it(item) } }

        if (comparator != null) {
            values = values.sortedWith(comparator!!)
        }

        values.forEach { item ->
            val suggestion = transformer?.invoke(item) ?: item.toString()

            if (matcher == null) {
                if (suggestion.lowercase().startsWith(input)) {
                    builder.suggest(suggestion)
                }
            } else {
                if (matcher!!.invoke(input, suggestion.lowercase())) {
                    builder.suggest(suggestion)
                }
            }
        }
    }

    override fun filter(predicate: (T) -> Boolean): SuggestionType<T> {
        filters.add(predicate)
        return this
    }

    override fun transform(transformer: (T) -> String): SuggestionType<T> {
        this.transformer = transformer
        return this
    }

    override fun matching(matcher: (input: String, value: String) -> Boolean): SuggestionType<T> {
        this.matcher = matcher
        return this
    }

    override fun sorted(comparator: Comparator<T>?): SuggestionType<T> {
        this.comparator = comparator
        return this
    }
}