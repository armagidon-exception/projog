package ru.evorsiodev.lexer;

public enum TokenType {
    // CONTENT
    NUMBER,
    COMMENT,
    STRING,
    QUOTED_STRING,
    ANONYMOUS_VARIABLE,

    // OPERATORS
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    REFERENCE,
    CUT,

    // PUNCTUATION
    IMPLICATOR,
    TERMINATOR,
    COMMA,
    SEMICOLON,
    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_BRACKET,
    RIGHT_BRACKET,
    COLON,
    PIPE,
    HASH,

    // KEYWORDS
    AS,
    FAIL
}
