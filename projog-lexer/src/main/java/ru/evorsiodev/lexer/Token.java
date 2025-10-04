package ru.evorsiodev.lexer;

import lombok.Data;

@Data
public class Token {

    private final String contents;
    private final Type type;
    private final int rowStart;
    private final int colStart;
    private final int rowEnd;
    private final int colEnd;

    public enum Type {
        // CONTENT
        NUMBER,
        ATOM,
        COMMENT,
        VARIABLE,
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

        // KEYWORDS
        IMPORT,
        EXPORTS,
        INLINE,
        NEW
    }

    @Override
    public String toString() {
        return "Token{" +
                "contents='" + contents + '\'' +
                ", type=" + type +
                ", rowStart=" + rowStart +
                ", colStart=" + colStart +
                ", rowEnd=" + rowEnd +
                ", colEnd=" + colEnd +
                '}';
    }
}
