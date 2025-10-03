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
        ATOM,
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        NUMBER,
        IMPLICATOR,
        TERMINATOR,
        AND,
        OR,
        CUT,
        COMMENT,
        LEFT_PAREN,
        RIGHT_PAREN,
        LEFT_BRACKET,
        RIGHT_BRACKET,
    }

}
