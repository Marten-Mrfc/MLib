package mlib.api.forms

enum class FormType {
    INTEGER {
        override fun validate(input: String): Boolean = input.toIntOrNull() != null
        override fun parse(input: String): Any = input.toInt()
    },
    STRING {
        override fun validate(input: String): Boolean = true
        override fun parse(input: String): Any = input
    },
    BOOLEAN {
        override fun validate(input: String): Boolean = input.lowercase() in listOf("true", "false", "yes", "no")
        override fun parse(input: String): Any = input.lowercase() in listOf("true", "yes")
    };

    abstract fun validate(input: String): Boolean
    abstract fun parse(input: String): Any
}