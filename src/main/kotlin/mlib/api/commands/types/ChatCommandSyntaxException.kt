package mlib.api.commands.types

import com.mojang.brigadier.LiteralMessage
import com.mojang.brigadier.exceptions.CommandExceptionType
import com.mojang.brigadier.exceptions.CommandSyntaxException

class ChatCommandSyntaxException(
    type: CommandExceptionType,
    val chatMessage: Unit,
    input: String? = null,
    cursor: Int = -1
) : CommandSyntaxException(
    type, LiteralMessage(chatMessage.toString()),
    input, cursor
)