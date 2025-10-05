package ru.evorsiodev.lexer;

import lombok.Data;

@Data
public class Token {

    private final String contents;
    private final TokenType type;
    private final int rowStart;
    private final int colStart;
    private final int rowEnd;
    private final int colEnd;

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
