package mlib.api.commands.builders.suggestionTypes

import com.mojang.brigadier.suggestion.SuggestionsBuilder
import mlib.api.commands.builders.BrigadierCommandContext
import org.bukkit.command.CommandSender

interface SuggestionType<T> {
    fun suggest(context: BrigadierCommandContext<CommandSender>, builder: SuggestionsBuilder)

    fun filter(predicate: (T) -> Boolean): SuggestionType<T>
    fun transform(transformer: (T) -> String): SuggestionType<T>
    fun matching(matcher: (input: String, value: String) -> Boolean): SuggestionType<T>
    fun sorted(comparator: Comparator<T>? = null): SuggestionType<T>
}
